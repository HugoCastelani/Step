package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:10
 */

public class Area {
    private int code;
    private String name;
    private State state;

    public Area(@NonNegative final int code, @NonNull final String name,
                @NonNull final State state) {
        this.code = code;
        this.name = name;
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(@NonNegative final int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(@NonNull final State state) {
        this.state = state;
    }
}
