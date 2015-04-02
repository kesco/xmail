package com.kescoode.xmail.domain;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import com.fsck.k9.mail.internet.MimeUtility;
import org.apache.james.mime4j.util.MimeUtil;

import java.io.File;

/**
 * 本地附件业务对象
 *
 * @author Kesco Lin
 */
public class LocalAttachment {
    private final Context context;
    private final String name;
    private long emailId = -1L;
    private final String mime;
    private final String path;
    private final long size;

    private volatile File file = null;

    public LocalAttachment(Context context, String path) {
        this.context = context;
        this.file = new File(path);
        this.name = this.file.getName();
        this.mime = MimeUtility.getMimeTypeByExtension(name);
        this.path = path;
        this.size = this.file.length();         // TODO: File#length()的效率可能不大行，以后重构
    }

    public synchronized File getFile() {
        if (file == null) {
            file = new File(path);
        }
        return file;
    }

    public String getName() {
        return name;
    }

    public void getEmailId(long id) {
        emailId = id;
    }

    public long getEmailId() {
        return emailId;
    }

    public String getMime() {
        return mime;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }
}
