package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Description;
import com.enoughspam.step.database.localDao.abstracts.GenericHybridDAO;

/**
 * Created by Hugo Castelani
 * Date: 22/07/17
 * Time: 11:55
 */

public class DescriptionDAO extends GenericHybridDAO<Description> {
    private static DescriptionDAO instance;

    public static final String DESCRIPTION = "description";
    public static final String TREATMENT_ID = "treatment_id";

    @Override
    protected void prepareFields() {
        table = "description";
        node = "descriptions";
        aClass = Description.class;
    }

    private DescriptionDAO() {}

    public static DescriptionDAO get() {
        if (instance == null) instance = new DescriptionDAO();
        return instance;
    }

    @Override
    public Description generate(@NonNull final Cursor cursor) {
        return new Description(
                cursor.getInt(cursor.getColumnIndex(id)),
                cursor.getString(cursor.getColumnIndex(DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(TREATMENT_ID))
        );
    }

    @Override
    public DescriptionDAO create(@NonNull final Description description) {
        ContentValues values = new ContentValues();

        values.put(id, description.getID());
        values.put(DESCRIPTION, description.getDescription());
        values.put(TREATMENT_ID, description.getTreatmentID());

        DAOHandler.getLocalDatabase().insert(table, null, values);
        return instance;
    }

    @Override
    public DescriptionDAO update(@NonNull final Description description) {
        final ContentValues values = new ContentValues();

        // For convenience, I'm gonna update only treatment id
        values.put(TREATMENT_ID, description.getTreatmentID());

        DAOHandler.getLocalDatabase().update(table, values,
                id + " = ?", new String[] {String.valueOf(description.getID())});
        return instance;
    }

    @Override
    public int exists(@NonNull Description description) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}
