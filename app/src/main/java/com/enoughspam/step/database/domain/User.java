package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:02
 */

public class User extends Domain {
    private String socialID;
    private String username;
    private String picURL;

    public User() {}

    public User(@NonNegative final Integer id, @NonNull final String username,
                @NonNull final String picURL) {
        super(id);
        this.username = username;
        this.picURL = picURL;
    }

    public User(@NonNull final String socialID, @NonNull final String username,
                @NonNull final String picURL) {
        this.socialID = socialID;
        this.username = username;
        this.picURL = picURL;
    }

    public User(@NonNegative final Integer id, @NonNull final String socialID,
                @NonNull final String username, @NonNull final String picURL) {
        this(id, username, picURL);
        this.socialID = socialID;
    }

    public String getSocialID() {
        return socialID;
    }

    public void setSocialID(@NonNull final String socialID) {
        this.socialID = socialID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull final String username) {
        this.username = username;
    }

    public String getPicURL() {
        return picURL;
    }

    public void setPicURL(@NonNull final String picURL) {
        this.picURL = picURL;
    }
}
