package com.enoughspam.step.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo Castelani
 * Date: 03/08/17
 * Time: 21:06
 *
 * This is the personal database. It clones dynamic tables of the
 * wide database and let them available to a faster experience
 */

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "local.db";
    public static final int VERSION = 1;

    private SQLiteDatabase mSqLiteDatabase;

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mSqLiteDatabase = sqLiteDatabase;
        createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

    private void createTables() {
        // create user table
        mSqLiteDatabase.execSQL("create table user (" +
                "id integer primary key not null," +
                "social_id varchar(30) not null, " +
                "name varchar(50) not null);");

        // create friendship table
        mSqLiteDatabase.execSQL("create table friendship (" +
                "id integer primary key not null," +
                "user_added_id integer not null," +
                "user_adding_id integer not null," +
                "foreign key(user_added_id) references user(id)," +
                "foreign key(user_adding_id) references personal(id));");

        // creating phone table
        mSqLiteDatabase.execSQL("create table phone (" +
                "id integer primary key not null," +
                "number bigint not null," +
                "country_id integer," +
                "area_code integer," +
                "foreign key(country_id) references country(id)," +
                "foreign key(area_code) references area(code));");

        mSqLiteDatabase.execSQL("create table user_phone (" +
                "user_id integer not null," +
                "phone_id integer not null," +
                "is_property tinyint not null," +
                "primary key(user_id, phone_id)," +
                "foreign key(user_id) references user(id)," +
                "foreign key(phone_id) references phone(id));");

        // creating notification table
        mSqLiteDatabase.execSQL("create table notification (" +
                "id integer primary key not null," +
                "phone_id integer not null," +
                "notified_user_id integer not null," +
                "notifying_user_id integer not null," +
                "foreign key(phone_id) references phone(id)," +
                "foreign key(notified_user_id) references user(id)," +
                "foreign key(notifying_user_id) references personal(id));");

        // creating denunciation table
        mSqLiteDatabase.execSQL("create table denunciation (" +
                "id integer primary key not null," +
                "phone_id integer not null," +
                "foreign key(phone_id) references phone(id));");

        // creating denunciation/description table
        mSqLiteDatabase.execSQL("create table denunciation_description (" +
                "denunciation_id integer not null," +
                "description_id integer not null," +
                "primary key(denunciation_id, description_id)," +
                "foreign key(denunciation_id) references denunciation(id)," +
                "foreign key(description_id) references description(id));");
    }
}
