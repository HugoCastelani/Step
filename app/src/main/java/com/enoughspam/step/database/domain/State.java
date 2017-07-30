package com.enoughspam.step.database.domain;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:12
 */

public class State extends Domain {
    private String name;
    private Country country;

    public State(@NonNegative final int id, @NonNegative final String name,
                 @NonNegative final Country country) {
        super(id);
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNegative final String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(@NonNegative final Country country) {
        this.country = country;
    }
}
