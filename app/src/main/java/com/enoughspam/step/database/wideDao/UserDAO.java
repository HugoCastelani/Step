package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:01
 */

public class UserDAO {

    public static final String TABLE = "user";
    public static final String ID = "id";
    public static final String SOCIAL_ID = "social_id";
    public static final String NAME = "name";

    private UserDAO() {}

    public static User generate(@NonNull final Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(SOCIAL_ID)),
                cursor.getString(cursor.getColumnIndex(NAME))
        );
    }

    public static int create(@NonNull final User user) {
        final ContentValues values = new ContentValues();
        int id = user.getId();

        values.put(SOCIAL_ID, user.getSocialId());
        values.put(NAME, user.getName());

        if (id == 0) {
            id = (int) DAOHandler.getWideDatabase().insert(TABLE, null, values);
        } else {
            values.put(ID, id);
        }

        if (id != -1) {
            values.put(ID, id);
            DAOHandler.getLocalDatabase().insert(TABLE, null, values);
        }

        PreferenceManager.getDefaultSharedPreferences(DAOHandler.getContext())
                .edit().putInt("user_id", id).apply();

        return id;
    }

    public static void delete(@NonNegative final int id) {
        DAOHandler.getWideDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
        DAOHandler.getLocalDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static User findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        User area = null;

        if (cursor.moveToFirst()) area = generate(cursor);

        cursor.close();
        return area;
    }

    public static User findBySocialId(@NonNull final String socialId) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                TABLE, null, SOCIAL_ID + " = ?", new String[] {socialId},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
