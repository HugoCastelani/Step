package com.enoughspam.step.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 24/05/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "enough_spam.db";
    public static final int VERSION = 29;

    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
        createTables();
        insertAttributes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

    private void createTables() {
        // create personal table
        sqLiteDatabase.execSQL("create table personal (" +
                "id integer primary key not null," +
                "name varchar(50) not null," +
                "all_set tinyint);");

        // RELATED TABLES

        // create user table
        sqLiteDatabase.execSQL("create table user (" +
                "id integer primary key not null," +
                "social_id varchar(30) not null, " +
                "name varchar(50) not null);");

        // create friendship table
        sqLiteDatabase.execSQL("create table friendship (" +
                "id integer primary key not null," +
                "user_added_id integer not null," +
                "user_adding_id integer not null," +
                "foreign key(user_added_id) references user(id)," +
                "foreign key(user_adding_id) references personal(id));");

        // creating country table
        sqLiteDatabase.execSQL("create table country (" +
                "id integer primary key not null," +
                "code integer not null," +
                "name varchar(50) not null," +
                "mask varchar(25) not null)");

        // creating state table
        sqLiteDatabase.execSQL("create table state (" +
                "id integer primary key not null," +
                "name varchar(50) not null," +
                "country_id integer not null," +
                "foreign key(country_id) references country(id));");

        // creating area code table
        sqLiteDatabase.execSQL("create table area (" +
                "code integer primary key not null," +
                "name varchar(50) not null," +
                "state_id integer not null," +
                "foreign key(state_id) references state(id));");

        // creating phone table
        sqLiteDatabase.execSQL("create table phone (" +
                "id integer primary key not null," +
                "number bigint not null," +
                "country_id integer," +
                "area_code integer," +
                "user_id integer," +
                "foreign key(country_id) references country(id)," +
                "foreign key(area_code) references area(code)," +
                "foreign key(user_id) references personal(id));");

        // creating notification table
        sqLiteDatabase.execSQL("create table notification (" +
                "id integer primary key not null," +
                "phone_id integer not null," +
                "notified_user_id integer not null," +
                "notifying_user_id integer not null," +
                "foreign key(phone_id) references phone(id)," +
                "foreign key(notified_user_id) references user(id)," +
                "foreign key(notifying_user_id) references personal(id));");

        // creating denunciation table
        sqLiteDatabase.execSQL("create table denunciation (" +
                "id integer primary key not null," +
                "phone_id integer not null," +
                "foreign key(phone_id) references phone(id));");

        // creating description table
        sqLiteDatabase.execSQL("create table description (" +
                "id integer primary key not null," +
                "description varchar(50) not null);");

        // creating denunciation/description table
        sqLiteDatabase.execSQL("create table denunciation_description (" +
                "denunciation_id integer not null," +
                "description_id integer not null," +
                "primary key(denunciation_id, description_id)," +
                "foreign key(denunciation_id) references denunciation(id)," +
                "foreign key(description_id) references description(id));");

        // creating suspicious treatment table
        sqLiteDatabase.execSQL("create table suspicious_treatment (" +
                "id integer primary key not null," +
                "treatment varchar(20) not null);");

        // creating treatment configuration table
        sqLiteDatabase.execSQL("create table config_treatment (" +
                "description_id integer not null," +
                "suspicious_treatment_id integer not null," +
                "primary key(description_id, suspicious_treatment_id)," +
                "foreign key(description_id) references description(id)," +
                "foreign key(suspicious_treatment_id) references suspicious_treatment(id));");

        // NOT RELATED TABLES

        // creating feedback configuration table
        sqLiteDatabase.execSQL("create table config_feedback (" +
                "call_kind_name varchar(20) primary key  not null," +
                "show_feedback tinyint not null);");

        // creating service blocking configuration table
        /*sqLiteDatabase.execSQL("create table config_service_block (" +
                "service_name varchar(20) primary key not null," +
                "block tinyint not null);");*/

        // creating guide configuration table
        sqLiteDatabase.execSQL("create table config_guide (" +
                "view_name varchar(20) primary key not null," +
                "show_guide tinyint not null);");

        // creating network to download DB configuration table
        /*sqLiteDatabase.execSQL("create table config_network_download_db (" +
                "network_name varchar(20) primary key not null," +
                "download_it tinyint not null);");*/

        // creating suspicious denunciations amount configuration table
        sqLiteDatabase.execSQL("create table suspicious_denunciations_amount (" +
                "amount smallint primary key not null);");

        // creating theme configuration table
        /*sqLiteDatabase.execSQL("create table config_theme (" +
                "is_dark tinyint primary key not null," +
                "accent_color char(7) not null," +
                "default_accent_color char(7) not null);");*/
    }

    private void insertAttributes() {
        // inserting description attributes
        sqLiteDatabase.execSQL("insert into description(description) values('Companhia bancária');");
        sqLiteDatabase.execSQL("insert into description(description) values('Telemarketing');");
        sqLiteDatabase.execSQL("insert into description(description) values('Oferta utópica');");
        sqLiteDatabase.execSQL("insert into description(description) values('Oferta indesejada');");
        sqLiteDatabase.execSQL("insert into description(description) values('Oferta repetitiva');");
        sqLiteDatabase.execSQL("insert into description(description) values('Prisioneiro');");
        sqLiteDatabase.execSQL("insert into description(description) values('Golpe');");
        sqLiteDatabase.execSQL("insert into description(description) values('Chantagem');");
        // !!!!!!!!!!! NEVER ADD A NEW DESCRIPTION ON TOP OF THE TABLE !!!!!!!!!!!

        // inserting suspicious treatment attributes
        sqLiteDatabase.execSQL("insert into suspicious_treatment(treatment) values('Silenciar');");
        sqLiteDatabase.execSQL("insert into suspicious_treatment(treatment) values('Bloquear');");
        // !!!!!!!!!!! NEVER ADD A NEW TREATMENT ON TOP OF THE TABLE !!!!!!!!!!!

        // inserting countries and its codes
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(1, 'United States', '999 999 9999');");
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(1, 'Canada', '999 999 9999');");
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(1, 'República Dominicana', '999 999 9999');");
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(1, 'Puerto Rico', '999 999 9999');");
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(7, 'Россия', '999 999 9999');");   // Russia
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(7, 'Қазақстан', '999 999 99 99');");   // Kazakhstan
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(20, 'مَصر\u200E\u200E', '99 9999 9999');");    // Egypt
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(39, 'Italia', '');");
        sqLiteDatabase.execSQL("insert into country(code, name, mask) values(55, 'Brasil', '99 99999 9999');");
    }
}
