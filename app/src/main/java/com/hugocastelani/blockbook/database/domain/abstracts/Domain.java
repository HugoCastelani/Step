package com.hugocastelani.blockbook.database.domain.abstracts;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 19:25
 */

@IgnoreExtraProperties
public class Domain implements Serializable {
    protected String key;

    public Domain() {}

    public Domain(@NonNull String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }
}
