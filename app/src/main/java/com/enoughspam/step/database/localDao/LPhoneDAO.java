package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.wideDao.PhoneDAO;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:11
 */

public class LPhoneDAO {
    private LPhoneDAO() {}

    public static int create(@NonNull final Phone phone) {
        if (findById(phone.getID()) == null) {
            final ContentValues values = new ContentValues();

            if (phone.getArea().getCode() == 0) {
                values.put(PhoneDAO.ID, phone.getID());
                values.put(PhoneDAO.NUMBER, phone.getNumber());
                values.put(PhoneDAO.COUNTRY_ID, phone.getCountry().getID());

            } else {

                values.put(PhoneDAO.ID, phone.getID());
                values.put(PhoneDAO.NUMBER, phone.getNumber());
                values.put(PhoneDAO.AREA_CODE, phone.getArea().getCode());
            }

            return (int) DAOHandler.getLocalDatabase().insert(PhoneDAO.TABLE, null, values);
        }

        return -1;
    }

    public static Phone findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                PhoneDAO.TABLE, null, PhoneDAO.ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) phone = PhoneDAO.generate(cursor);

        cursor.close();
        return phone;
    }

    public static int exists(@NonNull final Phone phone) {
        final String number = String.valueOf(phone.getNumber());
        Cursor cursor;

        if (phone.getCountry() == null) {
            final String areaCode = String.valueOf(phone.getArea().getCode());

            cursor = DAOHandler.getLocalDatabase().query(PhoneDAO.TABLE, null,
                    PhoneDAO.NUMBER + " = ? AND " + PhoneDAO.AREA_CODE + " = ?", new String[] {number, areaCode},
                    null, null, null);

        } else {

            final String countryId = String.valueOf(phone.getCountry().getID());

            cursor = DAOHandler.getLocalDatabase().query(PhoneDAO.TABLE, null,
                    PhoneDAO.NUMBER + " = ? AND " + PhoneDAO.COUNTRY_ID + " = ? ", new String[] {number, countryId},
                    null, null, null);
        }

        Phone matchingPhone = null;

        if (cursor.moveToFirst()) matchingPhone = PhoneDAO.generate(cursor);

        cursor.close();

        if (matchingPhone == null) return -1;
        else return matchingPhone.getID();
    }
}
