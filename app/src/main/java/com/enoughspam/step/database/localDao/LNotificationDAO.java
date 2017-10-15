package com.enoughspam.step.database.localDao;

import android.database.Cursor;

import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Notification;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.wideDao.NotificationDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;
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
        List<User> friendList = LFriendshipDAO.findUserFriends(id);
        List<PhoneSection> phoneSectionList = new ArrayList<>();

        final String prefix = DAOHandler.getContext().getResources().getString(R.string.numbers_of);
        for (int i = 0; i < friendList.size(); i++) {
            final User tempUser = friendList.get(i);
            final List<Phone> phoneList = UserPhoneDAO.getPhoneList(tempUser.getID());

            phoneSectionList.add(new PhoneSection(prefix + tempUser.getUserName(), phoneList));
        }

        return phoneSectionList;
    }
}
