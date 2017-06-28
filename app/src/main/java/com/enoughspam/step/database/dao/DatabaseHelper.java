package com.enoughspam.step.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hugo on 24/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "enough_spam.db";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // RELATED TABLES

        // create user table
        sqLiteDatabase.execSQL("create table user (" +
                "id integer primary key not null," +
                "name varchar(50) not null);");

        // create friendship table
        sqLiteDatabase.execSQL("create table friendship (" +
                "id integer primary key not null," +
                "idUserAdded integer not null," +
                "idUserAdding integer not null," +
                "foreign key(idUserAdded) references user(id)," +
                "foreign key(idUserAdding) references user(id));");

        // creating country table
        sqLiteDatabase.execSQL("create table country (" +
                "id integer primary key not null," +
                "name varchar(50) not null)");

        // creating country code table
        sqLiteDatabase.execSQL("create table country_code (" +
                "code varchar(10) primary key not null," +
                "idCountry integer not null," +
                "foreign key(idCountry) references country(id));");

        // creating state table
        sqLiteDatabase.execSQL("create table state (" +
                "id integer primary key not null," +
                "name varchar(50) not null," +
                "idCountry integer not null," +
                "foreign key(idCountry) references country(id));");

        // creating area code table
        sqLiteDatabase.execSQL("create table area_code (" +
                "code varchar(10) primary key not null," +
                "name varchar(50) not null," +
                "idState integer not null," +
                "foreign key(idState) references state(id));");

        // creating phone table
        sqLiteDatabase.execSQL("create table phone (" +
                "id integer primary key not null," +
                "number varchar(20) not null," +
                "areaCode varchar(10) not null," +
                "idUser integer not null," +
                "foreign key(areaCode) references area_code(code)," +
                "foreign key(idUser) references user(id));");

        // creating notification table
        sqLiteDatabase.execSQL("create table notification (" +
                "id integer primary key not null," +
                "idPhone integer not null," +
                "idUserNotified integer not null," +
                "foreign key(idPhone) references phone(id)," +
                "foreign key(idUserNotified) references user(id));");

        // creating denunciation table
        sqLiteDatabase.execSQL("create table denunciation (" +
                "id integer primary key not null," +
                "idPhone integer not null," +
                "foreign key(idPhone) references phone(id));");

        // creating description table
        sqLiteDatabase.execSQL("create table description (" +
                "id integer primary key not null," +
                "description varchar(50) not null);");

        // creating denunciation/description table
        sqLiteDatabase.execSQL("create table denunciation_description (" +
                "idDenunciation integer not null," +
                "idDescription integer not null," +
                "primary key(idDenunciation, idDescription)," +
                "foreign key(idDenunciation) references denunciation(id)," +
                "foreign key(idDescription) references description(id));");

        // creating suspicious treatment table
        sqLiteDatabase.execSQL("create table suspicious_treatment (" +
                "id integer primary key not null," +
                "name varchar(20) not null);");

        // creating treatment configuration table
        sqLiteDatabase.execSQL("create table config_treatment (" +
                "idDescription integer not null," +
                "idSuspiciousTreatment integer not null," +
                "primary key(idDescription, idSuspiciousTreatment)," +
                "foreign key(idDescription) references description(id)," +
                "foreign key(idSuspiciousTreatment) references suspicious_treatment(id));");

        // NOT RELATED TABLES

        // creating feedback configuration table
        sqLiteDatabase.execSQL("create table config_feedback (" +
                "nmCallKind varchar(20) primary key  not null," +
                "showFeedback integer not null);");

        // creating service blocking configuration table
        sqLiteDatabase.execSQL("create table config_service_block (" +
                "nmService varchar(20) primary key not null," +
                "block integer not null);");

        // creating guide configuration table
        sqLiteDatabase.execSQL("create table config_guide (" +
                "nmView varchar(20) primary key not null," +
                "showGuide integer not null);");

        // creating intro configuration table
        sqLiteDatabase.execSQL("create table config_intro (" +
                "showIntro integer primary key not null);");

        sqLiteDatabase.execSQL("insert into config_intro values(1);");

        // creating network to download DB configuration table
        sqLiteDatabase.execSQL("create table config_network_download_db (" +
                "nmNetwork varchar(20) primary key not null," +
                "downloadIt integer not null);");

        // creating suspicious denunciations amount configuration table
        sqLiteDatabase.execSQL("create table suspicious_denunciations_amount (" +
                "amount integer primary key not null);");

        // creating theme configuration table
        sqLiteDatabase.execSQL("create table config_theme (" +
                "isDark integer primary key not null," +
                "accentColor char(7) not null," +
                "defaultAccentColor char(7) not null);");

        sqLiteDatabase.execSQL("insert into config_theme values(0, '#00BCD4', '#00BCD4');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}
}
