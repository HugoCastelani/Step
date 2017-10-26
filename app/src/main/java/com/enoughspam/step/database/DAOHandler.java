package com.enoughspam.step.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enoughspam.step.database.localDao.AreaDAO;
import com.enoughspam.step.database.localDao.CountryDAO;
import com.enoughspam.step.database.localDao.DescriptionDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.enoughspam.step.database.localDao.StateDAO;
import com.enoughspam.step.database.wideDao.FriendshipDAO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Hugo Castelani
 * Date: 24/07/17
 * Time: 13:07
 */

public class DAOHandler {
    private static Context sContext;

    private static SQLiteDatabase sWideDatabase;
    private static FirebaseDatabase sFirebaseDatabase;
    private static SQLiteDatabase sLocalDatabase;

    private DAOHandler() {}

    public static void init(@NonNull final Context context) {
        sContext = context;
    }

    public static void finish() {
        sWideDatabase.close();
        sLocalDatabase.close();
    }

    public static SQLiteDatabase getWideDatabase() {
        if (sWideDatabase == null) {
            sWideDatabase = new WideDatabaseHelper(sContext).getWritableDatabase();
        }
        return sWideDatabase;
    }

    public static DatabaseReference getFirebaseDatabase(@Nullable final String node) {
        if (sFirebaseDatabase == null) {
            sFirebaseDatabase = FirebaseDatabase.getInstance();
        }

        if (node == null || node.isEmpty()) {
            return sFirebaseDatabase.getReference();
        } else {
            return sFirebaseDatabase.getReference(node);
        }
    }

    public static SQLiteDatabase getLocalDatabase() {
        if (sLocalDatabase == null) {
            sLocalDatabase = new LocalDatabaseHelper(sContext).getWritableDatabase();
        }
        return sLocalDatabase;
    }

    public static void syncStaticTables(@NonNull final AnswerListener listener) {
        AnswerListener innerListener = new AnswerListener() {
            int count = 0;

            @Override
            public void onAnswerRetrieved() {
                if (++count == 4) {
                    listener.onAnswerRetrieved();
                }
            }
        };

        CountryDAO.sync(innerListener);
        StateDAO.sync(innerListener);
        AreaDAO.sync(innerListener);
        DescriptionDAO.sync(innerListener);
    }

    public static void syncDynamicTables(@NonNull final AnswerListener listener) {
        AnswerListener innerListener = new AnswerListener() {
            int count = 0;

            @Override
            public void onAnswerRetrieved() {
                if (++count == 1) {
                    FriendshipDAO.sync(this);
                } else if (count == 2) {
                    listener.onAnswerRetrieved();
                }
            }
        };

        LUserPhoneDAO.sync(innerListener);
    }

    public static Context getContext() {
        if (sContext != null) return sContext;
        throw new NullPointerException("You should call init method first.");
    }

    public interface AnswerListener {
        void onAnswerRetrieved();
    }
}
