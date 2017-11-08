package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.local.LUserDAO;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Hugo Castelani
 * Date: 31/07/17
 * Time: 16:05
 */

@IgnoreExtraProperties
public class Notification {
    private String phoneKey;
    private String notifiedKey;
    private String notifyingKey;

    public Notification() {}

    public Notification(@NonNull String phoneKey, @NonNull String notifiedKey,
                        @NonNull String notifyingKey) {
        setPhoneKey(phoneKey);
        setNotifiedKey(notifiedKey);
        setNotifyingKey(notifyingKey);
    }

    public String getPhoneKey() {
        return phoneKey;
    }

    public void setPhoneKey(@NonNull String phoneKey) {
        this.phoneKey = phoneKey;
    }

    public String getNotifiedKey() {
        return notifiedKey;
    }

    public void setNotifiedKey(@NonNull String notifiedKey) {
        this.notifiedKey = notifiedKey;
    }

    public String getNotifyingKey() {
        return notifyingKey;
    }

    public void setNotifyingKey(@NonNull String notifyingKey) {
        this.notifyingKey = notifyingKey;
    }

    @Exclude
    public User getNotifiedUser() {
        return LUserDAO.get().findByColumn(LUserDAO.key, notifiedKey);
    }

    @Exclude
    public User getNotifyingUser() {
        return LUserDAO.get().findByColumn(LUserDAO.key, notifyingKey);
    }
}
