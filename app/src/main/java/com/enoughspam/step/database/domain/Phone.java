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
    private Country country;
    private Area area;

    public Phone(@NonNegative final long number, @NonNull final Area area) {
        this.number = number;
        this.area = area;
    }

    // some countries don't have area
    public Phone(@NonNegative final long number, @NonNull final Country country) {
        this.number = number;
        this.country = country;
    }

    public Phone(@NonNegative final int id, @NonNegative final long number,
                 @NonNull final Area area) {
        super(id);
        this.number = number;
        this.area = area;
    }

    // some countries don't have area
    public Phone(@NonNegative final int id, @NonNegative final long number,
                 @NonNull final Country country) {
        super(id);
        this.number = number;
        this.country = country;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
