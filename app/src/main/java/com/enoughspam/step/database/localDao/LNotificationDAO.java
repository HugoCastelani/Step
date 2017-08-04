package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Notification;
import com.enoughspam.step.database.wideDao.NotificationDAO;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:52
 */

public class LNotificationDAO {
    private LNotificationDAO() {}

    public static Notification findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(NotificationDAO.TABLE, null,
                NotificationDAO.ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Notification notification = null;
        if (cursor.moveToFirst()) notification = NotificationDAO.generate(cursor);

        cursor.close();
        return notification;
    }

    public static int clone(@NonNegative final int id) {
        if (findById(id) == null) {
            Notification notification = NotificationDAO.findById(id);
            final ContentValues values = new ContentValues();

            values.put(NotificationDAO.ID, notification.getId());
            values.put(NotificationDAO.PHONE_ID, notification.getId());
            values.put(NotificationDAO.NOTIFIED_USER_ID, notification.getNotifiedUserId());
            values.put(NotificationDAO.NOTIFYING_USER_ID, notification.getNotifyingUserId());

            return (int) DAOHandler.getLocalDatabase().insert(NotificationDAO.TABLE, null, values);
        }

        return -1;
    }
}
