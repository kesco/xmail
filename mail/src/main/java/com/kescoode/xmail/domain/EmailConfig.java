package com.kescoode.xmail.domain;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 邮件服务器配置业务对象
 *
 * @author Kesco Lin
 */
public class EmailConfig {
    private final String domain;
    private final String sendServer;
    private final int sendPort;
    private final String receiveServer;
    private final int receivePort;
    private final boolean useSsl;
    private final boolean useSuffix;

    public EmailConfig(Cursor cursor) {
        domain = cursor.getString(1);
        sendServer = cursor.getString(2);
        sendPort = cursor.getInt(3);
        receiveServer = cursor.getString(4);
        receivePort = cursor.getInt(5);
        useSsl = cursor.getInt(6) == 1;
        useSuffix = cursor.getInt(7) == 1;
    }

    public EmailConfig(JSONObject json) throws JSONException {
        domain = json.getString("domain");
        sendServer = json.getString("send_server");
        sendPort = json.getInt("send_port");
        receiveServer = json.getString("receive_server");
        receivePort = json.getInt("receive_port");
        useSsl = json.getBoolean("use_ssl");
        useSuffix = json.getBoolean("use_suffix");
    }

    public String getDomain() {
        return domain;
    }

    public String getSendServer() {
        return sendServer;
    }

    public int getSendPort() {
        return sendPort;
    }

    public String getReceiveServer() {
        return receiveServer;
    }

    public int getReceivePort() {
        return receivePort;
    }

    public boolean isUseSsl() {
        return useSsl;
    }

    public boolean isUseSuffix() {
        return useSuffix;
    }

}
