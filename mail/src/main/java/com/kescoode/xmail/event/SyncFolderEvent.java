package com.kescoode.xmail.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 刷新邮件文件夹通知消息
 *
 * @author Kesco Lin
 */
public class SyncFolderEvent implements Parcelable {
    public static final int FAIL = -1;

    public final long folderId;
    public final int total;
    public final int synced;

    public SyncFolderEvent(long folderId, int total, int synced) {
        this.folderId = folderId;
        this.total = total;
        this.synced = synced;
    }

    private SyncFolderEvent(Parcel source) {
        this.folderId = source.readLong();
        this.total = source.readInt();
        this.synced = source.readInt();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(folderId);
        dest.writeInt(total);
        dest.writeInt(synced);
    }

    public static final Parcelable.Creator<SyncFolderEvent> CREATOR = new Parcelable.Creator<SyncFolderEvent>() {
        @Override
        public SyncFolderEvent createFromParcel(Parcel source) {
            return new SyncFolderEvent(source);
        }

        @Override
        public SyncFolderEvent[] newArray(int size) {
            return new SyncFolderEvent[size];
        }
    };
}
