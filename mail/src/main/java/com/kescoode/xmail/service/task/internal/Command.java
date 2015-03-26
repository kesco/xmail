package com.kescoode.xmail.service.task.internal;

import android.content.Context;
import android.os.Process;
import android.support.annotation.NonNull;

import com.kescoode.xmail.domain.Account;

/**
 * 用Runnable封装的邮件命令
 *
 * @author Kesco Lin
 */
public abstract class Command implements Runnable,Comparable<Command> {

    public enum Priority {
        HIGH(-1), NORMAL(0), LOW(1);

        private byte mLevel;

        private Priority(int level) {
            mLevel = (byte) level;
        }
    }

    protected final Context context;
    private Priority priority = Priority.NORMAL;
    protected final Account account;

    public Command(Context context,Account account) {
        this.context = context;
        this.account = account;
    }

    public final void setPriority(@NonNull Priority priority) {
        this.priority = priority;
    }

    @Override
    public final void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        task();
    }

    public abstract void task();

    @Override
    public int compareTo(@NonNull Command another) {
        if (another.priority.mLevel == priority.mLevel) {
            return 0;
        } else if (another.priority.mLevel < priority.mLevel) {
            return 1;
        } else {
            return -1;
        }
    }
}
