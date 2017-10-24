package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Area;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 19/10/17
 * Time: 15:10
 */

public class AreaDAO {
    public static final String TABLE = "area";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String STATE_ID = "state_id";

    public static final String NODE = "areas";

    private AreaDAO() {}

    public static Area generate(@NonNull final Cursor cursor) {
        return new Area(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                StateDAO.findByID(cursor.getInt(cursor.getColumnIndex(STATE_ID)))
        );
    }

    public static Area findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Area area = null;

        if (cursor.moveToFirst()) area = generate(cursor);

        cursor.close();
        return area;
    }

    public static Area findByCode(@NonNegative final int code) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, CODE + " = ?", new String[] {String.valueOf(code)},
                null, null, null);

        Area area = null;

        if (cursor.moveToFirst()) area = generate(cursor);

        cursor.close();
        return area;
    }

    public static void sync(@NonNull final DAOHandler.AnswerListener listener) {
        final DatabaseReference database = DAOHandler.getFirebaseDatabase(NODE);
        final SQLiteDatabase localDatabase = DAOHandler.getLocalDatabase();

        Query query = database.orderByChild("id");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onAnswerRetrieved();
                Log.e("AreaDAO", "Children addition finished");
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Area area = dataSnapshot.getValue(Area.class);

                if (area.getID() != 0) {
                    if (findByID(area.getID()) == null) {
                        ContentValues values = new ContentValues();
                        values.put(ID, area.getID());
                        values.put(CODE, area.getCode());
                        values.put(NAME, area.getName());
                        values.put(STATE_ID, area.getStateID());

                        localDatabase.insert(TABLE, null, values);
                        Log.e("AreaDAO", "Area was added");
                        return;
                    }
                }

                Log.e("AreaDAO", "Area was not added");
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
