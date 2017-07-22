package com.enoughspam.step.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.abstracts.CompoundDAO;
import com.enoughspam.step.database.domains.ConfigTreatment;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 19:54
 */

public class ConfigTreatmentDAO extends CompoundDAO<ConfigTreatment> {

    public ConfigTreatmentDAO(@NonNull final Context context) {
        super(context, new String[] {"description_id", "suspicious_treatment_id"}, "config_treatment");
    }

    @Override
    public ConfigTreatment generate(@NonNull final Cursor cursor) {
        return new ConfigTreatment(
                new int[] {
                        cursor.getInt(cursor.getColumnIndex(IDS[0])),
                        cursor.getInt(cursor.getColumnIndex(IDS[1]))
                }
        );
    }

    @Override
    public boolean create(@NonNull final ConfigTreatment configTreatment) {
        ContentValues values = new ContentValues();

        values.put(IDS[0], configTreatment.getIds()[0]);
        values.put(IDS[1], configTreatment.getIds()[1]);

        return getSqLiteDatabase().insert(TABLE, null, values) > 0;
    }
}
