package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LFriendshipDAO;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.abstracts.GenericWideDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 21:57
 */

public class FriendshipDAO extends GenericWideDAO<Friendship> {
    private static FriendshipDAO instance;

    @Override
    protected void prepareFields() {
        node = "friendships";
    }

    private FriendshipDAO() {}

    public static FriendshipDAO get() {
        if (instance == null) instance = new FriendshipDAO();
        return instance;
    }

    @Override
    public FriendshipDAO create(@NonNull final Friendship friendship) {
        exists(friendship, retrievedBoolean -> {
            if (!retrievedBoolean) {
                getReference().push().setValue(friendship);
            }
            LFriendshipDAO.get().create(friendship);
        });

        return instance;
    }

    @Override
    public GenericWideDAO update(@NonNull Friendship friendship) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public FriendshipDAO delete(@NonNegative final Integer addedID, @NonNegative final Integer addingID) {
        getReference().orderByChild("addedIDAddingID")
                .equalTo(String.valueOf(addedID) + addingID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue();
                        LFriendshipDAO.get().delete(addedID, addingID);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        return instance;
    }

    public FriendshipDAO exists(@NonNull final Friendship friendship,
                              @NonNull final UserPhoneDAO.BooleanListener listener) {
        getReference().orderByChild("addedIDAddingID")
                .equalTo(friendship.getAddedIDAddingID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                dataSnapshot.getChildren().iterator().hasNext()) {
                            listener.onBooleanRetrieved(true);

                        } else {

                            listener.onBooleanRetrieved(false);
                        }
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        return instance;
    }

    public FriendshipDAO sync(@NonNull final DAOHandler.AnswerListener listener) {
        final Query query = getReference().orderByChild("addingID")
                .equalTo(LUserDAO.get().getThisUser().getID());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onAnswerRetrieved();
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                UserDAO.get().findByID(
                        dataSnapshot.getValue(Friendship.class).getAddedID(),
                        retrievedUser -> LUserDAO.get().clone(retrievedUser)
                );
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        return instance;
    }

    public FriendshipDAO findUserFriends(@NonNegative final Integer userID,
                                       @NonNull final UserPhoneDAO.ListListener listener) {
        getReference().orderByChild("addingID")
                .equalTo(userID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserDAO.get().findByID(
                                dataSnapshot.getValue(Friendship.class).getAddedID(),
                                retrievedUser -> listener.onItemAdded(retrievedUser)
                        );
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        return instance;
    }

    public FriendshipDAO getFriendsBlockedList(@NonNegative final Integer userID,
                                             @NonNull final UserPhoneDAO.ListListener listListener,
                                             @NonNull final DAOHandler.AnswerListener answerListener) {
        findUserFriends(userID, new UserPhoneDAO.ListListener<User>() {
            @Override
            public void onItemAdded(@NonNull User user) {
                UserPhoneDAO.get().getUserPhoneList(user.getID(), listListener, answerListener);
            }

            @Override public void onItemRemoved(@NonNull User user) {}
        });

        return instance;
    }
}
