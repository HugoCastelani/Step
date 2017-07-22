package com.enoughspam.step.database.dao.abstracts;

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

    public static final String ID = "id";
    public String TABLE;

    public DAO(@NonNull final Context context, @NonNull final String TABLE) {
        databaseHelper = new DatabaseHelper(context);
        this.TABLE = TABLE;
    }

    @Override
    public SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    @Override
    public void closeSqLiteDatabase() {
        getSqLiteDatabase().close();
    }

    @Override
    public abstract Domain generate(@NonNull final Cursor cursor);

    @Override
    public abstract boolean create(@NonNull final Domain domain);

    @Override
    public boolean delete(@NonNegative final int id) {
        return getSqLiteDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)}) > 0;
    }

    @Override
    public Domain findById(@NonNegative final int id) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Domain domain = null;
        if (cursor.moveToFirst()) domain = generate(cursor);

        cursor.close();
        return domain;
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
