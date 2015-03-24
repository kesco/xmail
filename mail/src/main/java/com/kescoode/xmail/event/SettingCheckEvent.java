package com.kescoode.xmail.event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 检测帐号设置是否OK的事件类
 *
 * @author Kesco Lin
 */
public class SettingCheckEvent implements Parcelable {
    public static enum Type {
        SEND, RECEIVE
    }

    public final boolean ok;
    public final Type type;

    public SettingCheckEvent(boolean ok, Type type) {
        this.ok = ok;
        this.type = type;
    }

    private SettingCheckEvent(Parcel source) {
        this.ok = source.readByte() != 0;
        this.type = Type.valueOf(source.readString());
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (ok ? 1 : 0));
        dest.writeString(type.name());
    }

    public static final Parcelable.Creator<SettingCheckEvent> CREATOR = new Parcelable.Creator<SettingCheckEvent>() {
        public SettingCheckEvent createFromParcel(Parcel source) {
            return new SettingCheckEvent(source);
        }

        public SettingCheckEvent[] newArray(int size) {
            return new SettingCheckEvent[size];
        }
    };

}
