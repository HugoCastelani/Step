package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.abstracts.GenericLocalDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:25
 */

public class LUserPhoneDAO extends GenericLocalDAO<UserPhone> {
    private static LUserPhoneDAO instance;

    public static final String USER_ID = "user_id";
    public static final String PHONE_ID = "phone_id";
    public static final String IS_PROPERTY = "is_property";

    @Override
    protected void prepareFields() {
        table = "user_phone";
    }

    private LUserPhoneDAO() {}

    public static LUserPhoneDAO get() {
        if (instance == null) instance = new LUserPhoneDAO();
        return instance;
    }

    @Override
    public UserPhone generate(@NonNull final Cursor cursor) {
        return new UserPhone(
                LUserDAO.get().findByColumn(LUserDAO.id, cursor.getString(cursor.getColumnIndex(USER_ID))),
                LPhoneDAO.get().findByColumn(LPhoneDAO.id, cursor.getString(cursor.getColumnIndex(PHONE_ID))),
                cursor.getInt(cursor.getColumnIndex(IS_PROPERTY)) == 1
        );
    }

    @Override
    public LUserPhoneDAO create(@NonNull final UserPhone userPhone) {
        if (findByIDs(userPhone.getUserID(), userPhone.getPhoneID()) == null) {

            final ContentValues values = new ContentValues();

            values.put(USER_ID, userPhone.getUserID());
            values.put(PHONE_ID, userPhone.getPhoneID());
            values.put(IS_PROPERTY, userPhone.getIsProperty());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }

        return instance;
    }

    @Override @Deprecated
    public LUserPhoneDAO update(@NonNull UserPhone userPhone) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public LUserPhoneDAO delete(@NonNegative final Integer id) {
        throw new UnsupportedOperationException("You shouldn't do this. Call overloaded method" +
                " with two parameters instead.");
    }

    public LUserPhoneDAO delete(@NonNegative final Integer userID, @NonNegative final Integer phoneID) {
        DAOHandler.getLocalDatabase().delete(table,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {userID.toString(), phoneID.toString()});
        return instance;
    }

    @Override @Deprecated
    public int exists(@NonNull UserPhone userPhone) {
        throw new UnsupportedOperationException("You shouldn't do this. Call findByIDs()" +
                " method instead.");
    }

    public UserPhone findByIDs(@NonNegative final Integer userID, @NonNegative final Integer phoneID) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_ID + " = ? AND " + PHONE_ID + " = ?",
                new String[] {userID.toString(), phoneID.toString()}, null, null, null);

        UserPhone userPhone = null;
        if (cursor.moveToFirst()) userPhone = generate(cursor);

        return userPhone;
    }

    public ArrayList<Phone> getPhoneList(@NonNegative final Integer id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, new String[] {PHONE_ID},
                USER_ID + " = ? AND " + IS_PROPERTY + " = ?",
                new String[] {id.toString(), "0"}, null, null, null);

        final ArrayList<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(LPhoneDAO.get().findByColumn(LPhoneDAO.id,
                    cursor.getString(cursor.getColumnIndex(PHONE_ID)))
            );
        }

        cursor.close();
        return phoneList;
    }

    public Phone findThisUserPhone() {
        final User user = LUserDAO.get().getThisUser();

        final String[] parameters = new String[] {PHONE_ID};
        final String select = USER_ID + " = ? AND " + IS_PROPERTY + " = ?";
        final String[] arguments = new String[] {String.valueOf(user.getID()), "1"};

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, parameters, select, arguments, null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) {
            phone = LPhoneDAO.get().findByColumn(LPhoneDAO.id,
                    cursor.getString(cursor.getColumnIndex(PHONE_ID)));
        }

        cursor.close();
        return phone;
    }

    public boolean isBlocked(@NonNull final UserPhone userPhone) {
        final String phoneID = String.valueOf(LPhoneDAO.get().exists(userPhone.getPhone()));

        if (!phoneID.equals("-1")) {

            List<User> userList = LFriendshipDAO.get().findUserFriends(userPhone.getUserID());
            userList.add(0, userPhone.getUser());

            for (int i = 0; i < userList.size(); i++) {
                final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                        USER_ID + " = ? AND " + PHONE_ID + " = ?",
                        new String[] {String.valueOf(userList.get(i).getID()), phoneID},
                        null, null, null);

                UserPhone matchingUserPhone = null;

                if (cursor.moveToFirst()) matchingUserPhone = LUserPhoneDAO.get().generate(cursor);

                cursor.close();

                if (matchingUserPhone != null) return true;
            }
        }

        return false;
    }
}
