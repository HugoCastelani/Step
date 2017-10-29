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
import com.enoughspam.step.database.localDao.abstracts.GenericLocalDAO;
import com.enoughspam.step.domain.PhoneSection;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 23:14
 */

public class LFriendshipDAO extends GenericLocalDAO<Friendship> {
    private static LFriendshipDAO instance;

    public static final String USER_ADDED_ID = "user_added_id";
    public static final String USER_ADDING_ID = "user_adding_id";

    @Override
    protected void prepareFields() {
        table = "friendship";
    }

    private LFriendshipDAO() {}

    public static LFriendshipDAO get() {
        if (instance == null) instance = new LFriendshipDAO();
        return instance;
    }

    @Override
    public Friendship generate(@NonNull final Cursor cursor) {
        return new Friendship(
            LUserDAO.get().findByColumn(LUserDAO.id,
                    cursor.getString(cursor.getColumnIndex(USER_ADDED_ID))),
            LUserDAO.get().findByColumn(LUserDAO.id,
                    cursor.getString(cursor.getColumnIndex(USER_ADDING_ID)))
        );
    }

    @Override
    public LFriendshipDAO create(@NonNull final Friendship friendship) {
        if (findByIDs(friendship.getAddedID(), friendship.getAddingID()) == null) {

            LUserDAO.get().clone(friendship.getAdded());
            ContentValues values = new ContentValues();

            values.put(USER_ADDED_ID, friendship.getAddedID());
            values.put(USER_ADDING_ID, friendship.getAddingID());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }
        return instance;
    }

    @Override @Deprecated
    public LFriendshipDAO update(@NonNull Friendship friendship) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public LFriendshipDAO delete(@NonNegative final Integer id) {
        throw new UnsupportedOperationException("You shouldn't do this. Call overloaded method" +
                " with two parameters instead.");
    }

    public LFriendshipDAO delete(@NonNegative final Integer addedID, @NonNegative final Integer addingID) {
        DAOHandler.getLocalDatabase().delete(
                table, USER_ADDED_ID + " = ? AND " + USER_ADDING_ID + " = ?",
                new String[] {addedID.toString(), addingID.toString()});
        return instance;
    }

    @Override @Deprecated
    public int exists(@NonNull Friendship friendship) {
        throw new UnsupportedOperationException("You shouldn't do this. Call findByIDs()" +
                " method instead.");
    }

    public Friendship findByIDs(@NonNegative final Integer addedID, @NonNegative final Integer addingID) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_ADDED_ID + " = ? AND " + USER_ADDING_ID + " = ?",
                new String[] {addedID.toString(), addingID.toString()}, null, null, null);

        Friendship friendship = null;
        if (cursor.moveToFirst()) friendship = generate(cursor);

        return friendship;
    }

    public ArrayList<User> findUserFriends(@NonNegative final Integer id) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_ADDING_ID + " = ?", new String[] {id.toString()}, null, null, null);

        final ArrayList<User> friendList = new ArrayList<>();
        while (cursor.moveToNext()) {
            friendList.add(LUserDAO.get().findByColumn(LUserDAO.id,
                    cursor.getString(cursor.getColumnIndex(USER_ADDED_ID))
            ));
        }

        return friendList;
    }

    public ArrayList<PhoneSection> getFriendsBlockedList(@NonNegative final Integer id) {
        ArrayList<User> friendList = findUserFriends(id);
        ArrayList<PhoneSection> phoneSectionList = new ArrayList<>();

        final String prefix = DAOHandler.getContext().getResources().getString(R.string.numbers_of);
        for (int i = 0; i < friendList.size(); i++) {
            final User tempUser = friendList.get(i);
            final ArrayList<Phone> phoneList = LUserPhoneDAO.get().getPhoneList(tempUser.getID());

            phoneSectionList.add(new PhoneSection(prefix + tempUser.getUsername(), phoneList));
        }

        return phoneSectionList;
    }
}
