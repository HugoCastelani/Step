package com.enoughspam.step.database.dao.interfaces;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 21/07/17
 * Time: 18:29
 */

public interface IDAO<T> {
    SQLiteDatabase getSqLiteDatabase();
    void closeSqLiteDatabase();
    T generate(@NonNull final Cursor cursor);
    boolean create(@NonNull final T type);
    boolean delete(@NonNegative final int id);
    T findById(@NonNegative final int id);
    List<T> getList();
    List<String> getColumnList(@NonNull final String column);
}
