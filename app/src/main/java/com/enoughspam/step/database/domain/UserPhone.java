package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.local.LPhoneDAO;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.dao.wide.PhoneDAO;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:36
 */

@IgnoreExtraProperties
public class UserPhone {
    private String userKey;
    private String phoneKey;
    private boolean isProperty;
    private boolean isNotification;

    public UserPhone() {}

    public UserPhone(@NonNull final String userKey, @NonNull final String phoneKey,
                     final boolean isProperty, final boolean isNotification) {
        setUserKey(userKey);
        setPhoneKey(phoneKey);
        setProperty(isProperty);
        setNotification(isNotification);
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(@NonNull final String userKey) {
        this.userKey = userKey;
    }

    public String getPhoneKey() {
        return phoneKey;
    }

    public void setPhoneKey(@NonNull final String phoneKey) {
        this.phoneKey = phoneKey;
    }

    public boolean getIsProperty() {
        return isProperty;
    }

    public void setProperty(final boolean property) {
        isProperty = property;
    }

    public boolean getIsNotification() {
        return isNotification;
    }

    public void setNotification(final boolean notification) {
        isNotification = notification;
    }

    private User user;
    private Phone phone;

    public UserPhone setUser(@NonNull final User user) {
        this.user = user;
        return this;
    }

    @Exclude
    public User getUser() {
        if (user == null) {
            throw new UnsupportedOperationException("You must load user before getting it");
        } else {
            return user;
        }
    }

    @Exclude
    public UserPhone loadUserLocally() {
        user = LUserDAO.get().findByColumn(LUserDAO.key, userKey);
        return this;
    }

    @Exclude
    public UserPhone loadUserWidely(@NonNull final Listeners.AnswerListener listener) {
        UserDAO.get().findByKey(userKey, new Listeners.UserListener() {
            @Override
            public void onUserRetrieved(@NonNull User retrievedUser) {
                user = retrievedUser;
                listener.onAnswerRetrieved();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });
        return this;
    }

    public UserPhone setPhone(@NonNull final Phone phone) {
        this.phone = phone;
        return this;
    }

    @Exclude
    public Phone getPhone() {
        if (phone == null) {
            throw new UnsupportedOperationException("You must load phone before getting it");
        } else {
            return phone;
        }
    }

    @Exclude
    public UserPhone loadPhoneLocally() {
        phone =  LPhoneDAO.get().findByColumn(LPhoneDAO.key, phoneKey);
        return this;
    }

    @Exclude
    public UserPhone loadPhoneWidely(@NonNull final Listeners.AnswerListener listener) {
        PhoneDAO.get().findByKey(phoneKey, new Listeners.PhoneListener() {
            @Override
            public void onPhoneRetrieved(@NonNull Phone retrievedPhone) {
                phone = retrievedPhone;
                listener.onAnswerRetrieved();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });
        return this;
    }
}
