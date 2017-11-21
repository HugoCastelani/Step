package com.enoughspam.step.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.intangible.GenericLocalDAO;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 03/08/17
 * Time: 22:30
 */

public final class LUserDAO extends GenericLocalDAO<User> {
    private static LUserDAO instance;

    public static final String SOCIAL_KEY = "social_key";
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
                cursor.getString(cursor.getColumnIndex(key)),
                cursor.getString(cursor.getColumnIndex(SOCIAL_KEY)),
                cursor.getString(cursor.getColumnIndex(USER_NAME)),
                cursor.getString(cursor.getColumnIndex(PHOTO_URL))
        );
    }

    @Override
    public LUserDAO create(@NonNull final User user) {
        if (findByColumn(SOCIAL_KEY, user.getSocialKey()) == null) {
            final ContentValues values = new ContentValues();

            values.put(key, user.getKey());
            values.put(SOCIAL_KEY, user.getSocialKey());
            values.put(USER_NAME, user.getUsername());
            values.put(PHOTO_URL, user.getPicURL());

            DAOHandler.getLocalDatabase().insert(table, null, values);

            PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                    .edit().putString("user_key", user.getKey()).apply();
        }

        return instance;
    }

    @Override
    public GenericLocalDAO<User> update(@NonNull User user) {
        final ContentValues values = new ContentValues();

        values.put(key, user.getKey());
        values.put(SOCIAL_KEY, user.getSocialKey());
        values.put(USER_NAME, user.getUsername());
        values.put(PHOTO_URL, user.getPicURL());

        DAOHandler.getLocalDatabase().update(
                table, values, key + " = ?", new String[] {String.valueOf(key)});

        return instance;
    }

    @Override
    public String exists(@NonNull User user) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public GenericLocalDAO<User> sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }

    public LUserDAO clone(@NonNull final User user) {
        if (findByColumn(key, user.getKey()) == null) {
            final ContentValues values = new ContentValues();

            values.put(key, user.getKey());
            values.put(SOCIAL_KEY, user.getSocialKey());
            values.put(USER_NAME, user.getUsername());
            values.put(PHOTO_URL, user.getPicURL());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }
        return instance;
    }

    public LUserDAO loadLocally(@NonNull final String userKey,
                                @NonNull final Listeners.AnswerListener listener) {
        if (findByColumn(LUserDAO.key, userKey) == null) {
            UserDAO.get().findByKey(userKey, new Listeners.ObjectListener<User>() {
                @Override
                public void onObjectRetrieved(@NonNull User retrievedUser) {
                    create(retrievedUser);
                    listener.onAnswerRetrieved();
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            });
        } else listener.onAnswerRetrieved();

        return instance;
    }

    public User getThisUser() {
        final String key = PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .getString("user_key", "-1");

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, null, instance.key + " = ?", new String[] {key}, null, null, null);

        User user = null;

        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }

    public String getThisUserKey() {
        return PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .getString("user_key", "-1");
    }
}
