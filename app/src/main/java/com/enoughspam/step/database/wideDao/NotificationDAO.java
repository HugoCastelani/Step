package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Notification;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.domain.PhoneSection;

import java.util.ArrayList;
import java.util.List;

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

        return (int) DAOHandler.getWideDatabase().insert(TABLE, null, values);
    }

    public static Notification findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getWideDatabase().query(NotificationDAO.TABLE, null,
                NotificationDAO.ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        Notification notification = null;
        if (cursor.moveToFirst()) notification = NotificationDAO.generate(cursor);

        cursor.close();
        return notification;
    }

    public static void delete(@NonNull final int id) {
        DAOHandler.getWideDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static List<PhoneSection> getFriendsBlockedList(final int id, @NonNull final Context context) {
        Cursor cursor = DAOHandler.getWideDatabase().query(TABLE, null,
                NOTIFIED_USER_ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        List<PhoneSection> phoneSectionList = new ArrayList<>();

        outerLoop:
        while (cursor.moveToNext()) {
            final Notification notification = generate(cursor);
            final String notifyingUserName = UserDAO.findById(notification.getNotifyingUserId()).getName();
            final Phone phone = PhoneDAO.findById(notification.getPhoneId());

            for (int j = 0; j < phoneSectionList.size(); j++) {
                final String userName = phoneSectionList.get(j).getUserName();

                if (notifyingUserName.equals(userName)) {
                    phoneSectionList.get(j).addPhone(phone);
                    continue outerLoop;
                }
            }

            final List<Phone> phoneList = new ArrayList<>();
            final String prefix = context.getResources().getString(R.string.numbers_of);

            phoneList.add(phone);
            phoneSectionList.add(new PhoneSection(prefix + notifyingUserName, phoneList));
        }

        return phoneSectionList;
    }

}
