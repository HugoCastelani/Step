package com.enoughspam.step.database.localDao;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
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

    public static List<Phone> getPhoneList() {
        final int id = LUserDAO.getThisUser().getId();

        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                UserPhoneDAO.TABLE, new String[] {UserPhoneDAO.PHONE_ID},
                UserPhoneDAO.USER_ID + " = ? AND " + UserPhoneDAO.IS_PROPERTY + " = ?",
                new String[] {String.valueOf(id), "0"}, null, null, null);

        final List<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(LPhoneDAO.findById(
                    cursor.getInt(cursor.getColumnIndex(UserPhoneDAO.PHONE_ID)))
            );
        }

        cursor.close();
        return phoneList;
    }

    public static boolean isBlocked(@NonNull final UserPhone userPhone) {
        final String result = String.valueOf(LPhoneDAO.exists(userPhone.getPhone()));

        if (!result.equals("-1")) {

            final Cursor cursor = DAOHandler.getLocalDatabase().query(UserPhoneDAO.TABLE, null,
                    UserPhoneDAO.USER_ID + " = ? AND " + UserPhoneDAO.PHONE_ID + " = ?",
                    new String[] {String.valueOf(userPhone.getUser().getId()), result},
                    null, null, null);

            UserPhone matchingUserPhone = null;

            if (cursor.moveToFirst()) matchingUserPhone = UserPhoneDAO.generate(cursor);

            cursor.close();

            if (matchingUserPhone == null) return false;
            else return true;

        } else return false;
    }
}
