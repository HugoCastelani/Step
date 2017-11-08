package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

        final String key = user.getKey();
        if (key != null && !key.isEmpty()) {
            setUserKey(key);
        }

        return this;
    }

    @Exclude
    public User getUser(@Nullable final Listeners.UserListener listener) {
        if (user == null) {
            if (listener == null) {
                user = LUserDAO.get().findByColumn(LUserDAO.key, userKey);

            } else {

                UserDAO.get().findByKey(userKey, new Listeners.UserListener() {
                    @Override
                    public void onUserRetrieved(@NonNull User retrievedUser) {
                        user = retrievedUser;
                        listener.onUserRetrieved(user);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }
        }

        return user;
    }

    public UserPhone setPhone(@NonNull final Phone phone) {
        this.phone = phone;

        final String key = phone.getKey();
        if (key != null && !key.isEmpty()) {
            setPhoneKey(key);
        }

        return this;
    }

    @Exclude
    public Phone getPhone(@Nullable final Listeners.PhoneListener listener) {
        if (phone == null) {
            if (listener == null) {
                phone = LPhoneDAO.get().findByColumn(LUserDAO.key, userKey);

            } else {

                PhoneDAO.get().findByKey(phoneKey, new Listeners.PhoneListener() {
                    @Override
                    public void onPhoneRetrieved(@NonNull Phone retrievedPhone) {
                        phone = retrievedPhone;
                        listener.onPhoneRetrieved(phone);
                    }

                    @Override public void onError() {listener.onError();}
                });
            }
        }

        return phone;
    }
}
