package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.abstracts.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LRelationshipDAO;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.domain.Relationship;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 01/11/17
 * Time: 15:46
 */

public class UserFollowerDAO extends GenericWideDAO<User> {
    private static UserFollowerDAO instance;

    public static final String NODE_FOLLOWERS = "/followers";
    public static final String NODE_FOLLOWING = "/following";

    private UserFollowerDAO() {}

    @Override
    protected void prepareFields() {
        node = userNode + NODE_FOLLOWING;
    }

    public static UserFollowerDAO get() {
        if (instance == null) {
            instance = new UserFollowerDAO();
        }
        return instance;
    }

    @Override
    public UserFollowerDAO create(@NonNull final User targetUser,
                                  @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, isValid -> {
            if (isValid) {

                exists(targetUser, doesExist -> {

                    if (!doesExist) {
                        final Listeners.AnswerListener successListener = new Listeners.AnswerListener() {
                            Integer count = 0;

                            @Override
                            public void onAnswerRetrieved() {
                                if (++count == 2) {
                                    final User thisUser = LUserDAO.get().getThisUser();
                                    final Relationship relationship = new Relationship(targetUser.getKey(),
                                            thisUser.getKey());

                                    relationship.setFollowingUser(targetUser);
                                    relationship.setFollowerUser(thisUser);

                                    LRelationshipDAO.get().create(relationship);

                                    UserPhoneDAO.get().createOfUser(targetUser, listener);
                                }
                            }

                            @Override
                            public void onError() {
                                listener.onError();
                            }
                        };

                        getReference().push().setValue(new InnerUser(targetUser.getKey()))
                                .addOnFailureListener(e -> listener.onError())
                                .addOnSuccessListener(aVoid ->
                                    successListener.onAnswerRetrieved()
                                );

                        // ADD YOUR KEY TO OTHER USER
                        DAOHandler.getFirebaseDatabase("users/" + targetUser.getKey() + NODE_FOLLOWERS)
                                .push().setValue(new InnerUser(LUserDAO.get().getThisUserKey()))
                                .addOnFailureListener(e -> listener.onError())
                                .addOnSuccessListener(aVoid ->
                                        successListener.onAnswerRetrieved()
                                );

                    } else {

                        listener.onAnswerRetrieved();
                    }

                });

            } else listener.onError();
        });

        return instance;
    }

    @Override @Deprecated
    public UserFollowerDAO update(@NonNull User user,
                                  @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public UserFollowerDAO delete(@NonNull String targetKey,
                                  @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("userKey").equalTo(targetKey);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue()
                                .addOnFailureListener(e -> listener.onError())
                                .addOnSuccessListener(aVoid ->
                                        LRelationshipDAO.get().delete(targetKey)
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
                        UserPhoneDAO.get().deleteOfUser(targetKey, listener);
                        query.removeEventListener(childEventListener);
                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            } else listener.onError();
        });

        return instance;
    }

    @Override
    public UserFollowerDAO delete(@NonNull String key1, @NonNull String key2,
                                  @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call method with two " +
                "methods instead.");
    }

    public UserFollowerDAO deleteAllOfUser(@NonNull final String thisUserKey,
                                           @NonNull final Listeners.AnswerListener listener) {

        final Listeners.AnswerListener answerListener = new Listeners.AnswerListener() {
            Integer count = 0;

            @Override
            public void onAnswerRetrieved() {
                if (++count == 2) {
                    listener.onAnswerRetrieved();
                }
            }

            @Override
            public void onError() {
                listener.onError();
            }
        };

        final Listeners.ListListener<String> listListener = new Listeners.ListListener<String>() {
            @Override
            public void onItemAdded(@NonNull String userKey) {
                final String thisUserNode = "users/" + userKey;
                isNodeValid(thisUserNode, retrievedBoolean -> {
                    if (retrievedBoolean) {

                        final Query query = DAOHandler.getFirebaseDatabase(thisUserNode)
                                .orderByChild("userKey").equalTo(thisUserKey);

                        final ChildEventListener childEventListener = new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                final String key = dataSnapshot.getRef().toString()
                                        .replaceFirst("https://enough-spam-ed0f2.firebaseio.com/", "");

                                DAOHandler.getFirebaseDatabase(key).removeValue();
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
                                listener.onAnswerRetrieved();
                                query.removeEventListener(childEventListener);
                                query.removeEventListener(this);
                            }

                            @Override public void onCancelled(DatabaseError databaseError) {}
                        });

                    } else listener.onError();
                });
            }

            @Override public void onItemRemoved(@NonNull String userKey) {}
        };

        // remove all of this user from followers and following
        getUserKeyList(thisUserKey, NODE_FOLLOWERS, listListener, answerListener);
        getUserKeyList(thisUserKey, NODE_FOLLOWING, listListener, answerListener);

        return instance;
    }

    public UserFollowerDAO exists(@NonNull final User friend,
                                  @NonNull final Listeners.BooleanListener listener) {

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("userKey").equalTo(friend.getKey());

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
    public UserFollowerDAO sync(@NonNull Listeners.AnswerListener listener) {
        // LETS WORK THIS OUT
        return instance;
    }

    /**
     * @param userKey
     * @param node
     * @param listListener
     * @param answerListener
     * @return list of all followers/following
     */
    public UserFollowerDAO getUserKeyList(@NonNull final String userKey, @NonNull final String node,
                                          @NonNull final Listeners.ListListener<String> listListener,
                                          @NonNull final Listeners.AnswerListener answerListener) {

        String userNode = "users/" + userKey;

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = DAOHandler.getFirebaseDatabase(userNode + node).orderByKey();

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        listListener.onItemAdded(
                                dataSnapshot.getValue(InnerUser.class).getUserKey()
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

    public UserFollowerDAO getUserList(@NonNull final String userKey, @NonNull final String node,
                                       @NonNull final Listeners.ListListener<User> listListener,
                                       @NonNull final Listeners.AnswerListener answerListener) {

        final int[] completed = new int[] {0};
        final ArrayList<String> keyList = new ArrayList<>();

        getUserKeyList(userKey, node, new Listeners.ListListener<String>() {
            @Override
            public void onItemAdded(@NonNull String userKey) {
                keyList.add(userKey);
            }

            @Override
            public void onItemRemoved(@NonNull String userKey) {
                for (final String key : keyList) {
                    if (key.equals(userKey)) keyList.remove(key);
                }
            }
        }, new Listeners.AnswerListener() {
            @Override public void onAnswerRetrieved() {
                if (!keyList.isEmpty()) {

                    for (final String key : keyList) {

                        UserDAO.get().findByKey(key, new Listeners.UserListener() {
                            @Override
                            public void onUserRetrieved(@NonNull User retrievedUser) {
                                listListener.onItemAdded(retrievedUser);
                                if (++completed[0] == keyList.size()) {
                                    answerListener.onAnswerRetrieved();
                                }
                            }

                            @Override public void onError() {}
                        });

                    }

                } else answerListener.onAnswerRetrieved();
            }
            @Override public void onError() {}
        });

        return instance;
    }

    private static final class InnerUser {
        private String userKey;

        public InnerUser() {}

        public InnerUser(@NonNull final String userKey) {
            this.userKey = userKey;
        }

        public String getUserKey() {
            return userKey;
        }

        public void setUserKey(@NonNull final String userKey) {
            this.userKey = userKey;
        }
    }
}
