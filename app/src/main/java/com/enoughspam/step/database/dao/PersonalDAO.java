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

    public PersonalDAO(@NonNull Context context) {
        super(context, "personal", "id");
    }

    @Override
    public User generate(@NonNull Cursor cursor) {
        return new User(
                super.generate(cursor).getId(),
                cursor.getString(cursor.getColumnIndex(NAME))
        );
    }

    @Override
    public boolean create(@NonNull Domain domain) {
        User user = (User) domain;
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, user.getId());
        contentValues.put(NAME, user.getName());

        return getSqLiteDatabase().insert(TABLE, null, contentValues) > 0;
    }

    public User get() {
        Cursor cursor = getSqLiteDatabase().query(TABLE, null, null, null, null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = generate(cursor);

        cursor.close();
        return user;
    }

     public boolean isAllSet() {
         Cursor cursor = getSqLiteDatabase().query(
                 TABLE, new String[] {ALLSET}, null, null, null, null, null, null);

         int allSet = 0;
         if (cursor.moveToFirst()) allSet = cursor.getInt(cursor.getColumnIndex("all_set"));

         cursor.close();
         return allSet == 1 ? true : false;
     }

    public boolean setAllSet(boolean allSet) {
        ContentValues values = new ContentValues();
        values.put(ALLSET, allSet ? 1 : 0);

        return getSqLiteDatabase().update(TABLE, values, null, null) > 0;
    }
}
