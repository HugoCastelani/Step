package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

import java.io.Serializable;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:02
 */

public class User extends Domain implements Serializable {
    private String socialID;
    private String userName;
    private String photoURL;

    public User(@NonNegative final int id, @NonNull final String userName,
                @NonNull final String photoURL) {
        super(id);
        this.userName = userName;
        this.photoURL = photoURL;
    }

    public User(@NonNull final String socialID, @NonNull final String userName,
                @NonNull final String photoURL) {
        this.socialID = socialID;
        this.userName = userName;
        this.photoURL = photoURL;
    }

    public User(@NonNegative final int id, @NonNull final String socialID,
                @NonNull final String userName, @NonNull final String photoURL) {
        this(id, userName, photoURL);
        this.socialID = socialID;
    }

    public String getSocialID() {
        return socialID;
    }

    public void setSocialID(@NonNull final String socialID) {
        this.socialID = socialID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull final String userName) {
        this.userName = userName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(@NonNull final String photoURL) {
        this.photoURL = photoURL;
    }
}
