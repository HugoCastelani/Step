package com.enoughspam.step.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.abstracts.GenericLocalDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.util.Listeners;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:25
 */

public class LUserPhoneDAO extends GenericLocalDAO<UserPhone> {
    private static LUserPhoneDAO instance;

    public static final String USER_KEY = "user_key";
    public static final String PHONE_KEY = "phone_key";
    public static final String IS_PROPERTY = "is_property";
    public static final String IS_NOTIFICATION = "is_notification";

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
                cursor.getString(cursor.getColumnIndex(USER_KEY)),
                cursor.getString(cursor.getColumnIndex(PHONE_KEY)),
                cursor.getInt(cursor.getColumnIndex(IS_PROPERTY)) == 1,
                cursor.getInt(cursor.getColumnIndex(IS_NOTIFICATION)) == 1
        );
    }

    @Override @Deprecated
    public LUserPhoneDAO create(@NonNull final UserPhone userPhone) {
        throw new UnsupportedOperationException("You shouldn't do this. Call overloaded method" +
                " with two parameters instead.");
    }

    public LUserPhoneDAO create(@NonNull final UserPhone userPhone,
                                @NonNull final Listeners.AnswerListener listener,
                                @NonNull final Boolean force) {

        if (force || findByColumn(PHONE_KEY, userPhone.getPhoneKey()) == null) {

            if (findByKeys(userPhone.getUserKey(), userPhone.getPhoneKey()) == null) {
                Listeners.AnswerListener innerListener = new Listeners.AnswerListener() {
                    Integer count = 0;

                    @Override
                    public void onAnswerRetrieved() {
                        if (++count == 2) {
                            final ContentValues values = new ContentValues();

                            values.put(USER_KEY, userPhone.getUserKey());
                            values.put(PHONE_KEY, userPhone.getPhoneKey());
                            values.put(IS_PROPERTY, userPhone.getIsProperty());
                            values.put(IS_NOTIFICATION, userPhone.getIsNotification());

                            DAOHandler.getLocalDatabase().insert(table, null, values);
                            listener.onAnswerRetrieved();
                        }
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                };

                LUserDAO.get().loadLocally(userPhone.getUserKey(), innerListener);
                LPhoneDAO.get().loadLocally(userPhone.getPhoneKey(), innerListener);
            }

        } else listener.onAnswerRetrieved();

        return instance;
    }

    @Override @Deprecated
    public LUserPhoneDAO update(@NonNull UserPhone userPhone) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public LUserPhoneDAO delete(@NonNegative final String key) {
        throw new UnsupportedOperationException("You shouldn't do this. Call overloaded method" +
                " with two parameters instead.");
    }

    public LUserPhoneDAO delete(@Nullable final String userKey, @Nullable final String phoneKey) {
        if (userKey == null) {
            DAOHandler.getLocalDatabase().delete(table, PHONE_KEY + " = ?", new String[] {phoneKey});

        } else if (phoneKey != null) {

            DAOHandler.getLocalDatabase().delete(table, USER_KEY + " = ?", new String[] {userKey});

        } else {

            DAOHandler.getLocalDatabase().delete(table,
                    USER_KEY + " = ? AND " + PHONE_KEY + " = ?",
                    new String[] {userKey, phoneKey});
        }

        return instance;
    }

    @Override @Deprecated
    public String exists(@NonNull UserPhone userPhone) {
        throw new UnsupportedOperationException("You shouldn't do this. Call findByKeys()" +
                " method instead.");
    }

    @Override
    public GenericLocalDAO<UserPhone> sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }

    public UserPhone findByKeys(@NonNull final String userKey, @NonNull final String phoneKey) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_KEY + " = ? AND " + PHONE_KEY + " = ?",
                new String[] {userKey, phoneKey}, null, null, null);

        UserPhone userPhone = null;
        if (cursor.moveToFirst()) userPhone = generate(cursor);

        return userPhone;
    }

    public ArrayList<Phone> getPhoneList(@NonNegative final String key) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, new String[] {PHONE_KEY},
                USER_KEY + " = ? AND " + IS_PROPERTY + " = ?",
                new String[] {key, "0"}, null, null, null);

        final ArrayList<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(LPhoneDAO.get().findByColumn(LPhoneDAO.key,
                    cursor.getString(cursor.getColumnIndex(PHONE_KEY)))
            );
        }

        cursor.close();
        return phoneList;
    }

    public Phone findThisUserPhone() {
        final String key = PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .getString("user_key", "-1");

        final String[] parameters = new String[] {PHONE_KEY};
        final String select = USER_KEY + " = ? AND " + IS_PROPERTY + " = ?";
        final String[] arguments = new String[] {key, "1"};

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, parameters, select, arguments, null, null, null);

        Phone phone = null;

        if (cursor.moveToFirst()) {
            phone = LPhoneDAO.get().findByColumn(LPhoneDAO.key,
                    cursor.getString(cursor.getColumnIndex(PHONE_KEY)));
        }

        cursor.close();
        return phone;
    }

    public boolean isBlocked(@NonNull final UserPhone userPhone) {
        final String phoneKey = LPhoneDAO.get().exists(userPhone.getPhone(null));

        if (!phoneKey.equals("-1")) {

            List<User> userList = LFriendshipDAO.get().findUserFriends(userPhone.getUserKey());
            userList.add(0, userPhone.getUser(null));

            for (int i = 0; i < userList.size(); i++) {
                final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                        USER_KEY + " = ? AND " + PHONE_KEY + " = ?",
                        new String[] {userList.get(i).getKey(), phoneKey},
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
