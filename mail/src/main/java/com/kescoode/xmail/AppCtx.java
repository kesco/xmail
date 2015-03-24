package com.kescoode.xmail;

import android.app.Application;
import android.content.Context;
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

    }

}
