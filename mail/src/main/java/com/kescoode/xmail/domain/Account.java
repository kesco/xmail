package com.kescoode.xmail.domain;

import android.content.Context;
import android.database.Cursor;

import com.fsck.k9.mail.NetworkType;
import com.fsck.k9.mail.store.StoreConfig;

/**
 * 邮件帐户业务对象
 *
 * @author Kesco Lin
 */
public class Account implements StoreConfig {

    private final Context context;

    public Account(Context context) {
        this.context = context;
    }

    public Account(Context context, Cursor cursor) {
        this.context = context;
    }

    @Override
    public String getStoreUri() {
        return null;
    }

    @Override
    public String getTransportUri() {
        return null;
    }

    @Override
    public boolean subscribedFoldersOnly() {
        return false;
    }

    @Override
    public boolean useCompression(NetworkType type) {
        return false;
    }

    @Override
    public String getInboxFolderName() {
        return null;
    }

    @Override
    public String getOutboxFolderName() {
        return null;
    }

    @Override
    public String getDraftsFolderName() {
        return null;
    }

    @Override
    public void setDraftsFolderName(String name) {

    }

    @Override
    public void setTrashFolderName(String name) {

    }

    @Override
    public void setSpamFolderName(String name) {

    }

    @Override
    public void setSentFolderName(String name) {

    }

    @Override
    public void setAutoExpandFolderName(String name) {

    }

    @Override
    public void setInboxFolderName(String name) {

    }

    @Override
    public int getMaximumAutoDownloadMessageSize() {
        return 0;
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
        return false;
    }

    @Override
    public int getDisplayCount() {
        return 0;
    }

    @Override
    public int getIdleRefreshMinutes() {
        return 0;
    }

}
