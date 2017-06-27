package com.enoughspam.step.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hugo on 24/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database.db";
    public static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // RELATED TABLES

        // create user table
        sqLiteDatabase.execSQL("create table user (\n" +
                "id integer primary key not null auto_increment,\n" +
                "name varchar(50) not null);");

        // create friendship table
        sqLiteDatabase.execSQL("create table friendship (\n" +
                "id integer primary key not null auto_increment,\n" +
                "idUserAdded integer not null,\n" +
                "idUserAdding integer not null,\n" +
                "foreign key(idUserAdded) references user(id),\n" +
                "foreign key(idUserAdding) references user(id));");

        // creating country table
        sqLiteDatabase.execSQL("create table country (\n" +
                "id integer primary key not null auto_increment,\n" +
                "name varchar(50) not null)");

        // creating country code table
        sqLiteDatabase.execSQL("create table country_code (\n" +
                "code varchar(10) primary key not null,\n" +
                "idCountry integer not null,\n" +
                "foreign key(idCountry) references country(id));");

        // creating state table
        sqLiteDatabase.execSQL("create table state (\n" +
                "id integer primary key not null auto_increment,\n" +
                "name varchar(50) not null,\n" +
                "idCountry integer not null,\n" +
                "foreign key(idCountry) references country(id));");

        // creating area code table
        sqLiteDatabase.execSQL("create table area_code (\n" +
                "code varchar(10) primary key not null,\n" +
                "name varchar(50) not null,\n" +
                "idState integer not null,\n" +
                "foreign key(idState) references state(id));");

        // creating phone table
        sqLiteDatabase.execSQL("create table phone (\n" +
                "id integer primary key not null auto_increment,\n" +
                "number varchar(20) not null,\n" +
                "areaCode varchar(10) not null,\n" +
                "idUser integer not null,\n" +
                "foreign key(areaCode) references area_code(code),\n" +
                "foreign key(idUser) references user(id));");

        // creating notification table
        sqLiteDatabase.execSQL("create table notification (\n" +
                "id integer primary key not null auto_increment,\n" +
                "idPhone integer not null,\n" +
                "idUserNotified integer not null,\n" +
                "foreign key(idPhone) references phone(id),\n" +
                "foreign key(idUserNotified) references user(id));");

        // creating denunciation table
        sqLiteDatabase.execSQL("create table denunciation (\n" +
                "id integer primary key not null auto_increment,\n" +
                "idPhone integer not null,\n" +
                "foreign key(idPhone) references phone(id));");

        // creating description table
        sqLiteDatabase.execSQL("create table description (\n" +
                "id integer primary key not null auto_increment,\n" +
                "description varchar(50) not null);");

        // creating denunciation/description table
        sqLiteDatabase.execSQL("create table denunciation_description (\n" +
                "idDenunciation integer not null,\n" +
                "idDescription integer not null,\n" +
                "primary key(idDenunciation, idDescription),\n" +
                "foreign key(idDenunciation) references denunciation(id),\n" +
                "foreign key(idDescription) references description(id));");

        // creating suspicious treatment table
        sqLiteDatabase.execSQL("create table suspicious_treatment (\n" +
                "id integer primary key not null auto_increment,\n" +
                "name varchar(20) not null);");

        // creating treatment configuration table
        sqLiteDatabase.execSQL("create table config_treatment (\n" +
                "idDescription integer not null,\n" +
                "idSuspiciousTreatment integer not null,\n" +
                "primary key(idDescription, idSuspiciousTreatment),\n" +
                "foreign key(idDescription) references description(id),\n" +
                "foreign key(idSuspiciousTreatment) references suspicious_treatment(id));");

        // NOT RELATED TABLES

        // creating feedback configuration table
        sqLiteDatabase.execSQL("create table config_feedback (\n" +
                "nmCallKind varchar(20) primary key  not null,\n" +
                "showFeedback integer not null);");

        // creating service blocking configuration table
        sqLiteDatabase.execSQL("create table config_service_block (\n" +
                "nmService varchar(20) primary key not null,\n" +
                "block integer not null);");

        // creating guide configuration table
        sqLiteDatabase.execSQL("create table config_guide (\n" +
                "nmView varchar(20) primary key not null,\n" +
                "showGuide integer not null);");

        // creating intro configuration table
        sqLiteDatabase.execSQL("create table config_intro (\n" +
                "showIntro integer primary key not null);");

        sqLiteDatabase.execSQL("insert into config_intro values(1);");

        // creating network to download DB configuration table
        sqLiteDatabase.execSQL("create table config_network_download_db (\n" +
                "nmNetwork varchar(20) primary key not null,\n" +
                "downloadIt integer not null);");

        // creating suspicious denunciations amount configuration table
        sqLiteDatabase.execSQL("create table suspicious_denunciations_amount (\n" +
                "amount integer primary key not null);");

        // creating theme configuration table
        sqLiteDatabase.execSQL("create table config_theme (\n" +
                "isDark integer primary key not null,\n" +
                "accentColor char(7) not null,\n" +
                "defaultAccentColor char(7) not null);");

        sqLiteDatabase.execSQL("insert into config_theme values(0, '#00BCD4', '#00BCD4');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
