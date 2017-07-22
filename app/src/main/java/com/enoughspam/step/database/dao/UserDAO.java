package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.User;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:01
 */

public class UserDAO extends DAO {

    private static final String SOCIALID = "social_id";
    private static final String NAME = "name";

    public UserDAO(@NonNull Context context) {
        super(context, "user", "id");
    }

    @Override
    public User generate(@NonNull Cursor cursor) {
        return new User(
                super.generate(cursor).getId(),
                cursor.getString(cursor.getColumnIndex(SOCIALID)),
                cursor.getString(cursor.getColumnIndex(NAME))
        );
    }

    @Override
    public boolean create(@NonNull Domain domain) {
        User user = (User) domain;
        ContentValues values = new ContentValues();

        values.put(SOCIALID, user.getSocialId());
        values.put(NAME, user.getName());

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    public int findIdByIdSocial(@NonNull String socialId) {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, new String[] {ID}, SOCIALID + " = ?", new String[] {socialId},
                null, null, null);

        int id = -1;
        if (cursor.moveToFirst()) id = cursor.getInt(cursor.getColumnIndex(ID));

        cursor.close();
        return id;
    }

    @Override
    public User findById(@NonNegative int id) {
        return (User) super.findById(id);
    }

    public User findBySocialId(@NonNull String socialId) {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, SOCIALID + " = ?", new String[] {socialId},
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
