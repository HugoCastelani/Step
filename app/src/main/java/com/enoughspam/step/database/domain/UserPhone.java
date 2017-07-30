package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:36
 */

public class UserPhone {
    private User user;
    private Phone phone;
    private boolean isProperty;

    public UserPhone(@NonNull final User user, @NonNull final Phone phone,
                     final boolean isProperty) {
        this.user = user;
        this.phone = phone;
        this.isProperty = isProperty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NonNull final User user) {
        this.user = user;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(@NonNull final Phone phone) {
        this.phone = phone;
    }

    public boolean isProperty() {
        return isProperty;
    }

    public void setIsProperty(final boolean isProperty) {
        this.isProperty = isProperty;
    }
}
