package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.User;

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

    public UserDAO() {}

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

        return DAOHandler.getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    public static boolean delete(@NonNegative final int id) {
        return DAOHandler.getSqLiteDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)}) > 0;
    }

    public static int findIdByIdSocial(@NonNull final String socialId) {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(
                TABLE, new String[] {ID}, SOCIAL_ID + " = ?", new String[] {socialId},
                null, null, null);

        int id = -1;
        if (cursor.moveToFirst()) id = cursor.getInt(cursor.getColumnIndex(ID));

        cursor.close();
        return id;
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
