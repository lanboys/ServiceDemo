package com.m520it.alipay;

import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 蓝兵 on 2018/2/22.
 */

public class PayUser implements Parcelable {

    private int age;
    private String name;
    private Messenger messenger;

    public PayUser(int age, String name, Messenger messenger) {
        this.age = age;
        this.name = name;
        this.messenger = messenger;
    }

    protected PayUser(Parcel in) {
        age = in.readInt();
        name = in.readString();
        messenger = in.readParcelable(Messenger.class.getClassLoader());
    }

    public static final Creator<PayUser> CREATOR = new Creator<PayUser>() {
        @Override
        public PayUser createFromParcel(Parcel in) {
            return new PayUser(in);
        }

        @Override
        public PayUser[] newArray(int size) {
            return new PayUser[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
        dest.writeParcelable(messenger, flags);
    }

    @Override
    public String toString() {
        return "PayUser{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", messenger=" + messenger +
                '}';
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Messenger getMessenger() {
        return messenger;
    }

    public void setMessenger(Messenger messenger) {
        this.messenger = messenger;
    }
}
