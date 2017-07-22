package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.Description;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 11:55
 */

public class DescriptionDAO extends DAO<Description> {

    public static final String DESCRIPTION = "description";

    public DescriptionDAO(@NonNull final Context context) {
        super(context, "description");
    }

    @Override
    public Description generate(@NonNull final Cursor cursor) {
        return new Description(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION))
        );
    }

    @Override
    public boolean create(@NonNull final Description description) {
        final ContentValues values = new ContentValues();

        values.put(ID, description.getId());
        values.put(DESCRIPTION, description.getDescription());

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }
}
