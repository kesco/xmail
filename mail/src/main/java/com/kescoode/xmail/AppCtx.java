package com.kescoode.xmail;

import android.app.Application;
import android.content.Context;
import com.kescoode.adk.log.CommonTrunk;
import com.kescoode.adk.log.Logger;

/**
 * App的{@link Application}
 *
 * @author Kesco Lin
 */
public class AppCtx extends Application {

    private static Context mCtx;

    @Override
    public void onCreate() {
        super.onCreate();

        mCtx = this;
        Logger.appendTrunk(new CommonTrunk());
    }

    public static Context getContext() {
        return mCtx;
    }

}
