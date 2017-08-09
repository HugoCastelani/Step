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
        if (findById(phone.getId()) == null) {
            final ContentValues values = new ContentValues();

            if (phone.getArea().getCode() == 0) {
                values.put(PhoneDAO.ID, phone.getId());
                values.put(PhoneDAO.NUMBER, phone.getNumber());
                values.put(PhoneDAO.COUNTRY_ID, phone.getCountry().getId());

            } else {

                values.put(PhoneDAO.ID, phone.getId());
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
}