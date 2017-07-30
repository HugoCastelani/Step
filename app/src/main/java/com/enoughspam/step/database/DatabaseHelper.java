package com.enoughspam.step.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 24/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "enough_spam.db";
    public static final int VERSION = 37;

    private SQLiteDatabase mSqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mSqLiteDatabase = sqLiteDatabase;
        createTables();
        insertAttributes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    private void createTables() {
        // create personal table
        mSqLiteDatabase.execSQL("create table personal (" +
                "id integer primary key not null," +
                "name varchar(50) not null);");

        // RELATED TABLES

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

        // creating country table
        mSqLiteDatabase.execSQL("create table country (" +
                "id integer primary key not null," +
                "code integer not null," +
                "name varchar(50) not null," +
                "mask varchar(25) not null)");

        // creating state table
        mSqLiteDatabase.execSQL("create table state (" +
                "id integer primary key not null," +
                "name varchar(50) not null," +
                "country_id integer not null," +
                "foreign key(country_id) references country(id));");

        // creating area code table
        mSqLiteDatabase.execSQL("create table area (" +
                "code integer primary key not null," +
                "name varchar(50) not null," +
                "state_id integer not null," +
                "foreign key(state_id) references state(id));");

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

        // creating description table
        mSqLiteDatabase.execSQL("create table description (" +
                "id integer primary key not null," +
                "description varchar(50) not null," +
                "treatment_id integer not null," +
                "foreign key(treatment_id) references suspicious_treatment(id));");

        // creating denunciation/description table
        mSqLiteDatabase.execSQL("create table denunciation_description (" +
                "denunciation_id integer not null," +
                "description_id integer not null," +
                "primary key(denunciation_id, description_id)," +
                "foreign key(denunciation_id) references denunciation(id)," +
                "foreign key(description_id) references description(id));");

        // creating suspicious treatment table
        mSqLiteDatabase.execSQL("create table suspicious_treatment (" +
                "id integer primary key not null," +
                "treatment varchar(20) not null);");

        // creating treatment configuration table
        /*mSqLiteDatabase.execSQL("create table config_treatment (" +
                "description_id integer not null," +
                "suspicious_treatment_id integer not null," +
                "primary key(description_id, suspicious_treatment_id)," +
                "foreign key(description_id) references description(id)," +
                "foreign key(suspicious_treatment_id) references suspicious_treatment(id));");*/

        // NOT RELATED TABLES

        // creating feedback configuration table
        mSqLiteDatabase.execSQL("create table config_feedback (" +
                "call_kind_name varchar(20) primary key  not null," +
                "ask_feedback tinyint not null);");

        // creating service blocking configuration table
        /*mSqLiteDatabase.execSQL("create table config_service_block (" +
                "service_name varchar(20) primary key not null," +
                "block tinyint not null);");*/

        // creating guide configuration table
        mSqLiteDatabase.execSQL("create table config_guide (" +
                "view_name varchar(20) primary key not null," +
                "show_guide tinyint not null);");

        // creating network to download DB configuration table
        /*mSqLiteDatabase.execSQL("create table config_network_download_db (" +
                "network_name varchar(20) primary key not null," +
                "download_it tinyint not null);");*/

        // creating suspicious denunciations amount configuration table
        mSqLiteDatabase.execSQL("create table suspicious_denunciations_amount (" +
                "amount smallint primary key not null);");

        // creating theme configuration table
        /*mSqLiteDatabase.execSQL("create table config_theme (" +
                "is_dark tinyint primary key not null," +
                "accent_color char(7) not null," +
                "default_accent_color char(7) not null);");*/
    }

    private void insertAttributes() {
        // !!!!!!!!!!! NEVER ADD A NEW ATTRIBUTE ON TOP OF THE TABLE AFTER APP IS ON THE AIR!!!!!!!!!!!

        // inserting description attributes
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(0, 'Companhia bancária', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(1, 'Telemarketing', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(2, 'Oferta utópica', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(3, 'Oferta indesejada', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(4, 'Oferta repetitiva', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(5, 'Prisioneiro', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(6, 'Golpe', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(7, 'Chantagem', 0);");
        mSqLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(8, 'Questões pessoais', 0);");

        // inserting suspicious treatment attributes
        mSqLiteDatabase.execSQL("insert into suspicious_treatment(id, treatment) values(0, 'Silenciar');");
        mSqLiteDatabase.execSQL("insert into suspicious_treatment(id, treatment) values(1, 'Bloquear');");

        // inserting countries
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(1, 1, 'United States', '999 999 9999');");
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(2, 1, 'Canada', '999 999 9999');");
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(3, 1, 'República Dominicana', '999 999 9999');");
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(4, 1, 'Puerto Rico', '999 999 9999');");
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(5, 7, 'Россия', '999 999 9999');");   // Russia
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(6, 7, 'Қазақстан', '999 999 99 99');");   // Kazakhstan
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(7, 20, 'مَصر\u200E\u200E', '99 9999 9999');");    // Egypt
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(8, 39, 'Italia', '');");
        mSqLiteDatabase.execSQL("insert into country(id, code, name, mask) values(9, 55, 'Brasil', '99 99999 9999');");

        // inserting states
        mSqLiteDatabase.execSQL("insert into state(id, name, country_id) values(1, 'Minas Gerais', 9);");

        // inserting states
        mSqLiteDatabase.execSQL("insert into area(code, name, state_id) values(31, ' Região metropolitana de Belo Horizonte', 1);");
    }
}
