package com.kescoode.xmail.domain;

import com.fsck.k9.mail.Folder;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Store;

import java.util.List;

/**
 * Created by kesco on 15/3/23.
 */
public class LocalStore extends Store {

    @Override
    public Folder getFolder(String name) {
        return null;
    }

    @Override
    public List<? extends Folder> getPersonalNamespaces(boolean forceListAll) throws MessagingException {
        return null;
    }

    @Override
    public void checkSettings() throws MessagingException {

    }
}
