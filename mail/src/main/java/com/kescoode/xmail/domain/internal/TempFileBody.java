package com.kescoode.xmail.domain.internal;

import com.fsck.k9.mail.MessagingException;

import java.io.*;


public class TempFileBody extends BinaryAttachmentBody {
        private final File mFile;
        private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
        public TempFileBody(String filename) {
            mFile = new File(filename);
        }

        @Override
        public InputStream getInputStream() throws MessagingException {
            try {
                return new FileInputStream(mFile);
            } catch (FileNotFoundException e) {
                return new ByteArrayInputStream(EMPTY_BYTE_ARRAY);
            }
        }
    }