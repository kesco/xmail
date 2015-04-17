package com.kescoode.xmail.db.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Looper;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.db.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库管理类，主要负责创建和升级
 *
 * @author Kesco Lin
 */
public class DBManager extends SQLiteOpenHelper {
    private final List<DataDelegate> tables = new ArrayList<>();


    public DBManager(Context context, String name, int version) {
        /* 目前CursorFactory和ErrorHandler都没想到具体用途，所以在构造方法上去掉 */
        super(context, name, null, version);

        tables.add(new AccountDao(context));
        tables.add(new AttachmentDao(context));
        tables.add(new EmailConfigDao(context));
        tables.add(new EmailDao(context));
        tables.add(new FolderDao(context));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DataDelegate delegate : tables) {
            delegate.onCreate(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (DataDelegate delegate : tables) {
            delegate.onUpgrade(db, oldVersion, newVersion);
        }
    }
}
