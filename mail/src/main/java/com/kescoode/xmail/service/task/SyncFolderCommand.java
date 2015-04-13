package com.kescoode.xmail.service.task;

import android.content.Context;
import com.fsck.k9.mail.*;
import com.fsck.k9.mail.internet.MessageExtractor;
import com.fsck.k9.mail.store.RemoteStore;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.db.FolderDao;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.domain.LocalStore;
import com.kescoode.xmail.event.SyncFolderEvent;
import com.kescoode.xmail.service.task.internal.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 同步文件夹，目前只做Pull，不做Push
 *
 * @author Kesco Lin
 */
public class SyncFolderCommand extends Command {
    private final RemoteStore remoteStore;
    private final LocalStore localStore;
    private final LocalFolder folder;
    private final int page;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param account 邮件帐户
     * @param folder  邮件文件夹
     * @param page    需要更新的页数，以0为基准
     */
    public SyncFolderCommand(Context context, Account account, LocalFolder folder, int page) {
        super(context, account);
        this.remoteStore = account.getRemoteStore();
        this.localStore = account.getLocalStore();
        this.folder = folder;
        this.page = page + 1;
    }


    @Override
    public void task() {
        try {
            remoteStore.checkSettings();
            syncFolder(folder);
            folder.setLastUpdate(System.currentTimeMillis());
            FolderDao dao = new FolderDao(context);
            dao.updateFolder4Id(folder);
        } catch (MessagingException e) {
            Logger.e(e.getMessage());
            sendBroadCaset(new SyncFolderEvent(folder.getId(), SyncFolderEvent.FAIL, SyncFolderEvent.FAIL));
        }
    }

    protected void syncFolder(final LocalFolder folder) throws MessagingException {
        /* 和K-9一样，我们不同步发件箱 */
        String folderName = folder.getName();
        if (folderName.equals(account.getOutboxFolderName())) {
            return;
        }

        localStore.checkSettings();
        final Folder remote = remoteStore.getFolder(folder.getName());
        folder.open(Folder.OPEN_MODE_RW);
        remote.open(Folder.OPEN_MODE_RW);

        int total = remote.getMessageCount();
        int unread = remote.getUnreadMessageCount();
        int flagged = remote.getFlaggedMessageCount();
        Logger.d("Remote Server:\ntotal: %d\nunread: %d\nflagged: %d", total, unread, flagged);
        int news = Math.max(total - page * account.getDisplayCount(), 0) + 1;
        // TODO: 这里的未读数是错误的，要改正
        folder.setUnreadMessageCount(news + folder.getUnreadMessageCount());
        folder.setMessageCount(total);
        folder.setFlaggedCount(flagged);
        List<? extends Message> remoteMessages = remote.getMessages(news, total,
                null /* 根据K9的源码，这里是默认为空 */, null);
        downloadMessage(remote, remoteMessages);
        sendBroadCaset(new SyncFolderEvent(folder.getId(), total, news));
        folder.close();
        remote.close();
    }

    protected void downloadMessage(final Folder remote, List<? extends Message> remoteMessages)
            throws MessagingException {
        // TODO: 目前是把邮件的所有部分都拉过来，以后做附件的时候优化
        final List<Message> smallEmails = new ArrayList<>();
        final List<Message> largerEmails = new ArrayList<>();

        FetchProfile fp = new FetchProfile();
        if (remote.supportsFetchingFlags()) {
            fp.add(FetchProfile.Item.FLAGS);
        }
        fp.add(FetchProfile.Item.ENVELOPE);
        Logger.d("Fetch Basic Emails");
        remote.fetch(remoteMessages, fp, new MessageRetrievalListener() {
            @Override
            public void messageStarted(String uid, int number, int ofTotal) {
                /* Empty */
            }

            @Override
            public void messageFinished(Message message, int number, int ofTotal) {
                if (account.getMaximumAutoDownloadMessageSize() > 0 &&
                        message.getSize() > account.getMaximumAutoDownloadMessageSize()) {
                    largerEmails.add(message);
                } else {
                    smallEmails.add(message);
                }
            }

            @Override
            public void messagesFinished(int total) {
                /* Empty */
            }
        });

        Logger.d("Download %d emails smaller than the larger settings", smallEmails.size());
        fp = new FetchProfile();
        fp.add(FetchProfile.Item.BODY);
        remote.fetch(smallEmails, fp, new MessageRetrievalListener() {
            @Override
            public void messageStarted(String uid, int number, int ofTotal) {
                /* Empty */
            }

            @Override
            public void messageFinished(Message message, int number, int ofTotal) {
                try {
                    folder.appendMessages(Collections.singletonList(message));
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messagesFinished(int total) {
                /* Empty */
            }
        });

        Logger.d("Download %d larger emails", largerEmails.size());
        fp = new FetchProfile();
        fp.add(FetchProfile.Item.STRUCTURE);
        remote.fetch(largerEmails, fp, new MessageRetrievalListener() {
                    @Override
                    public void messageStarted(String uid, int number, int ofTotal) {
                    }

                    @Override
                    public void messageFinished(Message message, int number, int ofTotal) {
                        try {
                            if (message.getBody() == null) {
                                FetchProfile tmpFp = new FetchProfile();
                                tmpFp.add(FetchProfile.Item.BODY_SANE);
                                remote.fetch(Collections.singletonList(message), tmpFp, null);
                            } else {
                                Set<Part> viewables = MessageExtractor.collectTextParts(message);
                                for (Part part : viewables) {
                                    remote.fetchPart(message, part, null);
                                }
                            }
                            folder.appendMessages(Collections.singletonList(message));
                        } catch (MessagingException e) {
                            throw new RuntimeException("Can fetch all part from remote mail.");
                        }
                    }

                    @Override
                    public void messagesFinished(int total) {
                    }
                }
        );
    }

}
