package com.kescoode.xmail;

import android.app.Application;
import android.content.Context;

import com.fsck.k9.mail.internet.BinaryTempFileBody;
import com.fsck.k9.mail.ssl.LocalKeyStore;
import com.kescoode.adk.log.CommonTrunk;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.controller.MailManager;

/**
 * App的{@link Application}
 *
 * @author Kesco Lin
 */
public class AppCtx extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.appendTrunk(new CommonTrunk());

        MailManager manager = MailManager.getSingleTon(this);
        manager.init();

        // TODO: K9的临时缓存，但是目录路径和多进程相关的影响到时候要评估下
        BinaryTempFileBody.setTempDirectory(getCacheDir());
        LocalKeyStore.setKeyStoreLocation(getDir("KeyStore", MODE_PRIVATE).toString());
    }

}
