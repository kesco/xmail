package com.kescoode.adk.log;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 应用异常中断时记录于文件的{@link com.kescoode.adk.log.Logger.LogTrunk}
 *
 * @author Kesco Lin
 */
public class DefaultCrashFileTrunk extends Logger.LogTrunk {
    // TODO: Log的信息自定义化
    public DefaultCrashFileTrunk(Context context, File crashDir) {
        /* 把所有的线程没捕获的异常处理都统一做处理 */
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(
                context.getPackageName(),
                crashDir,
                Thread.getDefaultUncaughtExceptionHandler()
        ));
    }

    public DefaultCrashFileTrunk(Context context, String crashDir) {
        this(context, new File(crashDir));
    }

    public DefaultCrashFileTrunk(Context context) {
        this(context, new File(context.getFilesDir(), "log"));
    }

    private static final class CrashHandler implements Thread.UncaughtExceptionHandler {
        private String packageName;
        private File crashPath;
        private Thread.UncaughtExceptionHandler previousHandler;

        public CrashHandler(String packageName, File path, Thread.UncaughtExceptionHandler previous) {
            this.packageName = packageName;
            this.crashPath = path;
            this.previousHandler = previous;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try {
                appendMessage(ex);
            } catch (IOException e) {
                Log.e(Logger.class.getCanonicalName(), "Can not write logging messages to files");
            }
            previousHandler.uncaughtException(thread, ex);
        }

        @SuppressLint("SimpleDateFormat")
        private File generateCrashFile() {
            String fName = "crash-" + new SimpleDateFormat("yyyyMMdd").format(new Date());
            return new File(crashPath, fName);
        }

        private void appendMessage(Throwable ex) throws IOException {
            if (!crashPath.exists()) {
                if (!crashPath.mkdir()) {
                    Log.e(Logger.LOG_TAG, "Can not make the log dir");
                    throw new IOException("Can not make the log dir");
                }
            }
            PrintWriter output = new PrintWriter(new FileWriter(generateCrashFile(), true));
            output.append("App: ")
                    .append(packageName).
                    append("\t");
            output.append("Time: ")
                    .append(String.valueOf(System.currentTimeMillis()))
                    .append("\n");
            output.append("Android API: ")
                    .append(String.valueOf(Build.VERSION.SDK_INT))
                    .append("\t");
            output.append("System Version: ")
                    .append(System.getProperty("os.version"))
                    .append("\t");
            output.append("Device: ")
                    .append(Build.MODEL);
            output.append("\n");
            output.append("Message:\n");
            ex.printStackTrace(output);
            output.append("\n");
            output.close();
        }
    }

    @Override
    protected void i(String msg, Object... args) {
    }

    @Override
    protected void d(String msg, Object... args) {
    }

    @Override
    protected void e(String msg, Object... args) {
    }

    @Override
    protected void w(String msg, Object... args) {
    }
}
