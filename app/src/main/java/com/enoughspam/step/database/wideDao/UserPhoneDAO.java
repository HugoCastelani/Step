package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
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
                cursor.getInt(cursor.getColumnIndex(IS_PROPERTY)) == 1
        );
    }

    public static int create(@NonNull final UserPhone userPhone) {
        final ContentValues values = new ContentValues();

        int phoneId;
        final int possiblePhoneId = PhoneDAO.exists(userPhone.getPhone());
        if (possiblePhoneId != -1) {
            phoneId = possiblePhoneId;
        } else {
            phoneId = PhoneDAO.create(userPhone.getPhone());
        }

        if (phoneId != -1) {
            values.put(USER_ID, userPhone.getUser().getId());
            values.put(PHONE_ID, phoneId);
            values.put(IS_PROPERTY, userPhone.isProperty());

            DAOHandler.getWideDatabase().insert(TABLE, null, values);
            return (int) DAOHandler.getLocalDatabase().insert(TABLE, null, values);
        }

        return phoneId;
    }

    public static void delete(@NonNegative final int userId, @NonNegative final int phoneId) {
        DAOHandler.getWideDatabase().delete(TABLE,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {String.valueOf(userId), String.valueOf(phoneId)});

        DAOHandler.getLocalDatabase().delete(TABLE,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {String.valueOf(userId), String.valueOf(phoneId)});
    }
}
