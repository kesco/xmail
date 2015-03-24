package com.kescoode.xmail.controller;

import android.content.Context;
import com.kescoode.xmail.domain.Account;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理App全局邮件业务对象的开关类
 *
 * @author Kesco Lin
 */
public class MailManager {
    private final Context context;

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public MailManager(Context context) {
        this.context = context;
    }


}
