package com.kescoode.xmail.controller;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kescoode.xmail.db.AccountDao;
import com.kescoode.xmail.domain.Account;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import de.greenrobot.event.EventBus;

/**
 * 管理App全局邮件业务对象的开关类
 *
 * @author Kesco Lin
 */
public class MailManager {
    private static volatile MailManager SINGLETON = null;

    private final Context context;
    private final EventBus bus;
    private final List<Account> accounts = new CopyOnWriteArrayList<>();

    /**
     * 获取MailManager的单例
     *
     * @param context 必须是Application上下文
     * @return Manager的单例
     */
    public static MailManager getSingleTon(@NonNull Context context) {
        if (SINGLETON == null) {
            synchronized (MailManager.class) {
                if (SINGLETON == null) {
                    SINGLETON = new MailManager(context);
                }
            }
        }
        return SINGLETON;
    }

    public MailManager(Context context) {
        this.context = context;
        bus = EventBus.getDefault();
    }

    public void dB2Account() {
        AccountDao dao = new AccountDao(context);
        accounts.addAll(Arrays.asList(dao.getAllAccounts()));
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void postLoginEvent() {

    }
}
