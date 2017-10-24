package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:36
 */

public class UserPhone {
    private User user;
    private int userID;
    private Phone phone;
    private int phoneID;
    private boolean isProperty;

    // this variable joins user's ID phone's ID to enable finding existence at UserPhoneDAO
    private String userIDPhoneID;

    public UserPhone() {}

    public UserPhone(@NonNull final User user, @NonNull final Phone phone,
                     final boolean isProperty) {
        setUser(user);
        setPhone(phone);
        setUserIDPhoneID();
        this.isProperty = isProperty;
    }

    @Exclude
    public User getUser() {
        return user;
    }

    public int getUserID() {
        return userID;
    }

    public void setUser(@NonNull final User user) {
        this.user = user;
        setUserID(user.getID());
        setUserIDPhoneID();
    }

    public void setUserID(@NonNegative final int userID) {
        this.userID = userID;
        user.setID(userID);
    }

    @Exclude
    public Phone getPhone() {
        return phone;
    }

    public int getPhoneID() {
        return phoneID;
    }

    public void setPhone(@NonNull final Phone phone) {
        this.phone = phone;
        setPhoneID(phone.getID());
    }

    public void setPhoneID(@NonNegative final int phoneID) {
        this.phoneID = phoneID;
        setUserIDPhoneID();
        phone.setID(phoneID);
    }

    public boolean isProperty() {
        return isProperty;
    }

    public void setIsProperty(final boolean isProperty) {
        this.isProperty = isProperty;
    }

    public void setUserIDPhoneID() {
        userIDPhoneID = Integer.toString(getUserID()) + getPhoneID();
    }

    public String getUserIDPhoneID() {
        return userIDPhoneID;
    }
}
