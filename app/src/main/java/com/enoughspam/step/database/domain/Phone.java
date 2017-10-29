package com.enoughspam.step.database.domain;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.enoughspam.step.database.localDao.AreaDAO;
import com.enoughspam.step.database.localDao.CountryDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.google.firebase.database.Exclude;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:05
 */

public class Phone extends Domain {
    private long number;
    private Country country;
    private Integer countryID;
    private Area area;
    private Integer areaID;

    // this variable joins number and area/country id to enable finding existence at PhoneDAO
    private String numberACID;

    public Phone() {}

    public Phone(@NonNegative final long number, @NonNull final Area area) {
        this.number = number;
        setArea(area);
        countryID = -1;
        setNumberACID();
    }

    // some countries don't have area
    public Phone(@NonNegative final long number, @NonNull final Country country) {
        this.number = number;
        setCountry(country);
        areaID = -1;
        setNumberACID();
    }

    public Phone(@NonNegative final Integer id, @NonNegative final long number,
                 @NonNull final Area area) {
        super(id);
        this.number = number;
        setArea(area);
        countryID = -1;
    }

    // some countries don't have area
    public Phone(@NonNegative final Integer id, @NonNegative final long number,
                 @NonNull final Country country) {
        super(id);
        this.number = number;
        setCountry(country);
        areaID = -1;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    @Exclude
    public Country getCountry() {
        return country;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountry(Country country) {
        this.country = country;
        this.countryID = country.getID();
        setNumberACID();
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
        if (country != null) {
            country.setID(countryID);
        }
        setNumberACID();
    }

    @Exclude
    public Area getArea() {
        return area;
    }

    public Integer getAreaID() {
        return areaID;
    }

    public void setArea(Area area) {
        this.area = area;
        this.areaID = area.getID();
        setNumberACID();
    }

    public void setAreaID(Integer areaID) {
        this.areaID = areaID;
        if (area != null) {
            area.setID(areaID);
        }
        setNumberACID();
    }

    public void setNumberACID() {
        numberACID = Long.toString(getNumber()) + getAreaID() + getCountryID();
    }

    public String getNumberACID() {
        return numberACID;
    }

    /**
     * @return: returns a phone if it exists or null if it doesn't exist
     */
    public static Phone generateObject(@NonNull String number, @NonNull final String iso) {
        final long numberL = Long.parseLong(number);
        if (numberL <= 0) return null;    // a number can't be <= 0

        // any 0 left from country's code was removed
        number = String.valueOf(numberL);

        Country country = CountryDAO.get().findByColumn(CountryDAO.ISO, iso);
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
                    phone = new Phone(Long.parseLong(number), thisUserPhoneArea);
                } else {
                    phone = new Phone(Long.parseLong(number), area);
                }

            } else {
                phone = new Phone(Long.parseLong(number), area);
            }

        } else {

            phone = new Phone(Long.parseLong(number), country);
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

            area = AreaDAO.get().findByColumn(AreaDAO.CODE, areaCode.toString());
            if (area != null && area.getState().getCountry().getID() == country.getID()) {
                number = number.substring(i + 1, number.length());
            }
        }

        return new Object[] {area, number};
    }
}
