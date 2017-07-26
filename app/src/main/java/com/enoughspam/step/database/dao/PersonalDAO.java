package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.User;

/**
 * Created by Hugo
 * Date: 28/06/17
 * Time: 10:47
 */

public class PersonalDAO {

    public static final String TABLE = "personal";
    public static final String ID = "id";
    public static final String NAME = "name";

    public PersonalDAO() {}

    public static User generate(@NonNull final Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(NAME))
        );
    }

    public static boolean create(@NonNull final User user) {
        final ContentValues values = new ContentValues();

        values.put(ID, user.getId());
        values.put(NAME, user.getName());

        return DAOHandler.getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    public static User get() {
        final Cursor cursor = DAOHandler.getSqLiteDatabase().query(TABLE, null, null, null, null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }
}
