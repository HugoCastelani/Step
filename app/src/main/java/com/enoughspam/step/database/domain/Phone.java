package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.enoughspam.step.database.wideDao.AreaDAO;
import com.enoughspam.step.database.wideDao.CountryDAO;

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

    /**
     * @return: returns a phone if it exists or null if it doesn't exist
     */
    public static Phone generateObject(@NonNull String number, @NonNull final String iso) {
        // removing any 0 left
        number = String.valueOf(Long.parseLong(number));

        final Country country = CountryDAO.findByIso(iso);
        Phone phone = null;

        // some countries don't have area code
        if (!country.getMask().isEmpty()) {

            // discover number's area and country
            StringBuilder areaCode = new StringBuilder(50);
            Area area = null;
            final int codeLength = String.valueOf(country.getCode()).length();

            for (int i = 0; i < codeLength; i++) {
                areaCode.append(number.charAt(i));

                area = AreaDAO.findByCode(Integer.valueOf(areaCode.toString()));
                if (area != null && area.getState().getCountry().getId() == country.getId()) {
                    number = number.substring(i + 1, number.length());
                }
            }

            if (area != null) phone = new Phone(Long.parseLong(number), area);

        } else {

            phone = new Phone(Long.parseLong(number), country);
        }

        return phone;
    }
}
