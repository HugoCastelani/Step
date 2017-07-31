package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
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

    public static boolean create(@NonNull final User user) {
        final ContentValues values = new ContentValues();

        values.put(SOCIAL_ID, user.getSocialId());
        values.put(NAME, user.getName());

        int id = (int) DAOHandler.getSqLiteDatabase().insert(TABLE, null, values);
        if (id != -1) {
            user.setId(id);
            PersonalDAO.create(user);
            return true;
        } else return false;
    }

    public static void delete(@NonNegative final int id) {
        DAOHandler.getSqLiteDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static User findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        User area = null;

        if (cursor.moveToFirst()) area = generate(cursor);

        cursor.close();
        return area;
    }

    public static User findBySocialId(@NonNull final String socialId) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, null, SOCIAL_ID + " = ?", new String[] {socialId},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
