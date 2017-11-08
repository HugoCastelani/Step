package com.enoughspam.step.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.enoughspam.step.R;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.abstracts.GenericLocalDAO;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.util.Listeners;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 23:14
 */

public class LFriendshipDAO extends GenericLocalDAO<Friendship> {
    private static LFriendshipDAO instance;

    public static final String USER_ADDED_KEY = "user_added_key";
    public static final String USER_ADDING_KEY = "user_adding_key";

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
                cursor.getString(cursor.getColumnIndex(USER_ADDED_KEY)),
                cursor.getString(cursor.getColumnIndex(USER_ADDING_KEY))
        );
    }

    @Override
    public LFriendshipDAO create(@NonNull final Friendship friendship) {
        if (findByIDs(friendship.getAddedKey(), friendship.getAddingKey()) == null) {

            LUserDAO.get().clone(friendship.getAddedUser(null));    // user was already set
            ContentValues values = new ContentValues();

            values.put(USER_ADDED_KEY, friendship.getAddedKey());
            values.put(USER_ADDING_KEY, friendship.getAddingKey());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }
        return instance;
    }

    @Override @Deprecated
    public LFriendshipDAO update(@NonNull Friendship friendship) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public LFriendshipDAO delete(@NonNegative final String addedKey) {
        DAOHandler.getLocalDatabase().delete(
                table, USER_ADDED_KEY + " = ? AND " + USER_ADDING_KEY + " = ?",
                new String[] {addedKey, LUserDAO.get().getThisUserKey()});
        return instance;
    }

    @Override @Deprecated
    public String exists(@NonNull Friendship friendship) {
        throw new UnsupportedOperationException("You shouldn't do this. Call findByIDs()" +
                " method instead.");
    }

    @Override
    public GenericLocalDAO<Friendship> sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }

    public Friendship findByIDs(@NonNull final String addedKey, @NonNegative final String addingKey) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_ADDED_KEY + " = ? AND " + USER_ADDING_KEY + " = ?",
                new String[] {addedKey, addingKey}, null, null, null);

        Friendship friendship = null;
        if (cursor.moveToFirst()) friendship = generate(cursor);

        return friendship;
    }

    public ArrayList<User> findUserFriends(@NonNegative final String key) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_ADDING_KEY + " = ?", new String[] {key}, null, null, null);

        final ArrayList<User> friendList = new ArrayList<>();
        while (cursor.moveToNext()) {
            friendList.add(LUserDAO.get().findByColumn(LUserDAO.key,
                    cursor.getString(cursor.getColumnIndex(USER_ADDED_KEY))
            ));
        }

        return friendList;
    }

    public ArrayList<PhoneSection> getFriendsBlockedList(@NonNegative final String key) {
        ArrayList<User> friendList = findUserFriends(key);
        ArrayList<PhoneSection> phoneSectionList = new ArrayList<>();

        final String prefix = DAOHandler.getContext().getResources().getString(R.string.numbers_of);
        for (int i = 0; i < friendList.size(); i++) {
            final User tempUser = friendList.get(i);
            final ArrayList<Phone> phoneList = LUserPhoneDAO.get().getPhoneList(tempUser.getKey());

            phoneSectionList.add(new PhoneSection(prefix + tempUser.getUsername(), phoneList));
        }

        return phoneSectionList;
    }
}
