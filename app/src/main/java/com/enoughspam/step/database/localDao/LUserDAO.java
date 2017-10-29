package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.abstracts.GenericLocalDAO;

/**
 * Created by Hugo Castelani
 * Date: 03/08/17
 * Time: 22:30
 */

public class LUserDAO extends GenericLocalDAO<User> {
    private static LUserDAO instance;

    public static final String SOCIAL_ID = "social_id";
    public static final String USER_NAME = "user_name";
    public static final String PHOTO_URL = "photo_url";

    @Override
    protected void prepareFields() {
        table = "user";
    }

    private LUserDAO() {}

    public static LUserDAO get() {
        if (instance == null) instance = new LUserDAO();
        return instance;
    }

    @Override
    public User generate(@NonNull final Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(id)),
                cursor.getString(cursor.getColumnIndex(SOCIAL_ID)),
                cursor.getString(cursor.getColumnIndex(USER_NAME)),
                cursor.getString(cursor.getColumnIndex(PHOTO_URL))
        );
    }

    public LUserDAO create(@NonNull final User user) {
        final ContentValues values = new ContentValues();

        values.put(id, user.getID());
        values.put(SOCIAL_ID, user.getSocialID());
        values.put(USER_NAME, user.getUsername());
        values.put(PHOTO_URL, user.getPicURL());

        DAOHandler.getLocalDatabase().insert(table, null, values);

        PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .edit().putInt("user_id", user.getID()).apply();
        return instance;
    }

    @Override
    public GenericLocalDAO<User> update(@NonNull User user) {
        final ContentValues values = new ContentValues();

        values.put(id, user.getID());
        values.put(SOCIAL_ID, user.getSocialID());
        values.put(USER_NAME, user.getUsername());
        values.put(PHOTO_URL, user.getPicURL());

        DAOHandler.getLocalDatabase().update(
                table, values, id + " = ?", new String[] {String.valueOf(id)});

        return instance;
    }

    @Override
    public int exists(@NonNull User user) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    public LUserDAO clone(@NonNull final User user) {
        if (findByColumn(id, user.getID().toString()) == null) {
            final ContentValues values = new ContentValues();

            values.put(id, user.getID());
            values.put(SOCIAL_ID, user.getSocialID());
            values.put(USER_NAME, user.getUsername());
            values.put(PHOTO_URL, user.getPicURL());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }
        return instance;
    }

    public User getThisUser() {
        final int id = PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .getInt("user_id", 0);

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, null, instance.id + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        User user = null;

        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
