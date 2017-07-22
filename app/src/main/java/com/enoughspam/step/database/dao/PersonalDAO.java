package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.User;
import com.enoughspam.step.database.domains.abstracts.Domain;

/**
 * Created by Hugo
 * Date: 28/06/17
 * Time: 10:47
 */

public class PersonalDAO extends DAO {

    private static final String NAME = "name";
    private static final String ALLSET = "all_set";

    public PersonalDAO(@NonNull final Context context) {
        super(context, "personal");
    }

    @Override
    public User generate(@NonNull final Cursor cursor) {
        return new User(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(NAME))
        );
    }

    @Override
    public boolean create(@NonNull final Domain domain) {
        final User user = (User) domain;
        final ContentValues contentValues = new ContentValues();

        contentValues.put(ID, user.getId());
        contentValues.put(NAME, user.getName());

        return getSqLiteDatabase().insert(TABLE, null, contentValues) > 0;
    }

    public User get() {
        final Cursor cursor = getSqLiteDatabase().query(TABLE, null, null, null, null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }

     public boolean isAllSet() {
         final Cursor cursor = getSqLiteDatabase().query(
                 TABLE, new String[] {ALLSET}, null, null, null, null, null, null);

         int allSet = 0;
         if (cursor.moveToFirst()) allSet = cursor.getInt(cursor.getColumnIndex("all_set"));

         cursor.close();
         return allSet == 1 ? true : false;
     }

    public boolean setAllSet(final boolean allSet) {
        final ContentValues values = new ContentValues();
        values.put(ALLSET, allSet ? 1 : 0);

        return getSqLiteDatabase().update(TABLE, values, null, null) > 0;
    }
}
