package com.enoughspam.step.database.localDao.abstracts;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 28/10/17
 * Time: 16:55
 */

public abstract class GenericLocalDAO<T> {
    public String table;
    public static String id = "id";

    // must set table
    protected abstract void prepareFields();

    public GenericLocalDAO() {
        prepareFields();
    }

    protected abstract T generate(@NonNull final Cursor cursor);

    public abstract GenericLocalDAO<T> create(@NonNull final T t);

    public abstract GenericLocalDAO<T> update(@NonNull final T t);

    public GenericLocalDAO<T> delete(@NonNegative final Integer id) {
        DAOHandler.getLocalDatabase().delete(table, this.id + " = ?", new String[] {id.toString()});
        return this;
    }

    public abstract int exists(@NonNull final T t);

    public T findByColumn(@NonNull final String column, @NonNull final String value) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, null, column + " = ?", new String[] {value},
                null, null, null);

        T t = null;

        if (cursor.moveToFirst()) t = generate(cursor);

        cursor.close();
        return t;
    }

    public ArrayList<String> getColumnStringList(@NonNull final String column) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                table, new String[] {column}, null, null, null, null, null);

        final ArrayList<String> stringList = new ArrayList<>();

        while (cursor.moveToNext()) {
            stringList.add(cursor.getString(cursor.getColumnIndex(column)));
        }

        cursor.close();
        return stringList;
    }
}
