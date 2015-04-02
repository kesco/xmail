package com.kescoode.xmail.service;

import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.fsck.k9.mail.MessagingException;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.domain.*;
import com.kescoode.xmail.service.aidl.IRemoteMailService;
import com.kescoode.xmail.service.internal.CoreService;
import com.kescoode.xmail.service.task.SendMailCommand;
import com.kescoode.xmail.service.task.SettingsCheckCommand;
import com.kescoode.xmail.service.task.SyncFolderCommand;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * App中负责邮件收发功能的Service
 */
public class MailService extends CoreService {

    private final IRemoteMailService.Stub binder = new IRemoteMailService.Stub() {
        @Override
        public void login(String email, String password) throws RemoteException {
            EmailConfig config = manager.getEmailConfig(email);
            Account account = new Account(MailService.this, email, password, config);
            executor.execute(new SettingsCheckCommand(MailService.this, account));
        }

        @Override
        public void syncAll(long accountId) throws RemoteException {

        }

        @Override
        public void syncFolder(long accountId, String folder) throws RemoteException {
            Account account = manager.getAccount(accountId);
            LocalStore localStore = account.getLocalStore();
            try {
                localStore.checkSettings();
                executor.execute(new SyncFolderCommand(MailService.this, account,
                        localStore.getFolder(folder)));
            } catch (MessagingException e) {
                /* 根本不会异常 */
            }
        }

        @Override
        public void sendMail(long accountId, MailBuilder builder) throws RemoteException {
            Account account = manager.getAccount(accountId);
            executor.execute(new SendMailCommand(MailService.this, account, builder));
        }
    };

    public ExecutorService executor;
    private MailManager manager;

    public MailService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        executor = buildThreadPool();
        manager = MailManager.getSingleTon(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager.dB2Account();

        /* 返回原来CoreService的设置 */
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private ExecutorService buildThreadPool() {
        int cores = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(cores, cores * 2 > 4 ? 4 : cores * 2,
                300L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            executor.allowCoreThreadTimeOut(true);
        }
        return executor;
    }
}
