package com.enoughspam.step.database.wideDao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 17:26
 */

public class TreatmentDAO {

    public static final String TABLE = "suspicious_treatment";
    public static final String ID = "id";
    public static final String TREATMENT = "treatment";

    protected TreatmentDAO() {}

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
