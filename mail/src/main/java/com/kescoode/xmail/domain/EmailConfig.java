package com.kescoode.xmail.domain;

import android.database.Cursor;

import com.fsck.k9.mail.AuthType;
import com.fsck.k9.mail.ConnectionSecurity;
import com.fsck.k9.mail.ServerSettings;
import com.fsck.k9.mail.Transport;
import com.fsck.k9.mail.store.RemoteStore;
import com.fsck.k9.mail.store.imap.ImapStore;
import com.kescoode.xmail.tool.EmailUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件服务器配置业务对象
 *
 * @author Kesco Lin
 */
public class EmailConfig {
    private volatile long id = -1L;
    public final String domain;
    private final List<SettingRaw> raws;

    public EmailConfig(Cursor cursor) {
        int length = cursor.getCount();
        id = cursor.getInt(0);
        domain = cursor.getString(1);
        raws = new ArrayList<>(length);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < length; i++) {
                SettingRaw raw = new SettingRaw(cursor.getString(2),
                        cursor.getString(3), cursor.getInt(4) == 1, 2);
                raws.add(raw);
                cursor.moveToNext();
            }
        }
        cursor.close();
    }

    public EmailConfig(JSONObject json) throws JSONException {
        domain = json.getString("domain");
        JSONArray jArray = json.getJSONArray("settings");
        int length = jArray.length();
        raws = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            JSONObject jObj = jArray.getJSONObject(i);
            SettingRaw raw = new SettingRaw(jObj.getJSONObject("send").toString(),
                    jObj.getJSONObject("receive").toString(),
                    jObj.getBoolean("use_suffix"),
                    jObj.getInt("default"));
            raws.add(raw);
        }
    }

    public EmailConfig(EmailConfig one, EmailConfig two) {
        if (!one.domain.equals(two.domain)) {
            throw new IllegalArgumentException("The domains between two configs are not the same");
        }
        domain = one.domain;
        raws = new ArrayList<>();
        raws.addAll(one.raws);
        raws.addAll(two.raws);
    }

    /**
     * 获取默认的Server Uri数组，可以用来反序列化成{@link ServerSettings}
     *
     * @param user   用户邮箱
     * @param passwd 密码
     * @return Uri数组，第一个是发送服务器，第二个是接收服务器
     */
    public String[] getDefaultServerSettingUri(String user, String passwd) {
        ServerSettings[] settings = getDefaultServerSettings(user, passwd);
        String[] uris = new String[2];
        uris[0] = Transport.createTransportUri(settings[0]);
        uris[1] = RemoteStore.createStoreUri(settings[1]);
        return uris;
    }

    /**
     * 获取默认的{@link ServerSettings}数组
     *
     * @param user   用户邮箱
     * @param passwd 密码
     * @return {@link ServerSettings}数组，第一个是发送服务器，第二个是接收服务器
     */
    public ServerSettings[] getDefaultServerSettings(String user, String passwd) {
        ServerSettings[] settings = new ServerSettings[2];
        SettingRaw raw = getDefaultRaw();
        try {
            settings[0] = buildSendServer(new JSONObject(raw.send), user, passwd, raw.useSuffix);
            settings[1] = buildReceiveServer(new JSONObject(raw.receive), user, passwd,
                    raw.useSuffix);
        } catch (JSONException e) {
            throw new RuntimeException("Config is wrong");
        }
        return settings;
    }

    public SettingRaw getDefaultRaw() {
        if (raws.size() == 0) {
            throw new RuntimeException("Email Config has error!");
        }
        SettingRaw raw = raws.get(0);
        for (int i = 1; i < raws.size(); i++) {
            SettingRaw item = raws.get(i);
            if (raw.weight < item.weight) {
                raw = item;
            }
        }
        return raw;
    }

    private ServerSettings buildReceiveServer(JSONObject receive, String user, String passwd,
                                              boolean suffix) throws JSONException {
        String type = receive.getString("type");
        String server = receive.getString("server");
        int port = receive.getInt("port");
        String security = receive.getString("security");
        String email = suffix ? user : EmailUtil.subDomain(user)[0];
        Map<String, String> extras = null;

        if (type.equals("IMAP")) {
            /* Imap做个默认处理 */
            extras = new HashMap<>();
            extras.put(ImapStore.ImapStoreSettings.AUTODETECT_NAMESPACE_KEY,
                    Boolean.toString(true));
            extras.put(ImapStore.ImapStoreSettings.PATH_PREFIX_KEY, "");
        }

        /* AuthType目前都默认为普通密码，以后可能会添加多个选项 */
        ServerSettings settings = new ServerSettings(ServerSettings.Type.valueOf(type), server, port,
                ConnectionSecurity.valueOf(security), AuthType.PLAIN, email, passwd, null, extras);
        if (type.equals("IMAP")) {
            /* K9库对Imap的处理有点特别，所以这里做了个小小的Hacking */
            settings = ImapStore.decodeUri(ImapStore.createUri(settings));
        }
        return settings;
    }

    private ServerSettings buildSendServer(JSONObject send, String user, String passwd,
                                           boolean suffix) throws JSONException {
        String type = send.getString("type");
        String server = send.getString("server");
        int port = send.getInt("port");
        String security = send.getString("security");
        String email = suffix ? user : EmailUtil.subDomain(user)[0];

        /* AuthType目前都默认为普通密码，以后可能会添加多个选项 */
        return new ServerSettings(ServerSettings.Type.valueOf(type), server, port,
                ConnectionSecurity.valueOf(security), AuthType.PLAIN, email, passwd, null);
    }

    public synchronized void setId(long id) {
        this.id = id;
    }

    public synchronized long getId() {
        return id;
    }

    public static class SettingRaw {
        public final String send;
        public final String receive;
        public final boolean useSuffix;
        public final int weight;

        public SettingRaw(String send, String receive, boolean useSuffix, int weight) {
            this.send = send;
            this.receive = receive;
            this.useSuffix = useSuffix;
            this.weight = weight;
        }
    }

}
