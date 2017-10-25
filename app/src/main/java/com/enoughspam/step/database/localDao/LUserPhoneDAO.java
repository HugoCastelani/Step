package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.wideDao.PhoneDAO;
import com.enoughspam.step.database.wideDao.UserDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:25
 */

public class LUserPhoneDAO {
    public static final String TABLE = "user_phone";
    public static final String USER_ID = "user_id";
    public static final String PHONE_ID = "phone_id";
    public static final String IS_PROPERTY = "is_property";

    private LUserPhoneDAO() {}

    public static UserPhone generate(@NonNull final Cursor cursor) {
        return new UserPhone(
                LUserDAO.findByID(cursor.getInt(cursor.getColumnIndex(USER_ID))),
                LPhoneDAO.findByID(cursor.getInt(cursor.getColumnIndex(PHONE_ID))),
                cursor.getInt(cursor.getColumnIndex(IS_PROPERTY)) == 1
        );
    }

    public static void create(@NonNull final UserPhone userPhone) {
        if (!exists(userPhone)) {
            final ContentValues values = new ContentValues();

            values.put(USER_ID, userPhone.getUser().getID());
            values.put(PHONE_ID, userPhone.getPhone().getID());
            values.put(IS_PROPERTY, userPhone.getIsProperty());

            DAOHandler.getLocalDatabase().insert(TABLE, null, values);
        }
    }

    private static boolean exists(@NonNull final UserPhone userPhone) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(TABLE,
                null, USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {String.valueOf(userPhone.getUserID()), String.valueOf(userPhone.getPhoneID())},
                null, null, null);

        if (cursor.moveToFirst()) return true;
        return false;
    }

    public static void delete(@NonNegative final int userId, @NonNegative final int phoneId) {
        DAOHandler.getLocalDatabase().delete(TABLE,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {String.valueOf(userId), String.valueOf(phoneId)});
    }

    public static List<Phone> getPhoneList(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, new String[] {PHONE_ID},
                USER_ID + " = ? AND " + IS_PROPERTY + " = ?",
                new String[] {String.valueOf(id), "0"}, null, null, null);

        final List<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(LPhoneDAO.findByID(
                    cursor.getInt(cursor.getColumnIndex(PHONE_ID)))
            );
        }

        cursor.close();
        return phoneList;
    }

    public static Phone findThisUserPhone() {
        final User user = LUserDAO.getThisUser();

        final String[] parameters = new String[] {PHONE_ID};
        final String select = USER_ID + " = ? AND " + IS_PROPERTY + " = ?";
        final String[] arguments = new String[] {String.valueOf(user.getID()), "1"};

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, parameters, select, arguments, null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) {
            phone = LPhoneDAO.findByID(cursor.getInt(cursor.getColumnIndex(PHONE_ID)));
        }

        cursor.close();
        return phone;
    }

    public static boolean isBlocked(@NonNull final UserPhone userPhone) {
        final String result = String.valueOf(LPhoneDAO.exists(userPhone.getPhone()));

        if (!result.equals("-1")) {

            final Cursor cursor = DAOHandler.getLocalDatabase().query(TABLE, null,
                    USER_ID + " = ? AND " + PHONE_ID + " = ?",
                    new String[] {String.valueOf(userPhone.getUser().getID()), result},
                    null, null, null);

            UserPhone matchingUserPhone = null;

            if (cursor.moveToFirst()) matchingUserPhone = LUserPhoneDAO.generate(cursor);

            cursor.close();

            if (matchingUserPhone == null) return false;
            else return true;

        } else return false;
    }

    // this one can be fucking long
    public static void sync(@NonNull final DAOHandler.AnswerListener listener) {
        List<User> friendList = LFriendshipDAO.findUserFriends(LUserDAO.getThisUser().getID());
        for (final User friend : friendList) {
            UserPhoneDAO.findUserByID(friend.getID(), retrievedUserPhone -> {
                PhoneDAO.findByID(retrievedUserPhone.getPhoneID(), retrievedPhone ->
                    LPhoneDAO.create(retrievedPhone)
                );
                UserDAO.findByID(retrievedUserPhone.getUserID(), retrievedUser ->
                    LUserDAO.create(retrievedUser)
                );
                LUserPhoneDAO.create(retrievedUserPhone);
            });
        }
        listener.onAnswerRetrieved();
    }
}
