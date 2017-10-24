package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.State;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 13:59
 */

public class StateDAO {
    public static final String TABLE = "state";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COUNTRY_ID = "country_id";

    public static final String NODE = "states";

    private StateDAO() {}

    public static State generate(@NonNull final Cursor cursor) {
        return new State(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                CountryDAO.findByID(cursor.getInt(cursor.getColumnIndex(COUNTRY_ID)))
        );
    }

    public static State findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        State state = null;

        if (cursor.moveToFirst()) state = generate(cursor);

        cursor.close();
        return state;
    }

    public static void sync(@NonNull final DAOHandler.AnswerListener listener) {
        final DatabaseReference database = DAOHandler.getFirebaseDatabase(NODE);
        final SQLiteDatabase localDatabase = DAOHandler.getLocalDatabase();

        Query query = database.orderByChild("id");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onAnswerRetrieved();
                Log.e("StateDAO", "Children addition finished");
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final State state = dataSnapshot.getValue(State.class);

                if (state.getID() != 0) {
                    if (findByID(state.getID()) == null) {
                        ContentValues values = new ContentValues();
                        values.put(ID, state.getID());
                        values.put(NAME, state.getName());
                        values.put(COUNTRY_ID, state.getCountryID());

                        localDatabase.insert(TABLE, null, values);
                        Log.e("StateDAO", "State was added");
                        return;
                    }
                }

                Log.e("StateDAO", "State was not added");
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
