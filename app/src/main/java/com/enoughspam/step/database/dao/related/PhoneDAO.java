package com.enoughspam.step.database.dao.related;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.domains.Phone;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 09:20
 */

public class PhoneDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private UserDAO userDAO;

    public PhoneDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
        userDAO = new UserDAO(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    private Phone generate(Cursor cursor) {
        return new Phone(
                cursor.getLong(cursor.getColumnIndex("id")),
                cursor.getString(cursor.getColumnIndex("number")),
                cursor.getString(cursor.getColumnIndex("areaCode")),
                userDAO.findById(cursor.getLong(cursor.getColumnIndex("idUser")))
        );
    }

    public boolean create(Phone phone) {
        String sql = "insert into user values(" +
                "" + phone.getId() + "," +
                " '" + phone.getNumber() + "'," +
                " '" + phone.getAreaCode() + "'," +
                " " + phone.getUser().getId() +
                ")";

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean delete(long id) {
        String sql = "delete from phone where id = " + id;

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Phone findById(long id) {
        String sql = "select * from phone where id = " + id;
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        Phone phone = null;
        while (cursor.moveToNext())
            phone = generate(cursor);

        cursor.close();
        return phone;
    }
}
