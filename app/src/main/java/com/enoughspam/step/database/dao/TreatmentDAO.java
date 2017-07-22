package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.abstracts.DAO;
import com.enoughspam.step.database.domains.Treatment;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 17:26
 */

public class TreatmentDAO extends DAO<Treatment> {

    public static final String TREATMENT = "treatment";

    public TreatmentDAO(@NonNull final Context context) {
        super(context, "suspicious_treatment");
    }

    @Override
    public Treatment generate(@NonNull final Cursor cursor) {
        return new Treatment(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(TREATMENT))
        );
    }

    @Override
    public boolean create(@NonNull final Treatment treatment) {
        final ContentValues values = new ContentValues();

        values.put(ID, treatment.getId());
        values.put(TREATMENT, treatment.getTreatment());

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }
}
