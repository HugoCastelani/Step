package com.hugocastelani.blockbook.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.dao.intangible.GenericLocalDAO;
import com.hugocastelani.blockbook.database.dao.wide.PhoneDAO;
import com.hugocastelani.blockbook.database.domain.Phone;
import com.hugocastelani.blockbook.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:11
 */

public final class LPhoneDAO extends GenericLocalDAO<Phone> {
    private static LPhoneDAO instance;

    public static final String NUMBER = "number";
    public static final String COUNTRY_KEY = "country_key";
    public static final String AREA_KEY = "area_key";

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
        if (cursor.getString(cursor.getColumnIndex(AREA_KEY)).equals("-1")) {
            return new Phone(
                    cursor.getString(cursor.getColumnIndex(key)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    "-1", cursor.getString(cursor.getColumnIndex(COUNTRY_KEY))
            );

        } else {

            return new Phone(
                    cursor.getString(cursor.getColumnIndex(key)),
                    cursor.getLong(cursor.getColumnIndex(NUMBER)),
                    cursor.getString(cursor.getColumnIndex(AREA_KEY)), "-1"
            );
        }
    }

    @Override
    public LPhoneDAO create(@NonNull final Phone phone) {
        if (exists(phone).equals("-1")) {
            final ContentValues values = new ContentValues();

            values.put(key, phone.getKey());
            values.put(NUMBER, phone.getNumber());
            values.put(AREA_KEY, phone.getAreaKey());
            values.put(COUNTRY_KEY, phone.getCountryKey());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }
        return instance;
    }

    @Override
    public LPhoneDAO update(@NonNull Phone phone) {
        ContentValues values = new ContentValues();

        values.put(key, phone.getKey());
        values.put(NUMBER, phone.getNumber());
        values.put(AREA_KEY, phone.getAreaKey());
        values.put(COUNTRY_KEY, phone.getCountryKey());

        DAOHandler.getLocalDatabase().update(
                table, values, key + " = ?", new String[] {phone.getKey()});
        return instance;
    }

    @Override
    public String exists(@NonNull final Phone phone) {
        final String number = String.valueOf(phone.getNumber());
        Cursor cursor;

        if (phone.getAreaKey().equals("-1")) {
            cursor = DAOHandler.getLocalDatabase().query(table, null,
                    NUMBER + " = ? AND " + COUNTRY_KEY + " = ? ",
                    new String[] {number, phone.getCountry().getKey()}, null, null, null);

        } else {

            cursor = DAOHandler.getLocalDatabase().query(table, null,
                    NUMBER + " = ? AND " + AREA_KEY + " = ?",
                    new String[] {number, phone.getArea().getKey()}, null, null, null);
        }

        Phone matchingPhone = null;

        if (cursor.moveToFirst()) matchingPhone = generate(cursor);

        cursor.close();

        if (matchingPhone == null) return "-1";
        else return matchingPhone.getKey();
    }

    public LPhoneDAO loadLocally(@NonNull final String phoneKey,
                                 @NonNull final Listeners.AnswerListener listener) {
        if (findByColumn(LPhoneDAO.key, phoneKey) == null) {
            PhoneDAO.get().findByKey(phoneKey, new Listeners.ObjectListener<Phone>() {
                @Override
                public void onObjectRetrieved(@NonNull Phone retrievedPhone) {
                    create(retrievedPhone);
                    listener.onAnswerRetrieved();
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            });
        } else listener.onAnswerRetrieved();

        return instance;
    }

    @Override
    public LPhoneDAO sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }
}
