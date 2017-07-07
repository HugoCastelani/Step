package com.enoughspam.step.database.dao.related;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
                cursor.getInt(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("social_id")),
                cursor.getString(cursor.getColumnIndex("name"))
        );
    }

    public boolean create(User user) {
        String sql = "insert into user(social_id, name) values('"
                + user.getSocialId() + "', '"
                + user.getName() + "')";

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            Log.e("UserDAO", e.getMessage());
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

    public long findIdByIdSocial(String socialId) {
        String sql = "select * from user where social_id = '" + socialId + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        long id = -1L;
        while (cursor.moveToNext())
            id = generate(cursor).getId();

        cursor.close();
        return id;
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

    public User findByIdSocial(String socialId) {
        String sql = "select * from user where social_id = '" + socialId + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        User user = null;
        while (cursor.moveToNext())
            user = generate(cursor);

        cursor.close();
        return user;
    }
}
