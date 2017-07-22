package com.enoughspam.step.database.dao.interfaces;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domains.abstracts.Domain;

import java.util.List;

/**
 * Created by hugo
 * Date: 21/07/17
 * Time: 18:29
 */

public interface IDAO {
    SQLiteDatabase getSqLiteDatabase();
    Domain generate(@NonNull final Cursor cursor);
    boolean create(@NonNull final Domain domain);
    boolean delete(@NonNegative final int id);
    Domain findById(@NonNegative final int id);
    List<Domain> getList();
}
