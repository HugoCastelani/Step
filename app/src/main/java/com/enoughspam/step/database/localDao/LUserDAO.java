package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.wideDao.UserDAO;

/**
 * Created by Hugo Castelani
 * Date: 03/08/17
 * Time: 22:30
 */

public class LUserDAO {
    private LUserDAO() {}

    public static User findById(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                UserDAO.TABLE, null, UserDAO.ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        User area = null;

        if (cursor.moveToFirst()) area = UserDAO.generate(cursor);

        cursor.close();
        return area;
    }

    public static int clone(@NonNegative final int id) {
        if (findById(id) == null) {
            final User user = UserDAO.findById(id);
            final ContentValues values = new ContentValues();

            values.put(UserDAO.ID, user.getId());
            values.put(UserDAO.SOCIAL_ID, user.getSocialId());
            values.put(UserDAO.NAME, user.getName());

            int userId = (int) DAOHandler.getLocalDatabase().insert(UserDAO.TABLE, null, values);
            if (userId != -1) {
                user.setId(userId);
            }

            return userId;
        }

        return -1;
    }

    public static User get() {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(
                UserDAO.TABLE, null, null, null, null, null, null);

        User user = null;
        if (cursor.moveToFirst()) user = UserDAO.generate(cursor);

        cursor.close();
        return user;
    }
}
