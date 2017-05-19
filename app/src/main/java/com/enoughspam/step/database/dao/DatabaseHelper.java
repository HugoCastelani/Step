package com.enoughspam.step.database.dao;

// created by Hugo on 15/05/17 at 19:09

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        sqLiteDatabase.execSQL("create table tbUser (" +
                "cdUser integer primary key not null," +
                "nmUser varchar(50) not null)");

        // create friendship table
        sqLiteDatabase.execSQL("create table tbFriendship (" +
                "cdFriendship integer primary key not null," +
                "cdUserAdded integer not null," +
                "cdUserAdding integer not null," +
                "foreign key(cdUserAdded) references tbUser (cdUser)," +
                "foreign key(cdUserAdding) references tbUser (cdUser))");

        // creating country code table
        sqLiteDatabase.execSQL("create table tbCountryCode (" +
                "cdCountryCode integer primary key not null," +
                "cdCountry varchar(10) not null," +
                "nmCountry varchar(50) not null)");

        // creating area code table
        sqLiteDatabase.execSQL("create table tbAreaCode (" +
                "cdAreaCode integer primary key not null," +
                "cdArea varchar(10) not null," +
                "nmArea varchar(50) not null)");

        // creating phone table
        sqLiteDatabase.execSQL("create table tbPhone (" +
                "cdPhone integer primary key not null," +
                "nbPhone varchar(20) not null," +
                "cdCountryCode integer not null," +
                "cdAreaCode integer not null," +
                "cdUser integer," +
                "foreign key(cdCountryCode) references tbCountryCode(cdCountryCode)," +
                "foreign key(cdAreaCode) references tbAreaCode(cdAreaCode))," +
                "foreign key(cdUser) references tbUser(cdUser))");

        // creating notification table
        sqLiteDatabase.execSQL("create table tbNotification (" +
                "cdNotification integer primary key not null," +
                "cdPhone integer not null," +
                "cdUserNotified integer not null," +
                "foreign key(cdPhone) references tbPhone(cdPhone)," +
                "foreign key(cdUserNotified) references tbUser(cdUserNotified))");

        // creating denunciation table
        sqLiteDatabase.execSQL("create table tbDenunciation (" +
                "cdDenunciation integer primary key not null," +
                "cdPhone integer not null," +
                "foreign key(cdPhone) references tbPhone(cdPhone))");

        // creating description table
        sqLiteDatabase.execSQL("create table tbDescription (" +
                "cdDescription integer primary key not null," +
                "description varchar(50) not null)");

        // creating denunciation/description table
        sqLiteDatabase.execSQL("create table tbDenunciationDescription (" +
                "cdDenunciation integer primary key not null," +
                "cdDescription integer primary key not null," +
                "foreign key(cdDenunciation) references tbDenunciation(cdDenunciation)," +
                "foreign key(cdDescription) references tbDescription(cdDescription))");

        // creating suspicious treatment table
        sqLiteDatabase.execSQL("create table tbSuspiciousTreatment (" +
                "cdSuspiciousTreatment integer primary key not null," +
                "nmSuspiciousTreatment varchar(20) not null)");

        // creating treatment configuration table
        sqLiteDatabase.execSQL("create table tbConfigTreatment (" +
                "cdDescription integer primary key not null," +
                "cdSuspiciousTreatment integer primary key not null," +
                "foreign key(cdDescription) references tbDescription(cdDescription)," +
                "foreign key(cdSuspiciousTreatment) references tbSuspiciousTreatment(cdSuspiciousTreatment))");

        // NOT RELATED TABLES

        // creating feedback configuration table
        sqLiteDatabase.execSQL("create table tbConfigFeedback (" +
                "nmCallKind varchar(20) primary key not null," +
                "showFeedback integer not null)");

        // creating service blocking configuration table
        sqLiteDatabase.execSQL("create table tbConfigServiceBlock (" +
                "nmService varchar(20) primary key not null," +
                "block integer not null)");

        // creating guide configuration table
        sqLiteDatabase.execSQL("create table tbConfigGuide (" +
                "nmView varchar(20) primary key not null," +
                "showGuide integer not null)");

        // creating network to download DB configuration table
        sqLiteDatabase.execSQL("create table tbConfigNetworkDownloadDB (" +
                "nmNetworkDownload varchar(20) primary key not null," +
                "downloadIt not null)");

        // creating suspicious denunciations amount configuration table
        sqLiteDatabase.execSQL("create table tbSuspiciousDenunciationsAmount (" +
                "denunciationAmount integer primary key not null)");

        // creating theme configuration table
        sqLiteDatabase.execSQL("create table tbConfigTheme (" +
                "isDark integer primary key not null," +
                "lightAccentColor char(7) not null," +
                "defaultLightAccentColor char(7) not null," +
                "darkAccentColor char(7) not null," +
                "defaultDarkAccentColor char(7) not null)");

        sqLiteDatabase.execSQL("insert into tbConfigTheme values(0, '#00BCD4', '#00BCD4', '#CDDC39', '#CDDC39')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    // RELATED TABLES

    public static class User {
        public static final String TABLE = "tbUser";
        public static final String USER_CODE = "cdUser";
        public static final String USER_NAME = "nmUser";

        public static final String[] COLUMNS = new String[] {USER_CODE, USER_NAME};
    }


    public static class Friendship {
        public static final String TABLE = "tbFriendship";
        public static final String FRIEND_SHIP_CODE = "cdFriendship";
        public static final String USER_ADDED_CODE = "cdUserAdded";
        public static final String USER_ADDING_CODE = "cdUserAdding";

        public static final String[] COLUMNS = new String[] {FRIEND_SHIP_CODE, USER_ADDED_CODE, USER_ADDING_CODE};
    }

    public static class CountryCode {
        public static final String TABLE = "tbCountryCode";
        public static final String COUNTRY_CODE_CODE = "cdCountryCode";
        public static final String COUNTRY_CODE = "cdCountry";
        public static final String COUNTRY_NAME = "nmCountry";

        public static final String[] COLUMNS = new String[] {COUNTRY_CODE_CODE, COUNTRY_CODE, COUNTRY_NAME};
    }

    public static class AreaCode {
        public static final String TABLE = "tbAreaCode";
        public static final String AREA_CODE_CODE = "cdAreaCode";
        public static final String AREA_CODE = "cdArea";
        public static final String AREA_NAME = "nmArea";

        public static final String[] COLUMNS = new String[] {AREA_CODE_CODE, AREA_CODE, AREA_NAME};
    }

    public static class Phone {
        public static final String TABLE = "tbPhone";
        public static final String PHONE_CODE = "cdPhone";
        public static final String PHONE_NUMBER = "nbPhone";
        public static final String COUNTRY_CODE_CODE = "cdCountryCode";
        public static final String AREA_CODE_CODE = "cdAreaCode";
        public static final String USER_CODE = "cdUser";

        public static final String[] COLUMNS = new String[] {PHONE_CODE, PHONE_NUMBER, COUNTRY_CODE_CODE,
                AREA_CODE_CODE, USER_CODE};
    }

    public static class Notification {
        public static final String TABLE = "tbNotification";
        public static final String NOTIFICATION_CODE = "cdNotification";
        public static final String PHONE_CODE = "cdPhone";
        public static final String NOTIFIED_USER_CODE= "cdUserNotified";

        public static final String[] COLUMNS = new String[] {NOTIFICATION_CODE, PHONE_CODE, NOTIFIED_USER_CODE};
    }

    public static class Denunciation  {
        public static final String TABLE = "tbDenunciation";
        public static final String DENUNCIATION_CODE = "cdDenunciation";
        public static final String PHONE_CODE = "cdPhone";

        public static final String[] COLUMNS = new String[] {DENUNCIATION_CODE, PHONE_CODE};
    }

    public static class Description {
        public static final String TABLE = "tbDescription";
        public static final String DESCRIPTION_CODE = "cdDescription";
        public static final String DESCRIPTION = "description";

        public static final String[] COLUMNS = new String[] {DESCRIPTION_CODE, DESCRIPTION};
    }

    public static class DenunciationDescription {
        public static final String TABLE = "tbDenunciationDescription";
        public static final String DENUNCIATION_CODE = "cdDenunciation";
        public static final String DESCRIPTION_CODE = "cdDescription";

        public static final String[] COLUMNS = new String[] {DENUNCIATION_CODE, DESCRIPTION_CODE};
    }

    public static class SuspiciousTreatment {
        public static final String TABLE = "tbSuspiciousTreatment";
        public static final String SUSPICIOUS_TREATMENT_CODE = "cdSuspiciousTreatment";
        public static final String SUSPICIOUS_TREATMENT_NAME = "nmSuspiciousTreatment";

        public static final String[] COLUMNS = new String[] {SUSPICIOUS_TREATMENT_CODE, SUSPICIOUS_TREATMENT_NAME};
    }

    public static class ConfigTreatment {
        public static final String TABLE = "tbConfigTreatment";
        public static final String DESCRIPTION_CODE = "cdDescription";
        public static final String SUSPICIOUS_TREATMENT_CODE = "cdSuspiciousTreatment";

        public static final String[] COLUMNS = new String[] {DESCRIPTION_CODE, SUSPICIOUS_TREATMENT_CODE};
    }

    // NOT RELATED TABLES

    public static class ConfigFeedback {
        public static final String TABLE = "tbConfigFeedback";
        public static final String CALL_KIND_NAME = "nmCallKind";
        public static final String SHOW_FEEDBACK = "showFeedback";

        public static final String[] COLUMNS = new String[] {CALL_KIND_NAME, SHOW_FEEDBACK};
    }

    public static class ConfigServiceBlock {
        public static final String TABLE = "tbConfigServiceBlock";
        public static final String SERVICE_NAME = "nmService";
        public static final String BLOCK = "block";

        public static final String[] COLUMNS = new String[] {SERVICE_NAME, BLOCK};
    }

    public static class ConfigGuide {
        public static final String TABLE = "tbConfigGuide";
        public static final String VIEW_NAME = "nmView";
        public static final String SHOW_GUIDE = "showGuide";

        public static final String[] COLUMNS = new String[] {VIEW_NAME, SHOW_GUIDE};
    }

    public static class ConfigNetworkDownloadDB {
        public static final String TABLE = "tbConfigNetworkDownloadDB";
        public static final String NETWORK_DOWNLOAD_NAME = "nmNetworkDownload";
        public static final String DOWNLOAD_IT = "downloadIt";

        public static final String[] COLUMNS = new String[] {NETWORK_DOWNLOAD_NAME, DOWNLOAD_IT};
    }

    public static class SuspiciousDenunciationsAmount {
        public static final String TABLE = "tbSuspiciousDenunciationsAmount";
        public static final String DENUNCIATION_AMOUNT = "denunciationAmount";

        public static final String[] COLUMNS = new String[] {DENUNCIATION_AMOUNT};
    }

    public static class ConfigTheme {
        public static final String TABLE = "tbConfigTheme";
        public static final String IS_DARK = "isDark";
        public static final String LIGHT_ACCENT_COLOR = "lightAccentColor";
        public static final String DEFAULT_LIGHT_ACCENT_COLOR = "defaultLightAccentColor";
        public static final String DARK_ACCENT_COLOR= "darkAccentColor";
        public static final String DEFAULT_DARK_ACCENT_COLOR= "defaultDarkAccentColor";

        public static final String[] COLUMNS = new String[] {IS_DARK, LIGHT_ACCENT_COLOR, DEFAULT_LIGHT_ACCENT_COLOR,
                DARK_ACCENT_COLOR, DEFAULT_DARK_ACCENT_COLOR};
    }
}
