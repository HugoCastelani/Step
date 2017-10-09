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

    public static Friendship findByIDs(@NonNegative final int addedID, @NonNegative final int addingID) {
        Cursor cursor = DAOHandler.getLocalDatabase().query(
                FriendshipDAO.TABLE, null,
                FriendshipDAO.USER_ADDED_ID + " = ? AND " + FriendshipDAO.USER_ADDING_ID + " = ?",
                new String[] {String.valueOf(addedID), String.valueOf(addingID)},
                null, null, null);

        Friendship friendship = null;
        if (cursor.moveToFirst()) friendship = FriendshipDAO.generate(cursor);

        return friendship;
    }
}
