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
    private String userName;
    private List<Phone> phoneList;

    public PhoneSection(String userName, List<Phone> phoneList) {
        this.userName = userName;
        this.phoneList = phoneList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(@NonNull final String userName) {
        this.userName = userName;
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
