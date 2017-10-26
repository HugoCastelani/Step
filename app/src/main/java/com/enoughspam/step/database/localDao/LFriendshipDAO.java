package com.enoughspam.step.database.localDao;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.domain.PhoneSection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 23:14
 */

public class LFriendshipDAO {
    public static final String TABLE = "friendship";
    public static final String USER_ADDED_ID = "user_added_id";
    public static final String USER_ADDING_ID = "user_adding_id";

    private LFriendshipDAO() {}

    public static Friendship generate(@NonNull final Cursor cursor) {
        return new Friendship(
            LUserDAO.findByID(cursor.getInt(cursor.getColumnIndex(USER_ADDED_ID))),
            LUserDAO.findByID(cursor.getInt(cursor.getColumnIndex(USER_ADDING_ID)))
        );
    }

    public static void create(@NonNull final Friendship friendship) {
        LUserDAO.clone(friendship.getAdded());
        ContentValues values = new ContentValues();

        values.put(USER_ADDED_ID, friendship.getAdded().getID());
        values.put(USER_ADDING_ID, friendship.getAdding().getID());

        DAOHandler.getLocalDatabase().insert(TABLE, null, values);
    }

    public static void delete(@NonNegative final int addedID, @NonNegative final int addingID) {
        DAOHandler.getLocalDatabase().delete(
                TABLE, USER_ADDED_ID + " = ? AND " + USER_ADDING_ID + " = ?",
                new String[] {String.valueOf(addedID), String.valueOf(addingID)});
    }

    public static Friendship findByIDs(@NonNegative final int addedID, @NonNegative final int addingID) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(TABLE, null,
                USER_ADDED_ID + " = ? AND " + USER_ADDING_ID + " = ?",
                new String[] {String.valueOf(addedID), String.valueOf(addingID)},
                null, null, null);

        Friendship friendship = null;
        if (cursor.moveToFirst()) friendship = generate(cursor);

        return friendship;
    }

    public static List<User> findUserFriends(@NonNegative final int id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(TABLE, null,
                USER_ADDING_ID + " = ?", new String[] {String.valueOf(id)},
                null, null, null);

        final List<User> friendList = new ArrayList<>();
        while (cursor.moveToNext()) {
            friendList.add(LUserDAO.findByID(
                    cursor.getInt(cursor.getColumnIndex(USER_ADDED_ID))
            ));
        }

        return friendList;
    }

    public static List<PhoneSection> getFriendsBlockedList(@NonNegative final int id) {
        List<User> friendList = findUserFriends(id);
        List<PhoneSection> phoneSectionList = new ArrayList<>();

        final String prefix = DAOHandler.getContext().getResources().getString(R.string.numbers_of);
        for (int i = 0; i < friendList.size(); i++) {
            final User tempUser = friendList.get(i);
            final List<Phone> phoneList = LUserPhoneDAO.getPhoneList(tempUser.getID());

            phoneSectionList.add(new PhoneSection(prefix + tempUser.getUsername(), phoneList));
        }

        return phoneSectionList;
    }
}
