package com.enoughspam.step.database.domains;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 17:51
 */

public class CountryCode {
    private int code;
    private Country country;

    public CountryCode(int code, Country country) {
        this.code = code;
        this.country = country;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
