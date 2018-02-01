package com.hugocastelani.ivory.domain;

import android.support.annotation.NonNull;

import com.hugocastelani.ivory.database.domain.Phone;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 22:34
 */

public final class Call {
    private String name;
    private Phone phone;
    private Boolean isSelected;

    public Call(@NonNull final String name, @NonNull final Phone phone) {
        this.name = name;
        this.phone = phone;
        this.isSelected = false;
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

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(@NonNull final Boolean selected) {
        isSelected = selected;
    }
}
