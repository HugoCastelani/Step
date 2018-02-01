package com.hugocastelani.ivory.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hugocastelani.ivory.database.dao.DAOHandler;
import com.hugocastelani.ivory.database.dao.intangible.GenericLocalDAO;
import com.hugocastelani.ivory.database.domain.Notification;
import com.hugocastelani.ivory.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:52
 */

public final class LNotificationDAO extends GenericLocalDAO<Notification> {
    private static LNotificationDAO instance;

    public static final String PHONE_KEY = "phone_key";
    public static final String NOTIFIED_USER_KEY = "notified_user_key";
    public static final String NOTIFYING_USER_KEY = "notifying_user_key";

    @Override
    protected void prepareFields() {
        table = "notification";
    }

    private LNotificationDAO() {}

    public LNotificationDAO get() {
        if (instance == null) {
            instance = new LNotificationDAO();
        }
        return instance;
    }

    @Override
    protected Notification generate(@NonNull Cursor cursor) {
        return new Notification(
                cursor.getString(cursor.getColumnIndex(PHONE_KEY)),
                cursor.getString(cursor.getColumnIndex(NOTIFIED_USER_KEY)),
                cursor.getString(cursor.getColumnIndex(NOTIFYING_USER_KEY))
        );
    }

    @Override
    public LNotificationDAO create(@NonNull Notification notification) {
        if (!exists(notification).equals("-1")) {
            final ContentValues values = new ContentValues();

            values.put(PHONE_KEY, notification.getPhoneKey());
            values.put(NOTIFIED_USER_KEY, notification.getNotifiedKey());
            values.put(NOTIFYING_USER_KEY, notification.getNotifyingKey());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }

        return instance;
    }

    @Override @Deprecated
    public LNotificationDAO update(@NonNull Notification notification) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public LNotificationDAO delete(@NonNull String key) {
        throw new UnsupportedOperationException("You shouldn't do this. Use method with " +
                "notification parameter instead.");
    }

    public LNotificationDAO delete(@NonNull Notification notification) {
        final String where = PHONE_KEY + " = ? AND " + NOTIFIED_USER_KEY + " = ? AND "
                + NOTIFYING_USER_KEY + " = ?";

        final String[] args = new String[] {notification.getPhoneKey(),
                notification.getNotifiedKey(), notification.getNotifyingKey()};

        DAOHandler.getLocalDatabase().delete(table, where, args);
        return instance;
    }

    @Override
    public String exists(@NonNull Notification notification) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table,
                new String[] {PHONE_KEY}, PHONE_KEY + " = ?",
                new String[] {notification.getPhoneKey()}, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(PHONE_KEY));
        } else return "-1";
    }

    @Override
    public GenericLocalDAO<Notification> sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }
}
