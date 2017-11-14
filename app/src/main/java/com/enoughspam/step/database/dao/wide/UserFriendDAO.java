package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.abstracts.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LFriendshipDAO;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 01/11/17
 * Time: 15:46
 */

public class UserFriendDAO extends GenericWideDAO<User> {
    private static UserFriendDAO instance;

    private UserFriendDAO() {}

    @Override
    protected void prepareFields() {
        node = userNode + "/friends";
    }

    public static UserFriendDAO get() {
        if (instance == null) {
            instance = new UserFriendDAO();
        }
        return instance;
    }

    @Override
    public UserFriendDAO create(@NonNull final User user,
                                @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, isValid -> {
            if (isValid) {

                exists(user, doesExist -> {

                    if (!doesExist) {
                        getReference().push().setValue(new InnerUser(user.getKey()))
                                .addOnFailureListener(e -> listener.onError())
                                .addOnSuccessListener(aVoid -> {
                                    final User thisUser = LUserDAO.get().getThisUser();
                                    final Friendship friendship = new Friendship(user.getKey(),
                                            thisUser.getKey());

                                    friendship.setAddedUser(user);
                                    friendship.setAddingUser(thisUser);

                                    LFriendshipDAO.get().create(friendship);

                                    UserPhoneDAO.get().createOfUser(user, listener);
                                });
                    }

                });

            } else listener.onError();
        });

        return instance;
    }

    @Override @Deprecated
    public UserFriendDAO update(@NonNull User user,
                                @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public UserFriendDAO delete(@NonNull final String key,
                                @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("friendKey").equalTo(key);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue()
                                .addOnFailureListener(e -> listener.onError())
                                .addOnSuccessListener(aVoid ->
                                        LFriendshipDAO.get().delete(key)
                                );
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                };

                query.addChildEventListener(childEventListener);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserPhoneDAO.get().deleteOfUser(key, listener);
                        query.removeEventListener(childEventListener);
                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            } else listener.onError();
        });

        return instance;
    }

    @Override @Deprecated
    public UserFriendDAO delete(@NonNull String key1, @NonNull String key2,
                                @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call delete() method " +
                "with listener instead.");
    }

    public UserFriendDAO exists(@NonNull final User friend,
                                @NonNull final Listeners.BooleanListener listener) {

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("friendKey").equalTo(friend.getKey());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                dataSnapshot.getChildren().iterator().hasNext()) {
                            listener.onBooleanRetrieved(true);
                        } else {
                            listener.onBooleanRetrieved(false);
                        }

                        query.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        exists(friend, listener);
                    }
                });

            }
        });

        return instance;
    }

    @Override
    public UserFriendDAO sync(@NonNull Listeners.AnswerListener listener) {
        // LETS WORK THIS OUT
        return instance;
    }

    public UserFriendDAO getUserFriendList(@NonNull final String userKey,
                                           @NonNull final Listeners.ListListener<String> listListener,
                                           @NonNull final Listeners.AnswerListener answerListener) {

        final String userNode = "users/" + userKey;

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = DAOHandler.getFirebaseDatabase(userNode).orderByChild("userKey");

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        listListener.onItemAdded(
                                dataSnapshot.getValue(InnerUser.class).getFriendKey()
                        );
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                };

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        answerListener.onAnswerRetrieved();
                        query.removeEventListener(childEventListener);
                        query.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });

                query.addChildEventListener(childEventListener);

            } else answerListener.onError();
        });

        return instance;
    }

    private static final class InnerUser {
        private String friendKey;

        public InnerUser() {}

        public InnerUser(@NonNull final String friendKey) {
            this.friendKey = friendKey;
        }

        public String getFriendKey() {
            return friendKey;
        }

        public void setFriendKey(@NonNull final String friendKey) {
            this.friendKey = friendKey;
        }
    }
}