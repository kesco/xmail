package com.kescoode.xmail.domain;

import android.content.Context;

import com.fsck.k9.mail.Folder;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Store;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.db.FolderDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本地接收服务器的映射业务对象
 *
 * @author Kesco Lin
 */
public class LocalStore extends Store {
    private final Context context;
    private final Account account;

    private List<LocalFolder> folders = null;

    public LocalStore(Context context, Account account) {
        this.context = context;
        this.account = account;
    }

    public void syncRemote(List<? extends Folder> remotes, boolean refresh) {
        FolderDao dao = new FolderDao(context);
        if (refresh) {
            // TODO: 加入更新Folders
        } else {
            folders = new ArrayList<>();
            for (Folder remote : remotes) {
                try {
                    remote.open(Folder.OPEN_MODE_RO);
                    LocalFolder folder = new LocalFolder(context, account, remote);
                    remote.close();
                    dao.insertFolder2DB(folder);
                    folders.add(folder);
                } catch (MessagingException e) {
                    Logger.e("Error in get infomation from Folder %s", remote.getName());
                }
            }
        }
    }

    @Override
    public LocalFolder getFolder(String name) {
        for (LocalFolder folder : folders) {
            if (folder.getName().equals(name)) {
                return folder;
            }
        }
        return null;
    }

    @Override
    public List<? extends Folder> getPersonalNamespaces(boolean forceListAll) throws MessagingException {
        /* 数据库查询会有点慢，这个请求要放在异步线程上做 */
        FolderDao dao = new FolderDao(context);
        folders = new ArrayList<>(Arrays.asList(dao.selectFoldersFromDB(account)));

        return folders;
    }

    @Override
    public void checkSettings() throws MessagingException {
        if (folders == null) {
            /* 初始化 */
            getPersonalNamespaces(true);
        }
    }

    @Override
    public boolean isMoveCapable() {
        return true;
    }

    @Override
    public boolean isCopyCapable() {
        return true;
    }
}
