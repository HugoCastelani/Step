package com.enoughspam.step.database.dao.abstracts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.dao.interfaces.IDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 18:34
 */

public abstract class DAO<T> implements IDAO<T> {

    /**
     * {@value #TABLE} theoretically is a static variable because it belongs
     * to the class that calls {@link #DAO}, but it's used in this class, so
     * we can't declare it as static. That's why its get and set methods are
     * missing as well.
     */
    public static final String ID = "id";
    public final String TABLE;

    private final SQLiteDatabase mSqLiteDatabase;

    public DAO(@NonNull final Context context, @NonNull final String TABLE) {
        mSqLiteDatabase = new DatabaseHelper(context).getWritableDatabase();
        this.TABLE = TABLE;
    }

    @Override
    public SQLiteDatabase getSqLiteDatabase() {
        return mSqLiteDatabase;
    }

    @Override
    public void closeSqLiteDatabase() {
        getSqLiteDatabase().close();
    }

    @Override
    public abstract T generate(@NonNull final Cursor cursor);

    @Override
    public abstract boolean create(@NonNull final T type);

    @Override
    public abstract boolean update(@NonNull final T type);

    @Override
    public boolean delete(@NonNegative final int id) {
        return getSqLiteDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)}) > 0;
    }

    @Override
    public T findById(@NonNegative final int id) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        T t = null;
        if (cursor.moveToFirst()) t = generate(cursor);

        cursor.close();
        return t;
    }

    @Override
    public List<T> getList() {
        Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, null, null, null, null, null);

        List<T> ts = new ArrayList<>();

        while (cursor.moveToNext()) ts.add(generate(cursor));

        cursor.close();
        return ts;
    }

    @Override
    public List<String> getColumnList(@NonNull final String column) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, new String[] {column}, null, null, null, null, null);

        final List<String> countries = new ArrayList<>();

        while (cursor.moveToNext())
            countries.add(cursor.getString(cursor.getColumnIndex(column)));

        cursor.close();
        return countries;
    }
}
