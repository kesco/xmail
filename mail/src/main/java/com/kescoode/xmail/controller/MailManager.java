package com.kescoode.xmail.controller;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.kescoode.xmail.db.AccountDao;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.EmailConfig;
import com.kescoode.xmail.tool.EmailUtil;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    }

    /**
     * 把数据库内所有的邮箱帐户拉出来
     */
    public void dB2Account() {
        accounts.clear();
        AccountDao dao = new AccountDao(context);
        accounts.addAll(Arrays.asList(dao.getAllAccounts()));
    }

    /**
     * 获取所有的邮箱帐户(内存中的)
     *
     * @return 邮箱帐户列表
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     * 添加邮件帐户，这往往发生在
     * @param account
     */
    public void addAccount(Account account) {
        AccountDao dao = new AccountDao(context);
        dao.insertAccount2Db(account);
        accounts.add(account);
    }

    /**
     * 获取Asset中的邮箱配置
     *
     * @param email 用户邮箱地址
     * @return 邮箱配置类
     */
    public EmailConfig getEmailConfig(String email) {
        String domain = EmailUtil.subDomain(email)[1];
        AssetManager assetManager = context.getAssets();
        try {
            JSONArray jArray = new JSONArray(IOUtils.toString(assetManager.open("mail_config.json")));
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jObj = jArray.getJSONObject(i);
                if (jObj.getString("domain").equals(domain)) {
                    return new EmailConfig(jObj);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Config file has error");
        } catch (IOException e) {
            throw new RuntimeException("asset file has error or have no access to asset file");
        }
        throw new RuntimeException("Config file has error");
    }

}
