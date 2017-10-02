package com.enoughspam.step.database.localDao;

import android.database.Cursor;

import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Notification;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.wideDao.NotificationDAO;
import com.enoughspam.step.domain.PhoneSection;

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

    public static List<PhoneSection> getFriendsBlockedList(@NonNegative final int id) {
        List<Notification> notificationList = LNotificationDAO.getNotificationList(id);
        List<PhoneSection> phoneSectionList = new ArrayList<>();

        outerLoop:
        for (int i = 0; i < notificationList.size(); i++) {
            final Notification notification = notificationList.get(i);
            final String notifyingUserName = LUserDAO.findById(notification.getNotifyingUserId()).getUserName();
            final Phone phone = LPhoneDAO.findById(notification.getPhoneId());

            for (int j = 0; j < phoneSectionList.size(); j++) {
                final String userName = phoneSectionList.get(j).getUserName();

                if (notifyingUserName.equals(userName)) {
                    phoneSectionList.get(j).addPhone(phone);
                    continue outerLoop;
                }
            }

            final List<Phone> phoneList = new ArrayList<>();
            final String prefix = DAOHandler.getContext().getResources().getString(R.string.numbers_of);

            phoneList.add(phone);
            phoneSectionList.add(new PhoneSection(prefix + notifyingUserName, phoneList));
        }

        return phoneSectionList;
    }
}
