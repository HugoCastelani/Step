package com.hugocastelani.ivory.database;

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

public final class LocalDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "local.db";
    public static final int VERSION = 18;

    private SQLiteDatabase mSQLiteDatabase;

    public LocalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mSQLiteDatabase = sqLiteDatabase;
        createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

    private void createTables() {
        /**
         * Static tables
         * Tables' below values are read-only
         */

        // creating country table
        mSQLiteDatabase.execSQL("create table country (" +
                "key varchar(100) primary key not null," +
                "code integer not null," +
                "name varchar(50) not null," +
                "iso char(2) not null," +
                "mask varchar(25) not null)");

        // creating state table
        mSQLiteDatabase.execSQL("create table state (" +
                "key varchar(100) primary key not null," +
                "name varchar(50) not null," +
                "country_key varchar(100) not null," +
                "foreign key(country_key) references country(key));");

        // creating area code table
        mSQLiteDatabase.execSQL("create table area (" +
                "key varchar(100) primary key not null," +
                "code integer not null," +
                "name varchar(50) not null," +
                "state_key varchar(100) not null," +
                "foreign key(state_key) references state(key));");

        /**
         * Dynamic tables
         * Tables' below values are constantly modified
         */

        // create user table
        mSQLiteDatabase.execSQL("create table user (" +
                "key varchar(100) primary key not null," +
                "social_key varchar(50) not null, " +
                "user_name varchar(50) not null," +
                "photo_url varchar(500) not null);");

        // create friendship table
        mSQLiteDatabase.execSQL("create table relationship (" +
                "user_following_key varchar(100) not null," +
                "user_follower_key varchar(100) not null," +
                "foreign key(user_following_key) references user(key)," +
                "foreign key(user_follower_key) references user(key));");

        // creating phone table
        mSQLiteDatabase.execSQL("create table phone (" +
                "key varchar(100) primary key not null," +
                "number bigint not null," +
                "country_key varchar(100)," +
                "area_key varchar(100)," +
                "foreign key(country_key) references country(key)," +
                "foreign key(area_key) references area(key));");

        mSQLiteDatabase.execSQL("create table user_phone (" +
                "user_key varchar(100) not null," +
                "phone_key varchar(100) not null," +
                "is_property tinyint not null," +
                "is_notification tinyint not null," +
                "primary key(user_key, phone_key)," +
                "foreign key(user_key) references user(key)," +
                "foreign key(phone_key) references phone(key));");

        // creating notification table
        mSQLiteDatabase.execSQL("create table notification (" +
                "phone_key varchar(100) not null," +
                "notified_user_key varchar(100) not null," +
                "notifying_user_key varchar(100) not null," +
                "primary key(phone_key, notified_user_key, notifying_user_key)," +
                "foreign key(phone_key) references phone(key)," +
                "foreign key(notified_user_key) references user(key)," +
                "foreign key(notifying_user_key) references user(key));");
    }
}
