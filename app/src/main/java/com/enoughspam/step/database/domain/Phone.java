package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:05
 */

public class Phone extends Domain {
    private long number;
    private int countryId;
    private int areaCode;
    private User user;

    public Phone(final long number, @NonNegative final int areaCode, @NonNull final User user) {
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    // some countries don't have area sCode
    public Phone(@NonNegative final int countryId, final long number, @NonNull final User user) {
        this.countryId = countryId;
        this.number = number;
        this.user = user;
    }

    public Phone(@NonNegative final int id, final long number, @NonNegative final int areaCode, @NonNull final User user) {
        super(id);
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    // some countries don't have area sCode
    public Phone(@NonNegative final int id, @NonNegative final int countryId, final long number, @NonNull final User user) {
        super(id);
        this.countryId = countryId;
        this.number = number;
        this.user = user;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(final long number) {
        this.number = number;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(@NonNegative final int countryId) {
        this.countryId = countryId;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(@NonNegative final int areaCode) {
        this.areaCode = areaCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NonNull final User user) {
        this.user = user;
    }
}