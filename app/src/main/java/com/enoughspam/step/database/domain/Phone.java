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
    private int countryID;
    private Area area;
    private int areaID;

    // this variable joins number and area/country ID to enable finding existence at PhoneDAO
    private String numberACID;

    public Phone() {}

    public Phone(@NonNegative final long number, @NonNull final Area area) {
        this.number = number;
        setArea(area);
        setNumberAID();
        countryID = -1;
    }

    // some countries don't have area
    public Phone(@NonNegative final long number, @NonNull final Country country) {
        this.number = number;
        setCountry(country);
        setNumberCID();
        areaID = -1;
    }

    public Phone(@NonNegative final int id, @NonNegative final long number,
                 @NonNull final Area area) {
        super(id);
        this.number = number;
        setArea(area);
        countryID = -1;
    }

    // some countries don't have area
    public Phone(@NonNegative final int id, @NonNegative final long number,
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

    public int getCountryID() {
        return countryID;
    }

    public void setCountry(Country country) {
        this.country = country;
        setCountryID(country.getID());
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public void setNumberAID() {
        numberACID = Long.toString(getNumber()) + getAreaID();
    }

    public void setNumberCID() {
        numberACID = Long.toString(getNumber()) + getCountryID();
    }

    public String getNumberACID() {
        return numberACID;
    }

    @Exclude
    public Area getArea() {
        return area;
    }

    public int getAreaID() {
        return areaID;
    }

    public void setArea(Area area) {
        this.area = area;
        setAreaID(area.getID());
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    /**
     * @return: returns a phone if it exists or null if it doesn't exist
     */
    public static Phone generateObject(@NonNull String number, @NonNull final String iso) {
        final long numberL = Long.parseLong(number);
        if (numberL <= 0) return null;    // a number can't be <= 0

        // any 0 left from country's code was removed
        number = String.valueOf(numberL);

        Country country = CountryDAO.findByISO(iso);
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

                final Area thisUserPhoneArea = LUserPhoneDAO.findThisUserPhone().getArea();
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

            area = AreaDAO.findByCode(Integer.valueOf(areaCode.toString()));
            if (area != null && area.getState().getCountry().getID() == country.getID()) {
                number = number.substring(i + 1, number.length());
            }
        }

        return new Object[] {area, number};
    }
}
