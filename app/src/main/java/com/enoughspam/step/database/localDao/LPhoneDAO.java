package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:11
 */

public class LPhoneDAO {
    public static final String TABLE = "phone";
    public static final String ID = "id";
    public static final String NUMBER = "number";
    public static final String COUNTRY_ID = "country_id";
    public static final String AREA_ID = "area_id";

    private LPhoneDAO() {}

    public static Phone generate(@NonNull final Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(AREA_ID)) == -1) {
            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    CountryDAO.findByID(cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)))
            );

        } else {

            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    AreaDAO.findByID(cursor.getInt(cursor.getColumnIndex(AREA_ID)))
            );
        }
    }

    public static void create(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();
        values.put(ID, phone.getID());
        values.put(NUMBER, phone.getNumber());

        if (phone.getAreaID() == -1) {
            values.put(COUNTRY_ID, phone.getCountryID());
        } else {
            values.put(AREA_ID, phone.getAreaID());
        }

        final int id = (int) DAOHandler.getLocalDatabase().insert(TABLE, null, values);

        values.put(ID, id);
        DAOHandler.getLocalDatabase().insert(TABLE, null, values);
    }

    public static Phone findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) phone = generate(cursor);

        cursor.close();
        return phone;
    }

    public static void delete(@NonNull final int id) {
        DAOHandler.getLocalDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
        DAOHandler.getLocalDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static int exists(@NonNull final Phone phone) {
        final String number = String.valueOf(phone.getNumber());
        Cursor cursor;

        if (phone.getCountryID() == -1) {
            final String areaCode = String.valueOf(phone.getArea().getCode());

            cursor = DAOHandler.getLocalDatabase().query(TABLE, null,
                    NUMBER + " = ? AND " + AREA_ID + " = ?", new String[] {number, areaCode},
                    null, null, null);

        } else {

            final String countryId = String.valueOf(phone.getCountry().getID());

            cursor = DAOHandler.getLocalDatabase().query(TABLE, null,
                    NUMBER + " = ? AND " + COUNTRY_ID + " = ? ", new String[] {number, countryId},
                    null, null, null);
        }

        Phone matchingPhone = null;

        if (cursor.moveToFirst()) matchingPhone = generate(cursor);

        cursor.close();

        if (matchingPhone == null) return -1;
        else return matchingPhone.getID();
    }
}
