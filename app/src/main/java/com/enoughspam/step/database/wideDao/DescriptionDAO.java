package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Description;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 11:55
 */

public class DescriptionDAO {

    public static final String TABLE = "description";
    public static final String ID = "id";
    public static final String DESCRIPTION = "description";
    public static final String TREATMENT_ID = "treatment_id";

    protected DescriptionDAO() {}

    public static Description generate(@NonNull final Cursor cursor) {
        return new Description(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(TREATMENT_ID))
        );
    }

    public static void update(@NonNull final Description description) {
        final ContentValues values = new ContentValues();

        // For convenience, I'm gonna update only treatment id
        values.put(TREATMENT_ID, description.getTreatmentId());

        DAOHandler.getWideDatabase().update(TABLE, values,
                ID + " = ?", new String[] {String.valueOf(description.getId())});
    }

    public static Description findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Description description = null;
        if (cursor.moveToFirst()) description = generate(cursor);

        cursor.close();
        return description;
    }

    public static List<String> getColumnList(@NonNull final String column) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                TABLE, new String[] {column}, null, null, null, null, null);

        final List<String> stringList = new ArrayList<>();

        while (cursor.moveToNext()) {
            stringList.add(cursor.getString(cursor.getColumnIndex(column)));
        }

        cursor.close();
        return stringList;
    }
}
