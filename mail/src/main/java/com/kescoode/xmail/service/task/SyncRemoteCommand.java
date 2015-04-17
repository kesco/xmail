package com.kescoode.xmail.service.task;

import android.content.Context;

import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.service.task.internal.Command;

/**
 * 同步远程服务器，目前主要实现Pull，Push暂不考虑
 *
 * @author Kesco Lin
 */
public class SyncRemoteCommand extends Command {

    public SyncRemoteCommand(Context context, Account account) {
        super(context, account);
        // TODO: 继承SyncFolderCommand实现
        throw new UnsupportedOperationException("Have not code that!");
    }

    @Override
    public void task() {

    }
}
