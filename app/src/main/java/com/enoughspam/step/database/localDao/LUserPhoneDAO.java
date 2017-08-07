package com.enoughspam.step.database.localDao;

import android.database.Cursor;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
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
}
