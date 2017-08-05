package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;

import java.util.ArrayList;
import java.util.List;

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

    public static int create(@NonNull final UserPhone userPhone) {
        final ContentValues values = new ContentValues();

        int id;
        final int possibleId = PhoneDAO.exists(userPhone.getPhone());
        if (possibleId != -1) {
            id = possibleId;
        } else {
            id = PhoneDAO.create(userPhone.getPhone());
        }

        if (id > 0) {
            values.put(USER_ID, userPhone.getUser().getId());
            values.put(PHONE_ID, id);
            values.put(IS_PROPERTY, userPhone.isProperty());

            return (int) DAOHandler.getWideDatabase().insert(TABLE, null, values);
        }

        return id;
    }

    public static void delete(@NonNegative final int userId, @NonNegative final int phoneId) {
        DAOHandler.getWideDatabase().delete(TABLE,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {String.valueOf(userId), String.valueOf(phoneId)});
    }

    public static List<UserPhone> getUsersUserPhoneList(@NonNegative final int id) {
        Cursor cursor = DAOHandler.getWideDatabase().query(
            UserPhoneDAO.TABLE, null, UserPhoneDAO.USER_ID + " = ?",
            new String[] {String.valueOf(id)}, null, null, null);

        final List<UserPhone> userPhoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            userPhoneList.add(generate(cursor));
        }

        cursor.close();
        return userPhoneList;
    }

    /**
     * This method doesn't provide a list of UserPhone
     * object, but a list of phones blocked by the user
     */
    public static List<Phone> getUsersPhoneList(@NonNull final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(TABLE, null,
                USER_ID + " = ? AND " + IS_PROPERTY + " = ?",
                new String[] {String.valueOf(id), "0"},
                null, null, null);

        final List<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(PhoneDAO.findById(cursor.getInt(cursor.getColumnIndex(PHONE_ID))));
        }

        cursor.close();
        return phoneList;
    }

    public static boolean isAlreadyBlocked(@NonNull final UserPhone userPhone) {
        final String result = String.valueOf(PhoneDAO.exists(userPhone.getPhone()));

        if (!result.equals("-1")) {

            final Cursor cursor = DAOHandler.getWideDatabase().query(TABLE, null,
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
