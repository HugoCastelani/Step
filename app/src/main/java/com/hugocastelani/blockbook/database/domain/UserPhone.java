package com.hugocastelani.blockbook.database.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.hugocastelani.blockbook.database.dao.local.LPhoneDAO;
import com.hugocastelani.blockbook.database.dao.local.LUserDAO;
import com.hugocastelani.blockbook.database.dao.wide.PhoneDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserDAO;
import com.hugocastelani.blockbook.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:36
 */

@IgnoreExtraProperties
public final class UserPhone {
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
    public User getUser(@Nullable final Listeners.ObjectListener<User> listener) {
        if (user == null) {
            if (listener == null) {
                user = LUserDAO.get().findByColumn(LUserDAO.key, userKey);

            } else {

                UserDAO.get().findByKey(userKey, new Listeners.ObjectListener<User>() {
                    @Override
                    public void onObjectRetrieved(@NonNull User retrievedUser) {
                        user = retrievedUser;
                        listener.onObjectRetrieved(user);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onObjectRetrieved(user);
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
    public Phone getPhone(@Nullable final Listeners.ObjectListener<Phone> listener) {
        if (phone == null) {
            if (listener == null) {
                phone = LPhoneDAO.get().findByColumn(LUserDAO.key, userKey);

            } else {

                PhoneDAO.get().findByKey(phoneKey, new Listeners.ObjectListener<Phone>() {
                    @Override
                    public void onObjectRetrieved(@NonNull Phone retrievedPhone) {
                        phone = retrievedPhone;
                        listener.onObjectRetrieved(phone);
                    }

                    @Override public void onError() {listener.onError();}
                });
            }

        } else {

            if (listener != null) {
                listener.onObjectRetrieved(phone);
            }
        }

        return phone;
    }
}
