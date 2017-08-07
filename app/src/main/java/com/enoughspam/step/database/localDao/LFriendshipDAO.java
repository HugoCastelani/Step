package com.enoughspam.step.database.localDao;

import android.database.Cursor;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.wideDao.FriendshipDAO;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 23:14
 */

public class LFriendshipDAO {
    private LFriendshipDAO() {}

    public static Friendship findById(@NonNegative final int id) {
        Cursor cursor = DAOHandler.getLocalDatabase().query(
                FriendshipDAO.TABLE, null, FriendshipDAO.ID + " = ?",
                new String[] {String.valueOf(id)}, null, null, null);

        Friendship friendship = null;
        if (cursor.moveToFirst()) FriendshipDAO.generate(cursor);

        return friendship;
    }
}
