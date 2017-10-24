package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Country;
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
 * Date: 19/10/17
 * Time: 16:59
 */

public class CountryDAO {
    public static final String TABLE = "country";
    public static final String ID = "id";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String ISO = "iso";
    public static final String MASK = "mask";

    public static final String NODE = "countries";

    private CountryDAO() {}

    public static Country generate(@NonNull final Cursor cursor) {
        return new Country(
                cursor.getInt(cursor.getColumnIndex(ID)),
                cursor.getInt(cursor.getColumnIndex(CODE)),
                cursor.getString(cursor.getColumnIndex(NAME)),
                cursor.getString(cursor.getColumnIndex(ISO)),
                cursor.getString(cursor.getColumnIndex(MASK))
        );
    }

    public static Country findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Country country = null;

        if (cursor.moveToFirst()) country = generate(cursor);

        cursor.close();
        return country;
    }

    public static Country findByCode(@NonNegative final int code) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, CODE + " = ?", new String[] {String.valueOf(code)},
                null, null, null);

        Country firstCountry = null;

        if (cursor.moveToFirst()) firstCountry = generate(cursor);

        cursor.close();
        return firstCountry;
    }

    public static Country findByName(@NonNull final String name) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, NAME + " = ?", new String[] {name},
                null, null, null);

        Country country = null;

        if (cursor.moveToFirst()) country = generate(cursor);

        cursor.close();
        return country;
    }

    public static Country findByISO(@NonNull final String iso) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, null, ISO + " = ?", new String[] {iso},
                null, null, null);

        Country country = null;

        if (cursor.moveToFirst()) country = generate(cursor);

        cursor.close();
        return country;
    }

    public static String findCodeByName(@NonNull final String name) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                TABLE, new String[] {CODE}, NAME + " = ?", new String[] {name},
                null, null, null);

        String code = null;

        if (cursor.moveToFirst()) code = cursor.getString(cursor.getColumnIndex(CODE));

        cursor.close();
        return code;
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
                Log.e("CountryDAO", "Children addition finished");
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Country country = dataSnapshot.getValue(Country.class);

                if (country.getID() != 0) {
                    if (findByID(country.getID()) == null) {
                        ContentValues values = new ContentValues();
                        values.put(ID, country.getID());
                        values.put(CODE, country.getCode());
                        values.put(NAME, country.getName());
                        values.put(ISO, country.getISO());
                        values.put(MASK, country.getMask());

                        localDatabase.insert(TABLE, null, values);
                        Log.e("CountryDAO", "Country was added");
                        return;
                    }
                }

                Log.e("CountryDAO", "Country was not added");
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
