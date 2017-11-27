package com.enoughspam.step.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.Phone;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 30/07/17
 * Time: 23:31
 */

public final class PhoneSection {
    private String username;
    private ArrayList<Phone> phoneList;
    private Boolean isSelected;

    public PhoneSection(@NonNull final String username, @NonNull final ArrayList<Phone> phoneList) {
        this.username = username;
        this.phoneList = phoneList;
        this.isSelected = false;
    }

    public String getUsername() {
        return username;
    }

    public PhoneSection setUsername(@NonNull final String username) {
        this.username = username;
        return this;
    }

    public ArrayList<Phone> getPhoneList() {
        return phoneList;
    }

    public PhoneSection setPhoneList(@NonNull final ArrayList<Phone> phoneList) {
        this.phoneList = phoneList;
        return this;
    }

    public PhoneSection addPhone(@NonNull final Phone phone) {
        phoneList.add(phone);
        return this;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(@NonNull final Boolean selected) {
        isSelected = selected;
    }
}
