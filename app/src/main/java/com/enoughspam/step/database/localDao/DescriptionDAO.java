package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Description;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    public static final String NODE = "descriptions";

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
        values.put(TREATMENT_ID, description.getTreatmentID());

        DAOHandler.getLocalDatabase().update(TABLE, values,
                ID + " = ?", new String[] {String.valueOf(description.getID())});
    }

    public static Description findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Description description = null;
        if (cursor.moveToFirst()) description = generate(cursor);

        cursor.close();
        return description;
    }

    public static List<String> getColumnList(@NonNull final String column) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, new String[] {column}, null, null, null, null, null);

        final List<String> stringList = new ArrayList<>();

        while (cursor.moveToNext()) {
            stringList.add(cursor.getString(cursor.getColumnIndex(column)));
        }

        cursor.close();
        return stringList;
    }

    public static void sync(@NonNull final DAOHandler.AnswerListener listener) {
        final DatabaseReference database = DAOHandler.getFirebaseDatabase(NODE);
        final SQLiteDatabase localDatabase = DAOHandler.getLocalDatabase();

        Query query = database.orderByChild("id");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onAnswerRetrieved();
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Description description = dataSnapshot.getValue(Description.class);

                if (description.getID() != 0) {
                    if (findByID(description.getID()) == null) {
                        ContentValues values = new ContentValues();
                        values.put(ID, description.getID());
                        values.put(DESCRIPTION, description.getDescription());
                        values.put(TREATMENT_ID, description.getTreatmentID());

                        localDatabase.insert(TABLE, null, values);
                    }
                }
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
