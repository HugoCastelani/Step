package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.abstracts.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LPhoneDAO;
import com.enoughspam.step.database.dao.local.LUserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:30
 */

public class UserPhoneDAO extends GenericWideDAO<UserPhone> {
    private static UserPhoneDAO instance;

    @Override
    protected void prepareFields() {
        node = userNode + "/phones";
    }

    private UserPhoneDAO() {}

    public static UserPhoneDAO get() {
        if (instance == null) instance = new UserPhoneDAO();
        return instance;
    }

    @Override @Deprecated
    public UserPhoneDAO create(@NonNull final UserPhone userPhone,
                               @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call create() method " +
                "with three parameters instead.");
    }

    public UserPhoneDAO create(@NonNull final UserPhone userPhone,
                               @NonNull final Listeners.UserPhoneAnswerListener listener,
                               @NonNull final boolean force) {
        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                if (force) {
                    delete(userPhone.getPhoneKey(), new Listeners.AnswerListener() {
                        @Override
                        public void onAnswerRetrieved() {
                            getReference().push().setValue(userPhone)
                                    .addOnFailureListener(e -> listener.error())
                                    .addOnSuccessListener(aVoid ->
                                        LUserPhoneDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                            @Override
                                            public void onAnswerRetrieved() {
                                                listener.properlyAdded();
                                            }

                                            @Override
                                            public void onError() {
                                                listener.error();
                                            }
                                        }, force)
                                    );
                        }

                        @Override
                        public void onError() {
                            listener.error();
                        }
                    });

                } else {

                    exists(userPhone, new Listeners.UserPhoneAnswerListener() {
                        @Override
                        public void alreadyAdded() {
                            LUserPhoneDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                @Override
                                public void onAnswerRetrieved() {
                                    listener.alreadyAdded();
                                }

                                @Override
                                public void onError() {
                                    listener.error();
                                }
                            }, false);
                        }

                        @Override
                        public void properlyAdded() {
                            getReference().push().setValue(userPhone)
                                    .addOnFailureListener(e -> listener.error())
                                    .addOnSuccessListener(aVoid ->
                                        LUserPhoneDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                            @Override
                                            public void onAnswerRetrieved() {
                                                listener.properlyAdded();
                                            }

                                            @Override
                                            public void onError() {
                                                listener.error();
                                            }
                                        }, force)
                                    );
                        }

                        @Override
                        public void error() {
                            listener.error();
                        }
                    });
                }

            } else listener.error();
        });

        return instance;
    }

    public UserPhoneDAO createOfUser(@NonNull final User follower,
                                     @NonNull final Listeners.AnswerListener listener) {

        final String followerNode = "users/" + follower.getKey() + "/phones";
        isNodeValid(followerNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                // get only his own numbers
                final Query query = DAOHandler.getFirebaseDatabase(followerNode)
                        .orderByChild("userKey").equalTo(follower.getKey());

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);

                        create(userPhone, new Listeners.UserPhoneAnswerListener() {
                            @Override public void alreadyAdded() {}
                            @Override public void properlyAdded() {}
                            @Override public void error() {}
                        }, false);
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

            }
        });

        return instance;
    }

    @Override
    public GenericWideDAO update(@NonNull UserPhone userPhone,
                                 @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public UserPhoneDAO delete(@NonNull final String phoneKey,
                               @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("phoneKey").equalTo(phoneKey);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue()
                                .addOnFailureListener(e -> listener.onError())
                                .addOnSuccessListener(aVoid -> {
                                    LUserPhoneDAO.get().delete(null, phoneKey);
                                    listener.onAnswerRetrieved();
                                });
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
                        query.removeEventListener(childEventListener);
                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            } else listener.onError();
        });

        return instance;
    }

    private UserPhoneDAO deletePhoneFromFollowers() {

        return instance;
    }

    @Override
    public UserPhoneDAO delete(@NonNull String key1, @NonNull String key2,
                               @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Use method with one " +
                "parameter instead.");
    }

    public UserPhoneDAO deleteOfUser(@NonNull final String userKey,
                                     @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("userKey").equalTo(userKey);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue()
                                .addOnSuccessListener(aVoid ->
                                    LUserPhoneDAO.get().delete(userKey, null)
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
                        listener.onAnswerRetrieved();
                        query.removeEventListener(childEventListener);
                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

                query.addChildEventListener(childEventListener);

            } else listener.onError();
        });

        return instance;
    }

    public UserPhoneDAO exists(@NonNull final UserPhone userPhone,
                               @NonNull final Listeners.UserPhoneAnswerListener listener) {
        final Query query = getReference().orderByChild("phoneKey").equalTo(userPhone.getPhoneKey());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                    dataSnapshot.getChildren().iterator().hasNext()) {
                    listener.alreadyAdded();

                } else {

                    listener.properlyAdded();
                }

                query.removeEventListener(this);
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        return instance;
    }

    @Override
    public UserPhoneDAO sync(@NonNull final Listeners.AnswerListener listener) {
        Query query = getReference().orderByKey();

        final Integer[] waitingFor = new Integer[] {0, 0};

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);

                waitingFor[0]++;
                userPhone.getPhone(new Listeners.PhoneListener() {
                    @Override
                    public void onPhoneRetrieved(@NonNull Phone retrievedPhone) {
                        LPhoneDAO.get().create(retrievedPhone);
                        LUserPhoneDAO.get().create(userPhone, new Listeners.AnswerListener() {
                            @Override
                            public void onAnswerRetrieved() {
                                if (++waitingFor[1] == waitingFor[0]) {
                                    listener.onAnswerRetrieved();
                                }
                            }

                            @Override public void onError() {}
                        }, false);
                    }

                    @Override public void onError() {}
                });
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        return instance;
    }

    public UserPhoneDAO getUserPhoneList(@NonNull final String userKey,
                                         @NonNull final Listeners.ListListener listListener,
                                         @NonNull final Listeners.AnswerListener answerListener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = DAOHandler.getFirebaseDatabase("users/" + userKey + "/phones")
                        .orderByChild("isNotification").equalTo(false);

                final Integer[] waitingFor = new Integer[] {0, 0};

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);
                        if (!userPhone.getIsProperty()) {

                            waitingFor[0]++;
                            userPhone.getUser(new Listeners.UserListener() {
                                @Override
                                public void onUserRetrieved(@NonNull User retrievedUser) {

                                    userPhone.getPhone(new Listeners.PhoneListener() {
                                        @Override
                                        public void onPhoneRetrieved(@NonNull Phone retrievedPhone) {
                                            listListener.onItemAdded(userPhone);
                                            if (waitingFor[0] == ++waitingFor[1]) {
                                                answerListener.onAnswerRetrieved();
                                            }
                                        }

                                        @Override public void onError() {}
                                    });

                                }

                                @Override public void onError() {}
                            });

                        }
                    }

                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                };

                query.addChildEventListener(childEventListener);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (waitingFor[0] == 0) {
                            answerListener.onAnswerRetrieved();
                            query.removeEventListener(childEventListener);
                            query.removeEventListener(this);
                        }
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            } else answerListener.onError();
        });

        return instance;
    }

}
