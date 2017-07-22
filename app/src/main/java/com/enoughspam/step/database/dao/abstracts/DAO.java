package com.enoughspam.step.database.dao.abstracts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.dao.interfaces.IDAO;
import com.enoughspam.step.database.domains.abstracts.Domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 18:34
 */

public abstract class DAO implements IDAO {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public String TABLE;
    public String ID;

    public DAO(@NonNull final Context context, @NonNull String TABLE, @NonNull String ID) {
        databaseHelper = new DatabaseHelper(context);
        this.TABLE = TABLE;
        this.ID = ID;
    }

    @Override
    public SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    @Override
    public Domain generate(@NonNull Cursor cursor) {
        return new Domain(cursor.getInt(cursor.getColumnIndex(ID)));
    }

    @Override
    public Domain findById(@NonNegative final int id) {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Domain domain = null;
        if (cursor.moveToFirst()) domain = generate(cursor);

        cursor.close();
        return domain;
    }

    @Override
    public boolean create(@NonNull Domain domain) {
        ContentValues values = new ContentValues();
        values.put(ID, domain.getId());

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }

    @Override
    public boolean delete(@NonNegative int id) {
        return getSqLiteDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)}) > 0;
    }

    @Override
    public List<Domain> getList() {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, null, null, null, null, null);

        List<Domain> domains = new ArrayList<>();

        while (cursor.moveToNext()) domains.add(generate(cursor));

        cursor.close();
        return domains;
    }
}
