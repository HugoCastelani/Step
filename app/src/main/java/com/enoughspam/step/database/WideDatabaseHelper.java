package com.enoughspam.step.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Hugo on 24/05/17.
 *
 * This is the globally shared database. It gets
 * updated periodically and stores all exiting data.
 */

public class WideDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wide.db";
    public static final int VERSION = 47;

    private SQLiteDatabase mSQLiteDatabase;

    public WideDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        mSQLiteDatabase = sqLiteDatabase;
        createTables();
        insertAttributes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

    private void createTables() {

        /**
         * Low access index tables
         * Tables' below values are infrequently modified
         */

        // creating suspicious treatment table
        mSQLiteDatabase.execSQL("create table suspicious_treatment (" +
                "id integer primary key not null," +
                "treatment varchar(20) not null);");

        /**
         * Static tables
         * Tables' below values are read-only
         */

        // creating country table
        mSQLiteDatabase.execSQL("create table country (" +
                "id integer primary key not null," +
                "code integer not null," +
                "name varchar(50) not null," +
                "iso char(2) not null," +
                "mask varchar(25) not null)");

        // creating state table
        mSQLiteDatabase.execSQL("create table state (" +
                "id integer primary key not null," +
                "name varchar(50) not null," +
                "country_id integer not null," +
                "foreign key(country_id) references country(id));");

        // creating area code table
        mSQLiteDatabase.execSQL("create table area (" +
                "code integer primary key not null," +
                "name varchar(50) not null," +
                "state_id integer not null," +
                "foreign key(state_id) references state(id));");

        // creating description table
        mSQLiteDatabase.execSQL("create table description (" +
                "id integer primary key not null," +
                "description varchar(50) not null," +
                "treatment_id integer not null," +
                "foreign key(treatment_id) references suspicious_treatment(id));");

        /**
         * Dynamic tables
         * Tables' below values are constantly modified
         */

        // create user table
        mSQLiteDatabase.execSQL("create table user (" +
                "id integer primary key not null," +
                "name varchar(50) not null," +
                "social_id varchar(50) not null, " +
                "user_name varchar(50) not null," +
                "photo_url varchar(500) not null);");

        // create friendship table
        mSQLiteDatabase.execSQL("create table friendship (" +
                "id integer primary key not null," +
                "user_added_id integer not null," +
                "user_adding_id integer not null," +
                "foreign key(user_added_id) references user(id)," +
                "foreign key(user_adding_id) references personal(id));");

        // creating phone table
        mSQLiteDatabase.execSQL("create table phone (" +
                "id integer primary key not null," +
                "number bigint not null," +
                "country_id integer," +
                "area_code integer," +
                "foreign key(country_id) references country(id)," +
                "foreign key(area_code) references area(code));");

        mSQLiteDatabase.execSQL("create table user_phone (" +
                "user_id integer not null," +
                "phone_id integer not null," +
                "is_property tinyint not null," +
                "primary key(user_id, phone_id)," +
                "foreign key(user_id) references user(id)," +
                "foreign key(phone_id) references phone(id));");

        // creating notification table
        mSQLiteDatabase.execSQL("create table notification (" +
                "id integer primary key not null," +
                "phone_id integer not null," +
                "notified_user_id integer not null," +
                "notifying_user_id integer not null," +
                "foreign key(phone_id) references phone(id)," +
                "foreign key(notified_user_id) references user(id)," +
                "foreign key(notifying_user_id) references personal(id));");

        // creating denunciation table
        mSQLiteDatabase.execSQL("create table denunciation (" +
                "id integer primary key not null," +
                "phone_id integer not null," +
                "foreign key(phone_id) references phone(id));");

        // creating denunciation/description table
        mSQLiteDatabase.execSQL("create table denunciation_description (" +
                "denunciation_id integer not null," +
                "description_id integer not null," +
                "primary key(denunciation_id, description_id)," +
                "foreign key(denunciation_id) references denunciation(id)," +
                "foreign key(description_id) references description(id));");
    }

    private void insertAttributes() {
        mSQLiteDatabase.execSQL("insert into user(id, name, social_id, user_name, photo_url) " +
                                "values(1, 'Enough Spam! Official', '0', 'EnoughSpamOfficial', " +
                                "'https://image.ibb.co/iBoxR5/Icon2.png');");

        // !!!!!!!!!!! NEVER ADD A NEW ATTRIBUTE ON TOP OF THE TABLE AFTER APP IS ON THE AIR!!!!!!!!!!!

        // inserting description attributes
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(0, 'Companhia bancária', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(1, 'Telemarketing', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(2, 'Oferta utópica', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(3, 'Oferta indesejada', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(4, 'Oferta repetitiva', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(5, 'Prisioneiro', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(6, 'Golpe', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(7, 'Chantagem', 0);");
        mSQLiteDatabase.execSQL("insert into description(id, description, treatment_id) values(8, 'Questões pessoais', 0);");

        // inserting suspicious treatment attributes
        mSQLiteDatabase.execSQL("insert into suspicious_treatment(id, treatment) values(0, 'Silenciar');");
        mSQLiteDatabase.execSQL("insert into suspicious_treatment(id, treatment) values(1, 'Bloquear');");

        // inserting countries
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(1, 1, 'United States', 'US', '999 999 9999');");
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(2, 1, 'Canada', 'CA', '999 999 9999');");
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(3, 1, 'República Dominicana', 'DO', '999 999 9999');");
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(4, 1, 'Puerto Rico', 'PR', '999 999 9999');");
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(5, 7, 'Россия', 'RU', '999 999 9999');");   // Russia
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(6, 7, 'Қазақстан', 'KZ', '999 999 99 99');");   // Kazakhstan
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(7, 20, 'مَصر\u200E\u200E', 'EG', '99 9999 9999');");    // Egypt
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(8, 39, 'Italia', 'IT', '');");
        mSQLiteDatabase.execSQL("insert into country(id, code, name, iso, mask) values(9, 55, 'Brasil', 'BR', '99 99999 9999');");

        // inserting states
        mSQLiteDatabase.execSQL("insert into state(id, name, country_id) values(1, 'São Paulo', 9);");
        mSQLiteDatabase.execSQL("insert into state(id, name, country_id) values(2, 'Rio de Janeiro', 9);");
        mSQLiteDatabase.execSQL("insert into state(id, name, country_id) values(3, 'Espírito Santo', 9);");
        mSQLiteDatabase.execSQL("insert into state(id, name, country_id) values(4, 'Minas Gerais', 9);");

        // inserting areas
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(11, 'Região metropolitana de São Paulo', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(12, 'São José dos Campos e região', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(13, 'Região retropolitana da Baixada Santista', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(14, 'Bauru, Jaú, Marília, Botucatu e região', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(15, 'Sorocaba e região', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(16, 'Ribeirão Preto, São Carlos, Araraquara e região', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(17, 'São José do Rio Preto e região', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(18, 'Presidente Prudente, Araçatuba e região', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(19, 'Região metropolitana de Campinas', 1);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(22, 'Campos dos Goytacazes e Região', 2);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(21, 'Região metropolitana do Rio de Janeiro', 2);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(24, 'Volta Redonda, Petrópolis e região', 2);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(27, 'Região metropolitana de Vitória', 3);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(28, 'Cachoeiro de Itapemirim e região', 3);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(31, 'Região metropolitana de Belo Horizonte', 4);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(32, 'Juiz de Fora e região', 4);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(33, 'Governador Valadares e região', 4);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(34, 'Uberlândia e região', 4);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(35, 'Poços de Caldas, Pouso Alegre, Varginha e região', 4);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(37, 'Divinópolis, Itaúna e região', 4);");
        mSQLiteDatabase.execSQL("insert into area(code, name, state_id) values(38, 'Montes Claros e região', 4);");
    }
}
