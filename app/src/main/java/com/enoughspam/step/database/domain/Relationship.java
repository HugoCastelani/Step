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
public class Relationship {
    private String followedKey;
    private String followerKey;

    public Relationship() {}

    public Relationship(@NonNull final String followedKey, @NonNull final String followerKey) {
        setFollowedKey(followedKey);
        setFollowerKey(followerKey);
    }

    public String getFollowedKey() {
        return followedKey;
    }

    public void setFollowedKey(@NonNull final String followedKey) {
        this.followedKey = followedKey;
    }

    public String getFollowerKey() {
        return followerKey;
    }

    public void setFollowerKey(@NonNull final String followerKey) {
        this.followerKey = followerKey;
    }

    private User followedUser;
    private User followerUser;

    public void setFollowedUser(@NonNull final User followedUser) {
        this.followedUser = followedUser;

        final String key = followedUser.getKey();
        if (key != null && !key.isEmpty()) {
            setFollowedKey(key);
        }
    }

    @Exclude
    public User getFollowedUser(@Nullable final Listeners.UserListener listener) {
        if (followedUser == null) {
            if (listener == null) {
                followedUser = LUserDAO.get().findByColumn(LUserDAO.key, followedKey);

            } else {

                UserDAO.get().findByKey(followedKey, new Listeners.UserListener() {
                    @Override
                    public void onUserRetrieved(@NonNull User retrievedUser) {
                        followedUser = retrievedUser;
                        listener.onUserRetrieved(followedUser);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onUserRetrieved(followedUser);
            }
        }

        return followedUser;
    }

    public void setFollowerUser(@NonNull final User followerUser) {
        this.followerUser = followerUser;

        final String key = followerUser.getKey();
        if (key != null && !key.isEmpty()) {
            setFollowerKey(key);
        }
    }

    @Exclude
    public User getFollowerUser(@Nullable final Listeners.UserListener listener) {
        if (followerUser == null) {
            if (listener == null) {
                followerUser = LUserDAO.get().findByColumn(LUserDAO.key, followerKey);

            } else {

                UserDAO.get().findByKey(followerKey, new Listeners.UserListener() {
                    @Override
                    public void onUserRetrieved(@NonNull User retrievedUser) {
                        followerUser = retrievedUser;
                        listener.onUserRetrieved(followerUser);
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });
            }

        } else {

            if (listener != null) {
                listener.onUserRetrieved(followerUser);
            }
        }

        return followerUser;
    }
}