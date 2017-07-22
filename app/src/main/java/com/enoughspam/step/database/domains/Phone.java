package com.enoughspam.step.database.domains;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.MaskRespectful;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.Domain;

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

    public Phone(@MaskRespectful long number, @NonNegative int areaCode, @NonNull User user) {
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    // some countries don't have area code
    public Phone(@NonNegative int countryId, @MaskRespectful long number, @NonNull User user) {
        this.countryId = countryId;
        this.number = number;
        this.user = user;
    }

    public Phone(@NonNegative int id, @MaskRespectful long number,
                 @NonNegative int areaCode, @NonNull User user) {
        super(id);
        this.number = number;
        this.areaCode = areaCode;
        this.user = user;
    }

    // some countries don't have area code
    public Phone(@NonNegative int id, @NonNegative int countryId,
                 @MaskRespectful long number, @NonNull User user) {
        super(id);
        this.countryId = countryId;
        this.number = number;
        this.user = user;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(@MaskRespectful long number) {
        this.number = number;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(@NonNegative int countryId) {
        this.countryId = countryId;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(@NonNegative int areaCode) {
        this.areaCode = areaCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
    }
}
