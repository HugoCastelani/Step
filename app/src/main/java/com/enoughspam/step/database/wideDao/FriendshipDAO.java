package com.enoughspam.step.database.wideDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.localDao.LUserDAO;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 21:57
 */

public class FriendshipDAO {

    public static final String TABLE = "friendship";
    public static final String ID = "id";
    public static final String USER_ADDED_ID = "user_added_id";
    public static final String USER_ADDING_ID = "user_adding_id";

    private FriendshipDAO() {}

    public static Friendship generate(@NonNull final Cursor cursor) {
        final Friendship friendship = new Friendship();
        friendship.setId(cursor.getInt(cursor.getColumnIndex(ID)));

        UserDAO.findByID(
                cursor.getInt(cursor.getColumnIndex(USER_ADDED_ID)),
                retrievedUser -> friendship.setAdded(retrievedUser)
        );

        UserDAO.findByID(
                cursor.getInt(cursor.getColumnIndex(USER_ADDING_ID)),
                retrievedUser -> friendship.setAdding(retrievedUser)
        );

        return friendship;
    }

    public static int create(@NonNull final Friendship friendship) {
        ContentValues values = new ContentValues();

        values.put(USER_ADDED_ID, friendship.getAdded().getID());
        values.put(USER_ADDING_ID, friendship.getAdding().getID());

        final int id = (int) DAOHandler.getWideDatabase().insert(TABLE, null, values);

        if (id != -1) {
            values.put(ID, id);
            DAOHandler.getLocalDatabase().insert(TABLE, null, values);
            LUserDAO.clone(friendship.getAdded().getID());
        }

        return id;
    }

    public static void delete(@NonNegative final int id) {
        DAOHandler.getWideDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
        DAOHandler.getLocalDatabase().delete(
                TABLE, ID + " = ?", new String[] {String.valueOf(id)});
    }

    public static void delete(@NonNegative final int addedID, @NonNegative final int addingID) {
        DAOHandler.getWideDatabase().delete(
                TABLE, USER_ADDED_ID + " = ? AND " + USER_ADDING_ID + " = ?",
                new String[] {String.valueOf(addedID), String.valueOf(addingID)});
        DAOHandler.getLocalDatabase().delete(
                TABLE, USER_ADDED_ID + " = ? AND " + USER_ADDING_ID + " = ?",
                new String[] {String.valueOf(addedID), String.valueOf(addingID)});
    }
}
