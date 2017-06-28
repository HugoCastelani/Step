package com.enoughspam.step.database.dao.related;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.domains.User;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:01
 */

public class UserDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public UserDAO(Context context) {
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

    public boolean create(User user) {
        String sql = "insert into user values(" + user.getId() + ", '" + user.getName() + "')";

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean delete(long id) {
        String sql = "delete from user where id = " + id;

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public User findById(long id) {
        String sql = "select * from user where id = " + id;
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        User user = null;
        while (cursor.moveToNext())
            user = generate(cursor);

        cursor.close();
        return user;
    }
}
