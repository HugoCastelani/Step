package com.enoughspam.step.database.dao.abstracts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DatabaseHelper;
import com.enoughspam.step.database.dao.interfaces.ICompoundDAO;
import com.enoughspam.step.database.dao.interfaces.IDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 20:02
 */

public abstract class CompoundDAO<T> implements IDAO<T>, ICompoundDAO<T> {

    /**
     * Theoretically, IDS and TABLE are both static variables because they belong
     * to the class that calls {@link #DAO}, but they're used in this class, so we
     * can't declare them as static. That's why their get and set methods are missing
     * as well.
     */
    public final String[] IDS;
    public final String TABLE;

    private final SQLiteDatabase sqLiteDatabase;

    public CompoundDAO(@NonNull final Context context, @NonNull final String[] IDS,
                       @NonNull final String TABLE) {
        sqLiteDatabase = new DatabaseHelper(context).getWritableDatabase();
        this.IDS = IDS;
        this.TABLE = TABLE;
    }

    @Override
    public SQLiteDatabase getSqLiteDatabase() {
        return sqLiteDatabase;
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
    public boolean delete(@NonNegative final int id) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("You should use pass a int[] instead.");
    }

    @Override
    public boolean delete(@NonNull @NonNegative final int[] ids) {
        return getSqLiteDatabase().delete(
                TABLE, IDS[0] + " = ? AND " + IDS[1] + " = ?",
                new String[] {String.valueOf(ids[0]), String.valueOf(ids[1])})
                > 0;
    }

    @Override
    public T findById(@NonNegative int id) {
        throw new UnsupportedOperationException("You should use pass a int[] instead.");
    }

    @Override
    public T findByIds(@NonNull @NonNegative int[] ids) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, null, IDS[0] + " = ? AND " + IDS[1] + " = ?",
                new String[] {String.valueOf(ids[0]), String.valueOf(ids[1])},
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
    public List<String> getColumnList(@NonNull String column) {
        final Cursor cursor = getSqLiteDatabase().query(
                TABLE, new String[] {column}, null, null, null, null, null);

        final List<String> countries = new ArrayList<>();

        while (cursor.moveToNext())
            countries.add(cursor.getString(cursor.getColumnIndex(column)));

        cursor.close();
        return countries;
    }
}
