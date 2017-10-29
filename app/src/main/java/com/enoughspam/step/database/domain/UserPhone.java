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
    private Integer userID;
    private Phone phone;
    private Integer phoneID;
    private boolean isProperty;

    // this variable joins user's id phone's id to enable finding existence at UserPhoneDAO
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

    public Integer getUserID() {
        return userID;
    }

    public void setUser(@NonNull final User user) {
        this.user = user;
        setUserID(user.getID());
        setUserIDPhoneID();
    }

    public void setUserID(@NonNegative final Integer userID) {
        this.userID = userID;
        if (user != null) {
            user.setID(userID);
        }
    }

    @Exclude
    public Phone getPhone() {
        return phone;
    }

    public Integer getPhoneID() {
        return phoneID;
    }

    public void setPhone(@NonNull final Phone phone) {
        this.phone = phone;
        setPhoneID(phone.getID());
    }

    public void setPhoneID(@NonNegative final Integer phoneID) {
        this.phoneID = phoneID;
        setUserIDPhoneID();
        if (phone != null) {
            phone.setID(phoneID);
        }
    }

    public boolean getIsProperty() {
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
