package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Notification;

/**
 * Created by Hugo Castelani
 * Date: 31/07/17
 * Time: 16:15
 */

public class NotificationDAO {

    public static final String TABLE = "notification";
    public static final String ID = "id";
    public static final String PHONE_ID = "phone_id";
    public static final String NOTIFIED_USER_ID = "notified_user_id";
    public static final String NOTIFYING_USER_ID = "notifying_user_id";

    private NotificationDAO() {}

    public static Notification generate(@NonNull final Cursor cursor) {
        return new Notification(
            cursor.getInt(cursor.getColumnIndex(ID)),
            cursor.getInt(cursor.getColumnIndex(PHONE_ID)),
            cursor.getInt(cursor.getColumnIndex(NOTIFIED_USER_ID)),
            cursor.getInt(cursor.getColumnIndex(NOTIFYING_USER_ID))
        );
    }

    public static int create(@NonNull final Notification notification) {
        ContentValues values = new ContentValues();

        values.put(PHONE_ID, notification.getPhoneId());
        values.put(NOTIFIED_USER_ID, notification.getNotifiedUserId());
        values.put(NOTIFYING_USER_ID, notification.getNotifyingUserId());

        final int id = (int) DAOHandler.getWideDatabase().insert(TABLE, null, values);

        if (id != -1) {
            values.put(ID, id);
            DAOHandler.getLocalDatabase().insert(TABLE, null, values);
        }

        return id;
    }

    public static Notification findByID(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(TABLE, null,
                ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Notification notification = null;
        if (cursor.moveToFirst()) notification = NotificationDAO.generate(cursor);

        cursor.close();
        return notification;
    }

    public static void delete(@NonNull final int id) {
        DAOHandler.getWideDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
        DAOHandler.getLocalDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

}
