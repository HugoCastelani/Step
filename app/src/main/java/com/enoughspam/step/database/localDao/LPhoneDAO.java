package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.localDao.abstracts.GenericLocalDAO;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:11
 */

public class LPhoneDAO extends GenericLocalDAO<Phone> {
    private static LPhoneDAO instance;

    public static final String NUMBER = "number";
    public static final String COUNTRY_ID = "country_id";
    public static final String AREA_ID = "area_id";

    @Override
    protected void prepareFields() {
        table = "phone";
    }

    private LPhoneDAO() {}

    public static LPhoneDAO get() {
        if (instance == null) instance = new LPhoneDAO();
        return instance;
    }

    @Override
    public Phone generate(@NonNull final Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex(AREA_ID)) == -1) {
            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(id)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    CountryDAO.get().findByColumn(CountryDAO.id,
                            cursor.getString(cursor.getColumnIndex(COUNTRY_ID)))
            );

        } else {

            return new Phone(
                    cursor.getInt(cursor.getColumnIndex(id)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    AreaDAO.get().findByColumn(AreaDAO.id,
                            String.valueOf(cursor.getInt(cursor.getColumnIndex(AREA_ID))))
            );
        }
    }

    @Override
    public LPhoneDAO create(@NonNull final Phone phone) {
        final ContentValues values = new ContentValues();

        values.put(id, phone.getID());
        values.put(NUMBER, phone.getNumber());

        if (phone.getAreaID() == -1) {
            values.put(COUNTRY_ID, phone.getCountryID());
        } else {
            values.put(AREA_ID, phone.getAreaID());
        }

        DAOHandler.getLocalDatabase().insert(table, null, values);
        return instance;
    }

    @Override
    public LPhoneDAO update(@NonNull Phone phone) {
        ContentValues values = new ContentValues();

        values.put(id, phone.getID());
        values.put(NUMBER, phone.getNumber());

        if (phone.getAreaID() == -1) {
            values.put(COUNTRY_ID, phone.getCountryID());
        } else {
            values.put(AREA_ID, phone.getAreaID());
        }

        DAOHandler.getLocalDatabase().update(
                table, values, id + " = ?", new String[] {String.valueOf(phone.getID())});
        return instance;
    }

    @Override
    public int exists(@NonNull final Phone phone) {
        final String number = String.valueOf(phone.getNumber());
        Cursor cursor;

        if (phone.getAreaID() == -1) {
            final String countryId = String.valueOf(phone.getCountry().getID());

            cursor = DAOHandler.getLocalDatabase().query(table, null,
                    NUMBER + " = ? AND " + COUNTRY_ID + " = ? ", new String[] {number, countryId},
                    null, null, null);

        } else {

            final String areaCode = String.valueOf(phone.getArea().getCode());

            cursor = DAOHandler.getLocalDatabase().query(table, null,
                    NUMBER + " = ? AND " + AREA_ID + " = ?", new String[] {number, areaCode},
                    null, null, null);
        }

        Phone matchingPhone = null;

        if (cursor.moveToFirst()) matchingPhone = generate(cursor);

        cursor.close();

        if (matchingPhone == null) return -1;
        else return matchingPhone.getID();
    }
}
