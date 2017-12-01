package com.hugocastelani.blockbook.database.dao.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.dao.intangible.GenericLocalDAO;
import com.hugocastelani.blockbook.database.domain.Phone;
import com.hugocastelani.blockbook.database.domain.Relationship;
import com.hugocastelani.blockbook.database.domain.User;
import com.hugocastelani.blockbook.domain.PhoneSection;
import com.hugocastelani.blockbook.util.Listeners;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 23:14
 */

public final class LRelationshipDAO extends GenericLocalDAO<Relationship> {
    private static LRelationshipDAO instance;

    public static final String USER_FOLLOWING_KEY = "user_following_key";
    public static final String USER_FOLLOWER_KEY = "user_follower_key";

    @Override
    protected void prepareFields() {
        table = "relationship";
    }

    private LRelationshipDAO() {}

    public static LRelationshipDAO get() {
        if (instance == null) instance = new LRelationshipDAO();
        return instance;
    }

    @Override
    public Relationship generate(@NonNull final Cursor cursor) {
        return new Relationship(
                cursor.getString(cursor.getColumnIndex(USER_FOLLOWING_KEY)),
                cursor.getString(cursor.getColumnIndex(USER_FOLLOWER_KEY))
        );
    }

    @Override
    public LRelationshipDAO create(@NonNull final Relationship relationship) {
        if (findByIDs(relationship.getFollowingKey(), relationship.getFollowerKey()) == null) {

            LUserDAO.get().cloneUser(relationship.getFollowingUser(null));    // user was already set
            ContentValues values = new ContentValues();

            values.put(USER_FOLLOWING_KEY, relationship.getFollowingKey());
            values.put(USER_FOLLOWER_KEY, relationship.getFollowerKey());

            DAOHandler.getLocalDatabase().insert(table, null, values);
        }
        return instance;
    }

    @Override @Deprecated
    public LRelationshipDAO update(@NonNull Relationship relationship) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public LRelationshipDAO delete(@NonNull final String addedKey) {
        DAOHandler.getLocalDatabase().delete(
                table, USER_FOLLOWING_KEY + " = ? AND " + USER_FOLLOWER_KEY + " = ?",
                new String[] {addedKey, LUserDAO.get().getThisUserKey()});
        return instance;
    }

    @Override @Deprecated
    public String exists(@NonNull Relationship relationship) {
        throw new UnsupportedOperationException("You shouldn't do this. Call findByKeys()" +
                " method instead.");
    }

    @Override
    public GenericLocalDAO<Relationship> sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }

    public Relationship findByIDs(@NonNull final String followedKey,
                                  @NonNull final String followerKey) {

        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_FOLLOWING_KEY + " = ? AND " + USER_FOLLOWER_KEY + " = ?",
                new String[] {followedKey, followerKey}, null, null, null);

        Relationship relationship = null;
        if (cursor.moveToFirst()) relationship = generate(cursor);

        return relationship;
    }

    public ArrayList<User> findUserFriends(@NonNull final String key) {
        final Cursor cursor = DAOHandler.getLocalDatabase().query(table, null,
                USER_FOLLOWER_KEY + " = ?", new String[] {key}, null, null, null);

        final ArrayList<User> friendList = new ArrayList<>();
        while (cursor.moveToNext()) {
            friendList.add(LUserDAO.get().findByColumn(LUserDAO.key,
                    cursor.getString(cursor.getColumnIndex(USER_FOLLOWING_KEY))
            ));
        }

        return friendList;
    }

    public ArrayList<PhoneSection> getFriendsBlockedList(@NonNull final String key) {
        ArrayList<User> friendList = findUserFriends(key);
        ArrayList<PhoneSection> phoneSectionList = new ArrayList<>();

        final String prefix = DAOHandler.getContext().getResources().getString(R.string.numbers_prefix);
        for (int i = 0; i < friendList.size(); i++) {
            final User tempUser = friendList.get(i);
            final ArrayList<Phone> phoneList = LUserPhoneDAO.get().getPhoneList(tempUser.getKey());

            phoneSectionList.add(new PhoneSection(prefix + tempUser.getUsername(), phoneList));
        }

        return phoneSectionList;
    }
}
