package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.local.LCountryDAO;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:12
 */

public class State extends Domain {
    private String name;
    private String countryKey;

    public State() {}

    public State(@NonNull final String key, @NonNull final String name,
                 @NonNull final String countryKey) {
        super(key);
        setName(name);
        setCountryKey(countryKey);
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull final String name) {
        this.name = name;
    }

    public String getCountryKey() {
        return countryKey;
    }

    public void setCountryKey(String countryKey) {
        this.countryKey = countryKey;
    }

    @Exclude
    public Country getCountry() {
        return LCountryDAO.get().findByColumn(LCountryDAO.key, countryKey);
    }
}
