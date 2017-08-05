package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.wideDao.PhoneDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 18:25
 */

public class LUserPhoneDAO {
    private LUserPhoneDAO() {}

    /**
     * @param ids the correct order is user ID and phone ID
     */
    public static UserPhone findByIds(@NonNegative final int[] ids) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(UserPhoneDAO.TABLE, null,
                UserPhoneDAO.USER_ID + " = ? AND " + UserPhoneDAO.PHONE_ID + " = ?",
                new String[] {String.valueOf(ids[0]), String.valueOf(ids[1])},
                null, null, null);

        UserPhone userPhone = null;

        if (cursor.moveToFirst()) userPhone = UserPhoneDAO.generate(cursor);

        cursor.close();
        return userPhone;
    }

    public static List<Phone> getPhoneList() {
        final Cursor cursor = DAOHandler.getWideDatabase().query(
                UserPhoneDAO.TABLE, new String[] {UserPhoneDAO.PHONE_ID},
                UserPhoneDAO.IS_PROPERTY + " = ?", new String[] {"0"},
                null, null, null);

        final List<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(PhoneDAO.findById(
                    cursor.getInt(cursor.getColumnIndex(UserPhoneDAO.PHONE_ID)))
            );
        }

        cursor.close();
        return phoneList;
    }

    public static void clone(@NonNegative final int id) {
        List<UserPhone> userPhoneList = UserPhoneDAO.getUsersUserPhoneList(id);

        for (int i = 0; i < userPhoneList.size(); i++) {
            final UserPhone userPhone = userPhoneList.get(i);
            final int[] ids = new int[] {userPhone.getUser().getId(), userPhone.getPhone().getId()};

            if (findByIds(ids) == null) {
                ContentValues values = new ContentValues();

                values.put(UserPhoneDAO.USER_ID, userPhone.getUser().getId());
                values.put(UserPhoneDAO.PHONE_ID, userPhone.getPhone().getId());
                values.put(UserPhoneDAO.IS_PROPERTY, userPhone.isProperty());

                DAOHandler.getLocalDatabase().insert(UserPhoneDAO.TABLE, null, values);
            }

            LPhoneDAO.clone(userPhone.getPhone().getId());  // tests by itself
        }
    }
}
