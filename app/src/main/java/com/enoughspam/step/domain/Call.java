package com.enoughspam.step.domain;

import android.support.annotation.NonNull;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 22:34
 */

public class Call {
    private String name;
    private String number;

    public Call(@NonNull final String name, @NonNull final String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(@NonNull final String number) {
        this.number = number;
    }
}
