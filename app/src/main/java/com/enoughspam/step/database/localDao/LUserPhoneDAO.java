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

            values.put(USER_ID, userPhone.getUserID());
            values.put(PHONE_ID, userPhone.getPhoneID());
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

    public static void delete(@NonNegative final int userID, @NonNegative final int phoneID) {
        DAOHandler.getLocalDatabase().delete(TABLE,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {String.valueOf(userID), String.valueOf(phoneID)});
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
        final String phoneID = String.valueOf(LPhoneDAO.exists(userPhone.getPhone()));

        if (!phoneID.equals("-1")) {

            List<User> userList = LFriendshipDAO.findUserFriends(userPhone.getUserID());
            userList.add(0, userPhone.getUser());

            for (int i = 0; i < userList.size(); i++) {
                final Cursor cursor = DAOHandler.getLocalDatabase().query(TABLE, null,
                        USER_ID + " = ? AND " + PHONE_ID + " = ?",
                        new String[] {String.valueOf(userList.get(i).getID()), phoneID},
                        null, null, null);

                UserPhone matchingUserPhone = null;

                if (cursor.moveToFirst()) matchingUserPhone = LUserPhoneDAO.generate(cursor);

                cursor.close();

                if (matchingUserPhone != null) return true;
            }
        }

        return false;
    }

    // this one can be fucking long
    public static void sync(@NonNull final DAOHandler.AnswerListener listener) {
        List<User> userList = LFriendshipDAO.findUserFriends(LUserDAO.getThisUser().getID());
        userList.add(0, LUserDAO.getThisUser());

        for (int i = 0; i < userList.size(); i++) {
            DAOHandler.AnswerListener answerListener = null;
            if (i == userList.size() - 1) {
                answerListener = () -> listener.onAnswerRetrieved();
            }

            UserPhoneDAO.getUserPhoneList(userList.get(i).getID(), new UserPhoneDAO.ListListener<UserPhone>() {
                @Override
                public void onItemAdded(@NonNull UserPhone userPhone) {
                    PhoneDAO.findByID(userPhone.getPhoneID(), LPhoneDAO::create);
                    create(userPhone);
                }

                @Override
                public void onItemRemoved(@NonNull UserPhone userPhone) {
                    LPhoneDAO.delete(userPhone.getPhoneID());
                }
            }, answerListener);
        }
    }
}
