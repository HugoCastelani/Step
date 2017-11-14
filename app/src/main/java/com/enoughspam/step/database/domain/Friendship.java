package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.util.Listeners;
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

    private User addedUser;
    private User addingUser;

    public void setAddedUser(@NonNull final User addedUser) {
        this.addedUser = addedUser;

        final String key = addedUser.getKey();
        if (key != null && !key.isEmpty()) {
            setAddedKey(key);
        }
    }

    /**
     * If user was already set or can be found at local database, you must
     * use the return, else, you must use wait for it with the listener
     */

    @Exclude
    public User getAddedUser(@Nullable final Listeners.UserListener listener) {
        if (addedUser == null) {
            if (listener == null) {
                addedUser = LUserDAO.get().findByColumn(LUserDAO.key, addedKey);

            } else {

                UserDAO.get().findByKey(addedKey, new Listeners.UserListener() {
                    @Override
                    public void onUserRetrieved(@NonNull User retrievedUser) {
                        addedUser = retrievedUser;
                        listener.onUserRetrieved(addedUser);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onUserRetrieved(addedUser);
            }
        }

        return addedUser;
    }

    public void setAddingUser(@NonNull final User addingUser) {
        this.addingUser = addingUser;

        final String key = addingUser.getKey();
        if (key != null && !key.isEmpty()) {
            setAddingKey(key);
        }
    }

    @Exclude
    public User getAddingUser(@Nullable final Listeners.UserListener listener) {
        if (addingUser == null) {
            if (listener == null) {
                addingUser = LUserDAO.get().findByColumn(LUserDAO.key, addingKey);

            } else {

                UserDAO.get().findByKey(addingKey, new Listeners.UserListener() {
                    @Override
                    public void onUserRetrieved(@NonNull User retrievedUser) {
                        addingUser = retrievedUser;
                        listener.onUserRetrieved(addingUser);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onUserRetrieved(addingUser);
            }
        }

        return addingUser;
    }
}