package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;

/**
 * Created by Hugo Castelani
 * Date: 03/08/17
 * Time: 22:30
 */

public class LUserDAO {
    public static final String TABLE = "user";
    public static final String ID = "id";
    public static final String SOCIAL_ID = "social_id";
    public static final String USER_NAME = "user_name";
    public static final String PHOTO_URL = "photo_url";

    private LUserDAO() {}

    public static User generate(@NonNull final Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(SOCIAL_ID)),
                cursor.getString(cursor.getColumnIndex(USER_NAME)),
                cursor.getString(cursor.getColumnIndex(PHOTO_URL))
        );
    }

    public static void create(@NonNull final User user) {
        final ContentValues values = new ContentValues();

        values.put(ID, user.getID());
        values.put(SOCIAL_ID, user.getSocialID());
        values.put(USER_NAME, user.getUsername());
        values.put(PHOTO_URL, user.getPicURL());

        DAOHandler.getLocalDatabase().insert(TABLE, null, values);

        PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .edit().putInt("user_id", user.getID()).apply();
    }

    public static User findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        User user = null;

        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }

    public static void delete(@NonNegative final int id) {
        DAOHandler.getLocalDatabase().delete(TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static void clone(@NonNull final User user) {
        if (findByID(user.getID()) == null) {
            final ContentValues values = new ContentValues();

            values.put(ID, user.getID());
            values.put(SOCIAL_ID, user.getSocialID());
            values.put(USER_NAME, user.getUsername());
            values.put(PHOTO_URL, user.getPicURL());

            DAOHandler.getLocalDatabase().insert(TABLE, null, values);
        }
    }

    public static User getThisUser() {
        final int id = PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .getInt("user_id", 0);

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        User user = null;

        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
