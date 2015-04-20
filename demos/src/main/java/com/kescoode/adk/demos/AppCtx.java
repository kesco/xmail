package com.kescoode.adk.demos;

import android.app.Application;
import com.kescoode.adk.log.CommonTrunk;
import com.kescoode.adk.log.DefaultCrashFileTrunk;
import com.kescoode.adk.log.Logger;

/**
 * Created by kesco on 15/4/20.
 */
public class AppCtx extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.appendTrunk(new CommonTrunk());
        Logger.appendTrunk(new DefaultCrashFileTrunk(this));
    }
}
