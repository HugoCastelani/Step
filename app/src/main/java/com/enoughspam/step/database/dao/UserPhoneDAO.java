package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.UserPhone;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:30
 */

public class UserPhoneDAO {

    public static final String TABLE = "user_phone";
    public static final String USER_ID = "user_id";
    public static final String PHONE_ID = "phone_id";
    public static final String IS_PROPERTY = "is_property";

    private UserPhoneDAO() {}

    public static UserPhone generate(@NonNull final Cursor cursor) {
        return new UserPhone(
                UserDAO.findById(cursor.getInt(cursor.getColumnIndex(USER_ID))),
                PhoneDAO.findById(cursor.getInt(cursor.getColumnIndex(PHONE_ID))),
                cursor.getInt(cursor.getColumnIndex(IS_PROPERTY)) == 0 ? false : true
        );
    }

    public static boolean create(@NonNull final UserPhone userPhone) {
        final ContentValues values = new ContentValues();
        final long id = PhoneDAO.create(userPhone.getPhone());

        if (id > 0) {
            values.put(USER_ID, userPhone.getUser().getId());
            values.put(PHONE_ID, id);
            values.put(IS_PROPERTY, userPhone.isProperty());

            return DAOHandler.getSqLiteDatabase().insert(TABLE, null, values) > 0;

        } else {

            return false;
        }
    }

    public static boolean isAlreadyBlocked(@NonNull final UserPhone userPhone) {
        final String result = String.valueOf(PhoneDAO.exists(userPhone.getPhone()));

        if (!result.equals("-1")) {

            final Cursor cursor = DAOHandler.getSqLiteDatabase().query(TABLE, null,
                    USER_ID + " = ? AND " + PHONE_ID + " = ?",
                    new String[] {String.valueOf(userPhone.getUser().getId()), result},
                    null, null, null);

            UserPhone matchingUserPhone = null;

            if (cursor.moveToFirst()) matchingUserPhone = generate(cursor);

            cursor.close();

            if (matchingUserPhone == null) return false;
            else return true;

        } else return false;
    }
}