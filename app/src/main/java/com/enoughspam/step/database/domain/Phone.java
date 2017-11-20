package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.local.LAreaDAO;
import com.enoughspam.step.database.dao.local.LCountryDAO;
import com.enoughspam.step.database.dao.local.LUserPhoneDAO;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:05
 */

public class Phone extends Domain {
    private Long number;
    private String areaKey;
    private String countryKey;

    // this variable joins number and area/country key to enable finding existence at PhoneDAO
    private String numberACKey;

    public Phone() {}

    public Phone(@NonNegative @NonNull final Long number, @NonNull final String areaKey,
                 @NonNull final String countryKey) {
        setNumber(number);
        setAreaKey(areaKey);
        setCountryKey(countryKey);
        setNumberACKey();
    }

    public Phone(@NonNull final String key, @NonNegative @NonNull final Long number,
                 @NonNull final String areaKey, @NonNull final String countryKey) {
        super(key);
        setNumber(number);
        setAreaKey(areaKey);
        setCountryKey(countryKey);
        setNumberACKey();
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getCountryKey() {
        return countryKey;
    }

    public void setCountryKey(@NonNull String countryKey) {
        this.countryKey = countryKey;
        setNumberACKey();
    }

    public String getAreaKey() {
        return areaKey;
    }

    public void setAreaKey(@NonNull String areaKey) {
        this.areaKey = areaKey;
        setNumberACKey();
    }

    public void setNumberACKey() {
        numberACKey = Long.toString(getNumber()) + getAreaKey() + getCountryKey();
    }

    public String getNumberACKey() {
        return numberACKey;
    }

    @Exclude
    public Country getCountry() {
        return LCountryDAO.get().findByColumn(LCountryDAO.key, countryKey);
    }

    @Exclude
    public Area getArea() {
        return LAreaDAO.get().findByColumn(LAreaDAO.key, areaKey);
    }

    /**
     * @return: returns a phone if it exists or null if it doesn't exist
     */
    public static Phone generateObject(@NonNull String number, @NonNull final String iso) {
        final Long numberL = Long.parseLong(number);
        if (numberL <= 0) return null;    // a number can't be <= 0

        // any 0 left from country's code was removed
        number = String.valueOf(numberL);

        Country country = LCountryDAO.get().findByColumn(LCountryDAO.ISO, iso);
        Phone phone;

        // countries without mask don't have area code
        if (!country.getMask().isEmpty()) {
            Object[] methodReturn = findAreaByNumber(number, country);
            Area area = (Area) methodReturn[0];
            number = (String) methodReturn[1];

            if (area == null) {
                // if no area was detected, there are two possibilities:
                // 1. Android is crazy and giving us wrong ISO (happened to me in Lineage OS), so
                // we're going to test if the user's phone can be found inside the actual number
                // 2. In some Samsung devices, android hides the phone's area if it's
                // the same as user's phone area, so that's what we're going to assume

                final Area thisUserPhoneArea = LUserPhoneDAO.get().findThisUserPhone().getArea();
                country = thisUserPhoneArea.getState().getCountry();

                methodReturn = findAreaByNumber(number, country);
                area = (Area) methodReturn[0];
                number = (String) methodReturn[1];

                if (area == null) {
                    phone = new Phone(Long.parseLong(number), thisUserPhoneArea.getKey(), "-1");
                } else {
                    phone = new Phone(Long.parseLong(number), area.getKey(), "-1");
                }

            } else {
                phone = new Phone(Long.parseLong(number), area.getKey(), "-1");
            }

        } else {

            phone = new Phone(Long.parseLong(number), "-1", country.getKey());
        }

        return phone;
    }

    /**
     * @return both area and new number
     */
    private static Object[] findAreaByNumber(@NonNull String number, @NonNull final Country country) {
        // find out how many digits the area's code has by first space
        final int codeLength = country.getMask().indexOf(" ");

        StringBuilder areaCode = new StringBuilder(50);
        Area area = null;

        for (int i = 0; i < codeLength; i++) {
            areaCode.append(number.charAt(i));    // add current position's digit

            area = LAreaDAO.get().findByColumn(LAreaDAO.CODE, areaCode.toString());
            if (area != null && area.getState().getCountry().getKey().equals(country.getKey())) {
                number = number.substring(i + 1, number.length());
            }
        }

        return new Object[] {area, number};
    }
}
