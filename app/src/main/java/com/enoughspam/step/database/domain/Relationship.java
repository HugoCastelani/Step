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
public final class Relationship {
    private String followingKey;
    private String followerKey;

    public Relationship() {}

    public Relationship(@NonNull final String followingKey, @NonNull final String followerKey) {
        setFollowingKey(followingKey);
        setFollowerKey(followerKey);
    }

    public String getFollowingKey() {
        return followingKey;
    }

    public void setFollowingKey(@NonNull final String followingKey) {
        this.followingKey = followingKey;
    }

    public String getFollowerKey() {
        return followerKey;
    }

    public void setFollowerKey(@NonNull final String followerKey) {
        this.followerKey = followerKey;
    }

    private User followingUser;
    private User followerUser;

    public void setFollowingUser(@NonNull final User followingUser) {
        this.followingUser = followingUser;

        final String key = followingUser.getKey();
        if (key != null && !key.isEmpty()) {
            setFollowingKey(key);
        }
    }

    @Exclude
    public User getFollowingUser(@Nullable final Listeners.ObjectListener<User> listener) {
        if (followingUser == null) {
            if (listener == null) {
                followingUser = LUserDAO.get().findByColumn(LUserDAO.key, followingKey);

            } else {

                UserDAO.get().findByKey(followingKey, new Listeners.ObjectListener<User>() {
                    @Override
                    public void onObjectRetrieved(@NonNull User retrievedUser) {
                        followingUser = retrievedUser;
                        listener.onObjectRetrieved(followingUser);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onObjectRetrieved(followingUser);
            }
        }

        return followingUser;
    }

    public void setFollowerUser(@NonNull final User followerUser) {
        this.followerUser = followerUser;

        final String key = followerUser.getKey();
        if (key != null && !key.isEmpty()) {
            setFollowerKey(key);
        }
    }

    @Exclude
    public User getFollowerUser(@Nullable final Listeners.ObjectListener<User> listener) {
        if (followerUser == null) {
            if (listener == null) {
                followerUser = LUserDAO.get().findByColumn(LUserDAO.key, followerKey);

            } else {

                UserDAO.get().findByKey(followerKey, new Listeners.ObjectListener<User>() {
                    @Override
                    public void onObjectRetrieved(@NonNull User retrievedUser) {
                        followerUser = retrievedUser;
                        listener.onObjectRetrieved(followerUser);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onObjectRetrieved(followerUser);
            }
        }

        return followerUser;
    }
}