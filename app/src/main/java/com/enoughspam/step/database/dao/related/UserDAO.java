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
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("idSocial")),
                cursor.getString(cursor.getColumnIndex("name"))
        );
    }

    public boolean create(User user) {
        String sql = "insert into user(idSocial, name) values('"
                + user.getIdSocial() + "', '"
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

    public long findIdByIdSocial(String idSocial) {
        String sql = "select * from user where idSocial = '" + idSocial + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        long id = -1;
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

    public User findByIdSocial(String idSocial) {
        String sql = "select * from user where idSocial = '" + idSocial + "'";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        User user = null;
        while (cursor.moveToNext())
            user = generate(cursor);

        cursor.close();
        return user;
    }
}
