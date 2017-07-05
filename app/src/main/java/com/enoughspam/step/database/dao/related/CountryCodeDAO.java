package com.enoughspam.step.database.dao.related;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.domains.Country;
import com.enoughspam.step.database.domains.CountryCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/07/17
 * Time: 20:55
 */

public class CountryCodeDAO {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private CountryDAO countryDAO;

    public CountryCodeDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
        countryDAO = new CountryDAO(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }

        return sqLiteDatabase;
    }

    private CountryCode generate(Cursor cursor) {
        return new CountryCode(
                cursor.getInt(cursor.getColumnIndex("code")),
                countryDAO.findById(cursor.getLong(cursor.getColumnIndex("idCountry")))
        );
    }

    public Country findCountryByCode(int code) {
        String sql = "select * " +
                "from country c inner join country_code cc" +
                "on c.id = cc.idCountry " +
                "where cc.code = " + code;
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        Country country = null;
        while (cursor.moveToNext())
            country = countryDAO.generate(cursor);

        cursor.close();
        return country;
    }

    public List<CountryCode> getCountryCodeList() {
        String sql = "select * from country_code";
        Cursor cursor = getSqLiteDatabase().rawQuery(sql, null);

        List<CountryCode> countryCodes = new ArrayList<>();

        while (cursor.moveToNext()) {
            CountryCode countryCode = generate(cursor);
            countryCodes.add(countryCode);
        }

        cursor.close();
        return countryCodes;
    }
}
