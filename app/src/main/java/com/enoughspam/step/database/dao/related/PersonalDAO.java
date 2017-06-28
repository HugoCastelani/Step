package com.enoughspam.step.database.dao.related;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.domains.User;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 10:47
 */

public class PersonalDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public PersonalDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    private User generate(Cursor cursor) {
        return new User(
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("name"))
        );
    }

    public boolean delete(long id) {
        String sql = "delete from personal where id = " + id;

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public User get() {
        String sql = "select * from personal";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        User user = null;
        while (cursor.moveToNext())
            user = generate(cursor);

        cursor.close();
        return user;
    }
}
