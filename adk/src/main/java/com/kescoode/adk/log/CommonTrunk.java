package com.kescoode.adk.log;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 平时通用的{@link Logger.LogTrunk}
 *
 * @author Kesco Lin
 */
public class CommonTrunk extends Logger.LogTrunk {
    private static final Pattern ANONYMOUS_CLASS = Pattern.compile("\\$\\d+$");

    public CommonTrunk() {
        /* Empty */
    }

    @Override
    protected void i(String msg, Object... args) {
        Log.i(tagBuild(), String.format(msg, args));
    }

    @Override
    protected void d(String msg, Object... args) {
        Log.d(tagBuild(), String.format(msg, args));
    }

    @Override
    protected void e(String msg, Object... args) {
        Log.e(tagBuild(), String.format(msg, args));
    }

    @Override
    protected void w(String msg, Object... args) {
        Log.w(tagBuild(), String.format(msg, args));
    }

    private String tagBuild() {
        StringBuilder builder = new StringBuilder();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        if (stackTrace.length < 6) {
            throw new IllegalStateException(
                    "Synthetic stacktrace didn't have enough elements: are you using proguard?");
        }

        StackTraceElement target = stackTrace[3];
        String className = target.getClassName();
        Matcher matcher = ANONYMOUS_CLASS.matcher(className);
        if (matcher.find()) {
            className = matcher.replaceAll("");
        }
        builder.append(className.substring(className.lastIndexOf('.') + 1));
        builder.append(':');
        builder.append(target.getMethodName());
        builder.append('[');
        builder.append(target.getLineNumber());
        builder.append(']');

        return builder.toString();
    }

}
