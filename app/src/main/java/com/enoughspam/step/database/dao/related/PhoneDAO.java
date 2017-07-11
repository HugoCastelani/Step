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
    private PersonalDAO personalDAO;

    public PhoneDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
        personalDAO = new PersonalDAO(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    private Phone generate(Cursor cursor) {
        if (cursor.getInt(cursor.getColumnIndex("area_code")) == 0) {
            return new Phone(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getLong(cursor.getColumnIndex("number")),
                    cursor.getInt(cursor.getColumnIndex("area_code")),
                    personalDAO.get()
            );

        } else {

            return new Phone(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("country_id")),
                    cursor.getLong(cursor.getColumnIndex("number")),
                    personalDAO.get()
            );
        }

    }

    public boolean create(Phone phone) {
        String sql;

        if (phone.getAreaCode() == 0) {
            sql = "insert into phone(number, country_id, user_id) values(" +
                    "'" + phone.getNumber() + "', " +
                    " '" + phone.getCountryId() + "', " +
                    " " + phone.getUser().getId() +
                    ");";

        } else {

            sql = "insert into phone(number, area_code, user_id) values(" +
                    "'" + phone.getNumber() + "', " +
                    " '" + phone.getAreaCode() + "', " +
                    " " + phone.getUser().getId() +
                    ");";
        }

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public boolean delete(int id) {
        String sql = "delete from phone where id = " + id;

        try {
            getSqLiteDatabase().execSQL(sql);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Phone findById(int id) {
        String sql = "select * from phone where id = " + id;
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        Phone phone = null;
        while (cursor.moveToNext())
            phone = generate(cursor);

        cursor.close();
        return phone;
    }
}
