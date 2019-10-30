package com.huatu.android.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 周竹
 * @file ContactsInfo
 * @brief
 * @date 2018/4/27 上午12:19
 * Copyright (c) 2017
 * All rights reserved.
 */
public class ContactsInfo implements Parcelable {
    public String name;
    public String phoneNum;
    public String index;
    public int id;

    public ContactsInfo(String name, String phoneNum, String index, int id) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.index = index;
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phoneNum);
        dest.writeString(this.index);
        dest.writeInt(this.id);
    }

    protected ContactsInfo(Parcel in) {
        this.name = in.readString();
        this.phoneNum = in.readString();
        this.index = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<ContactsInfo> CREATOR = new Parcelable.Creator<ContactsInfo>() {
        @Override
        public ContactsInfo createFromParcel(Parcel source) {
            return new ContactsInfo(source);
        }

        @Override
        public ContactsInfo[] newArray(int size) {
            return new ContactsInfo[size];
        }
    };
}
