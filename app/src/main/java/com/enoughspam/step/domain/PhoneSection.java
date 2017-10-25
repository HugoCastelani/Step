package com.enoughspam.step.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.Phone;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 30/07/17
 * Time: 23:31
 */

public class PhoneSection {
    private String username;
    private List<Phone> phoneList;

    public PhoneSection(String username, List<Phone> phoneList) {
        this.username = username;
        this.phoneList = phoneList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull final String username) {
        this.username = username;
    }

    public List<Phone> getPhoneList() {
        return phoneList;
    }

    public void setPhoneList(@NonNull final List<Phone> phoneList) {
        this.phoneList = phoneList;
    }

    public boolean addPhone(@NonNull final Phone phone) {
        return phoneList.add(phone);
    }
}
