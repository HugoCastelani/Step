package com.hugocastelani.blockbook.database.domain;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:02
 */

public final class User extends Domain {
    private String socialKey;
    private String username;
    private String picURL;

    public User() {}

    public User(@NonNull final String socialKey, @NonNull final String username,
                @NonNull final String picURL) {
        setSocialKey(socialKey);
        setUsername(username);
        setPicURL(picURL);
    }

    public User(@NonNull final String key, @NonNull final String socialKey,
                @NonNull final String username, @NonNull final String picURL) {
        super(key);
        setSocialKey(socialKey);
        setUsername(username);
        setPicURL(picURL);
    }

    public String getSocialKey() {
        return socialKey;
    }

    public void setSocialKey(@NonNull final String socialKey) {
        this.socialKey = socialKey;
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

    @Nullable
    public CharSequence getSocialMedia(@NonNull final Resources resources) {
        switch (getSocialKey().charAt(getSocialKey().length() - 1)) {    // last char
            case '1': return resources.getText(R.string.profile_signed_via_facebook);
            case '2': return resources.getText(R.string.profile_signed_via_google);
            case '3': return resources.getText(R.string.profile_signed_via_twitter);
            default: return null;
        }
    }
}
