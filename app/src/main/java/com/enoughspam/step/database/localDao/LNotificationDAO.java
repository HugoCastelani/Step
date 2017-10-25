package com.enoughspam.step.database.localDao;

import android.database.Cursor;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Notification;
import com.enoughspam.step.database.wideDao.NotificationDAO;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Notification> getNotificationList(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(NotificationDAO.TABLE, null,
                NotificationDAO.NOTIFIED_USER_ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        List<Notification> notificationList = new ArrayList<>();
        while (cursor.moveToNext()) {
            notificationList.add(NotificationDAO.generate(cursor));
        }

        cursor.close();
        return notificationList;
    }

}
