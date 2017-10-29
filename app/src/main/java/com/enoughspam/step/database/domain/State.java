package com.enoughspam.step.database.domain;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:12
 */

public class State extends Domain {
    private String name;
    private Country country;
    private Integer countryID;

    public State() {}

    public State(@NonNegative final Integer id, @NonNegative final String name,
                 @NonNegative final Country country) {
        super(id);
        this.name = name;
        setCountry(country);
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNegative final String name) {
        this.name = name;
    }

    @Exclude
    public Country getCountry() {
        return country;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountry(@NonNegative final Country country) {
        this.country = country;
        countryID = country.getID();
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
        if (country != null) {
            country.setID(countryID);
        }
    }
}
