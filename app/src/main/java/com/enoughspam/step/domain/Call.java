package com.enoughspam.step.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.Phone;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 22:34
 */

public class Call {
    private String name;
    private Phone phone;

    public Call(@NonNull final String name, @NonNull final Phone phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(@NonNull final Phone phone) {
        this.phone = phone;
    }
}
