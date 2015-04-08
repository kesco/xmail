package com.kescoode.xmail.domain;

import android.content.Context;
import android.database.Cursor;

import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.NetworkType;
import com.fsck.k9.mail.Transport;
import com.fsck.k9.mail.store.RemoteStore;
import com.fsck.k9.mail.store.StoreConfig;
import com.kescoode.xmail.R;
import com.kescoode.xmail.db.EmailConfigDao;
import com.kescoode.xmail.db.FolderDao;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 邮件帐户业务对象
 *
 * @author Kesco Lin
 */
public class Account implements StoreConfig {
    private static final String INBOX = "INBOX";
    private static final String OUTBOX = "K9MAIL_INTERNAL_OUTBOX";

    private final Context context;
    private volatile long id = -1;
    public final String name;
    public final String passwd;

    private final EmailConfig emailConfig;
    private final String transportUri;
    private final String storeUri;
    private volatile Transport transport = null;
    private volatile RemoteStore remoteStore = null;
    private final LocalStore loaclStore;
    private final Map<NetworkType, Boolean> compressionMap;
    private volatile String inboxFolder;
    private volatile String draftFolder = null;
    private volatile String trashFolder = null;
    private volatile String spamFolder = null;
    private volatile String sentFolder = null;
    private volatile String autoExpandFolder = null;
    private int maximumAutoDownloadMessageSize = 32768;

    public Account(Context context, Cursor cursor) {
        this.context = context;
        // TODO: 目前默认所有连接方式都压缩，以后待功能稳定后，重构为可选择
        compressionMap = new ConcurrentHashMap<>();
        compressionMap.put(NetworkType.MOBILE, true);
        compressionMap.put(NetworkType.OTHER, true);
        compressionMap.put(NetworkType.WIFI, true);
        inboxFolder = INBOX;
        this.loaclStore = new LocalStore(context, this);
        /* 读取数据库 */
        id = cursor.getLong(0);
        name = cursor.getString(1);
        passwd = cursor.getString(2);
        int configId = cursor.getInt(3);

        EmailConfigDao dao = new EmailConfigDao(context);
        emailConfig = dao.selectConfigFromDB(configId);
        String[] uris = emailConfig.getDefaultServerSettingUri(name, passwd);
        transportUri = uris[0];
        storeUri = uris[1];
    }

    public Account(Context context, String name, String passwd, EmailConfig config) {
        this.context = context;
        this.compressionMap = new ConcurrentHashMap<>();
        // TODO: 目前默认所有连接方式都压缩，以后待功能稳定后，重构为可选择
        this.compressionMap.put(NetworkType.MOBILE, true);
        this.compressionMap.put(NetworkType.OTHER, true);
        this.compressionMap.put(NetworkType.WIFI, true);
        inboxFolder = INBOX;
        this.loaclStore = new LocalStore(context, this);

        this.name = name;
        this.passwd = passwd;
        this.emailConfig = config;
        String[] uris = config.getDefaultServerSettingUri(name, passwd);
        this.transportUri = uris[0];
        this.storeUri = uris[1];
    }

    public synchronized RemoteStore getRemoteStore() {
        if (remoteStore == null) {
            try {
                remoteStore = (RemoteStore) RemoteStore.getInstance(context, this);
            } catch (MessagingException e) {
                throw new RuntimeException("Can not initial RemoteStore");
            }
        }
        return remoteStore;
    }

    public synchronized Transport getTransport() {
        if (transport == null) {
            try {
                transport = Transport.getInstance(context, this);
            } catch (MessagingException e) {
                throw new RuntimeException("Can not initial Transport");
            }
        }
        return transport;
    }

    public LocalStore getLocalStore() {
        return this.loaclStore;
    }

    public long getId() {
        return id;
    }

    public synchronized void setId(long id) {
        this.id = id;
    }

    public synchronized long getConfigId() {
        if (emailConfig.getId() == -1) {
            EmailConfigDao dao = new EmailConfigDao(context);
            dao.insertConfig2DB(emailConfig);
        }
        return emailConfig.getId();
    }

    @Override
    public String getStoreUri() {
        return storeUri;
    }

    @Override
    public String getTransportUri() {
        return transportUri;
    }

    @Override
    public boolean subscribedFoldersOnly() {
        /* 从K-9源码来看，这里都为false */
        return false;
    }

    @Override
    public boolean useCompression(NetworkType type) {
        Boolean useCompression = compressionMap.get(type);
        if (useCompression == null) {
            return true;
        }

        return useCompression;
    }

    @Override
    public String getInboxFolderName() {
        return inboxFolder;
    }

    @Override
    public String getOutboxFolderName() {
        return OUTBOX;
    }

    @Override
    public String getDraftsFolderName() {
        /* 目前没有实现草稿箱部分，所以置为null，
           而在K9库中，有影响的只有WebDav协议 */
        return draftFolder;
    }

    @Override
    public void setDraftsFolderName(String name) {
        draftFolder = name;
    }

    @Override
    public void setTrashFolderName(String name) {
        trashFolder = name;
    }

    @Override
    public void setSpamFolderName(String name) {
        spamFolder = name;
    }

    @Override
    public void setSentFolderName(String name) {
        sentFolder = name;
    }

    @Override
    public void setAutoExpandFolderName(String name) {
        autoExpandFolder = name;
    }

    @Override
    public synchronized void setInboxFolderName(String name) {
        inboxFolder = name;
    }

    @Override
    public int getMaximumAutoDownloadMessageSize() {
        return maximumAutoDownloadMessageSize;
    }

    @Override
    public boolean allowRemoteSearch() {
        return false;
    }

    @Override
    public boolean isRemoteSearchFullText() {
        return false;
    }

    @Override
    public boolean isPushPollOnConnect() {
        return true;
    }

    @Override
    public int getDisplayCount() {
        return 25;
    }

    @Override
    public int getIdleRefreshMinutes() {
        return 24;
    }

    public String getUserEmail() {
        return name;
    }

    public String getDisplayFolderName(String folder) {
        if (folder.equals(inboxFolder)) {
            return context.getString(R.string.inbox);
        } else {
            return null;
        }
    }

    public LocalFolder getFolder(String folder) {
        FolderDao dao = new FolderDao(context);
        return dao.selectFolder4Name(this, folder);
    }

}
