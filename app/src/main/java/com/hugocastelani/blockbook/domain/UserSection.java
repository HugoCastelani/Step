package com.hugocastelani.blockbook.domain;

import android.support.annotation.NonNull;
import com.hugocastelani.blockbook.database.domain.User;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 15/11/17
 * Time: 14:17
 */

public final class UserSection {
    private String sectionName;
    private ArrayList<User> userList;

    public UserSection(@NonNull final String sectionName, @NonNull final ArrayList<User> userList) {
        this.sectionName = sectionName;
        this.userList = userList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public UserSection setSectionName(@NonNull final String sectionName) {
        this.sectionName = sectionName;
        return this;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public UserSection setUserList(@NonNull final ArrayList<User> userList) {
        this.userList = userList;
        return this;
    }

    public UserSection addUser(@NonNull final User user) {
        userList.add(user);
        return this;
    }
}
