package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.local.LUserDAO;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 21:58
 */

@IgnoreExtraProperties
public class Friendship {
    private String addedKey;
    private String addingKey;

    public Friendship() {}

    public Friendship(@NonNull final String addedKey, @NonNull final String addingKey) {
        setAddedKey(addedKey);
        setAddingKey(addingKey);
    }

    public String getAddedKey() {
        return addedKey;
    }

    public void setAddedKey(@NonNull final String addedKey) {
        this.addedKey = addedKey;
    }

    public String getAddingKey() {
        return addingKey;
    }

    public void setAddingKey(@NonNull final String addingKey) {
        this.addingKey = addingKey;
    }

    @Exclude
    public User getAddedUser() {
        return LUserDAO.get().findByColumn(LUserDAO.key, addedKey);
    }

    @Exclude
    public User getAddingUser() {
        return LUserDAO.get().findByColumn(LUserDAO.key, addingKey);
    }
}
