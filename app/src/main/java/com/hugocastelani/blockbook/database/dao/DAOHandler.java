package com.hugocastelani.blockbook.database.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hugocastelani.blockbook.database.LocalDatabaseHelper;
import com.hugocastelani.blockbook.database.dao.local.LAreaDAO;
import com.hugocastelani.blockbook.database.dao.local.LCountryDAO;
import com.hugocastelani.blockbook.database.dao.local.LStateDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserFollowerDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserPhoneDAO;
import com.hugocastelani.blockbook.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 24/07/17
 * Time: 13:07
 */

public final class DAOHandler {
    private static Context sContext;

    private static FirebaseDatabase sFirebaseDatabase;
    private static SQLiteDatabase sLocalDatabase;

    private DAOHandler() {}

    public static void init(@NonNull final Context context) {
        sContext = context;
    }

    public static Context getContext() {
        if (sContext != null) return sContext;
        throw new NullPointerException("You should call init method first.");
    }

    public static void finish() {
        sLocalDatabase.close();
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

    public static void syncStaticTables(@NonNull final Listeners.AnswerListener listener) {
        Listeners.AnswerListener innerListener = new Listeners.AnswerListener() {
            int count = 0;

            @Override
            public void onAnswerRetrieved() {
                if (++count == 3) {
                    listener.onAnswerRetrieved();
                }
            }

            @Override public void onError() {}
        };

        LCountryDAO.get().sync(innerListener);
        LStateDAO.get().sync(innerListener);
        LAreaDAO.get().sync(innerListener);
    }

    public static void syncDynamicTables(@NonNull final Listeners.AnswerListener listener) {
        Listeners.AnswerListener innerListener = new Listeners.AnswerListener() {
            int count = 0;

            @Override
            public void onAnswerRetrieved() {
                if (++count == 2) {
                    listener.onAnswerRetrieved();
                }
            }

            @Override public void onError() {}
        };

        UserPhoneDAO.get().sync(innerListener);
        UserFollowerDAO.get().sync(innerListener);
    }

}
