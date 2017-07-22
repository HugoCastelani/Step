package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.User;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:01
 */

public class UserDAO extends DAO<User> {

    public static final String SOCIALID = "social_id";
    public static final String NAME = "name";

    public UserDAO(@NonNull final Context context) {
        super(context, "user");
    }

    @Override
    public User generate(@NonNull final Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(SOCIALID)),
                cursor.getString(cursor.getColumnIndex(NAME))
        );
    }

    @Override
    public boolean create(@NonNull final User user) {
        final ContentValues values = new ContentValues();

        values.put(SOCIALID, user.getSocialId());
        values.put(NAME, user.getName());

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    public int findIdByIdSocial(@NonNull final String socialId) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, new String[] {ID}, SOCIALID + " = ?", new String[] {socialId},
                null, null, null);

        int id = -1;
        if (cursor.moveToFirst()) id = cursor.getInt(cursor.getColumnIndex(ID));

        cursor.close();
        return id;
    }

    public User findBySocialId(@NonNull final String socialId) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, SOCIALID + " = ?", new String[] {socialId},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
