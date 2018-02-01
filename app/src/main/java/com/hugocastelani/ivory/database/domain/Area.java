package com.hugocastelani.ivory.database.domain;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.hugocastelani.ivory.annotation.NonNegative;
import com.hugocastelani.ivory.database.dao.local.LStateDAO;
import com.hugocastelani.ivory.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:10
 */

public final class Area extends Domain {
    private Integer code;
    private String name;
    private String stateKey;

    public Area() {}

    public Area(@NonNull final String key, @NonNegative @NonNull final Integer code,
                @NonNull final String name, @NonNull final String stateKey) {
        super(key);
        setCode(code);
        setName(name);
        setStateKey(stateKey);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(@NonNegative @NonNull final Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getStateKey() {
        return stateKey;
    }

    public void setStateKey(@NonNull final String stateKey) {
        this.stateKey = stateKey;
    }

    @Exclude
    public State getState() {
        return LStateDAO.get().findByColumn(LStateDAO.key, stateKey);
    }
}
