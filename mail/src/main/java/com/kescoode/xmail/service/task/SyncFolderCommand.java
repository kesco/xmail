package com.kescoode.xmail.service.task;

import android.content.Context;

import com.fsck.k9.mail.FetchProfile;
import com.fsck.k9.mail.Folder;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessageRetrievalListener;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Part;
import com.fsck.k9.mail.internet.MessageExtractor;
import com.fsck.k9.mail.store.RemoteStore;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.domain.LocalStore;
import com.kescoode.xmail.event.SyncFolderEvent;
import com.kescoode.xmail.service.task.internal.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 同步文件夹，目前只做Pull，不做Push
 *
 * @author Kesco Lin
 */
public class SyncFolderCommand extends Command {
    private RemoteStore remoteStore;
    private LocalStore localStore;
    private LocalFolder folder;

    public SyncFolderCommand(Context context, Account account, LocalFolder folder) {
        super(context, account);
        this.remoteStore = account.getRemoteStore();
        this.localStore = account.getLocalStore();
        this.folder = folder;
    }

    @Override
    public void task() {
        try {
            remoteStore.checkSettings();
            syncFolder(folder);
        } catch (MessagingException e) {
            Logger.e(e.getMessage());
            sendBroadCaset(new SyncFolderEvent(folder.getId(),SyncFolderEvent.FAIL,SyncFolderEvent.FAIL));
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

        // TODO: 以后要与本地对比，存入数据库
        int total = remote.getMessageCount();
        int unread = remote.getUnreadMessageCount();
        int flagged = remote.getFlaggedMessageCount();

        Logger.d("Remote Server:\ntotal: %d\nunread: %d\nflagged: %d", total, unread, flagged);

        // TODO: 目前是把邮件的所有部分都拉过来，以后做附件的时候优化
        List<? extends Message> remoteMessages = remote.getMessages(1, total, new Date(2000, 1, 1), null);
        List<? extends Message> syncEmails = new ArrayList<>();
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

        folder.close();
        remote.close();
    }

}
