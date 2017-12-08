package com.hugocastelani.blockbook.database.dao.wide;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.dao.intangible.GenericWideDAO;
import com.hugocastelani.blockbook.database.dao.local.LPhoneDAO;
import com.hugocastelani.blockbook.database.dao.local.LUserDAO;
import com.hugocastelani.blockbook.database.dao.local.LUserPhoneDAO;
import com.hugocastelani.blockbook.database.domain.Phone;
import com.hugocastelani.blockbook.database.domain.User;
import com.hugocastelani.blockbook.database.domain.UserPhone;
import com.hugocastelani.blockbook.util.Listeners;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:30
 */

public final class UserPhoneDAO extends GenericWideDAO<UserPhone> {
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

        isNodeValid(userNode, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    if (force) {
                        delete(LUserDAO.get().getThisUserKey(), userPhone.getPhoneKey(), false,
                                new Listeners.AnswerListener() {
                                    @Override
                                    public void onAnswerRetrieved() {
                                        getReference().push().setValue(userPhone)
                                                .addOnFailureListener(e -> listener.error())
                                                .addOnSuccessListener(aVoid ->
                                                        NotificationDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                                            @Override
                                                            public void onAnswerRetrieved() {
                                                                LUserPhoneDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                                                    @Override
                                                                    public void onAnswerRetrieved() {
                                                                        listener.properlyAdded();
                                                                    }

                                                                    @Override
                                                                    public void onError() {
                                                                        listener.error();
                                                                    }
                                                                }, force);
                                                            }

                                                            @Override public void onError() {}
                                                        })
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
                                            NotificationDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                                @Override
                                                public void onAnswerRetrieved() {
                                                    LUserPhoneDAO.get().create(userPhone, new Listeners.AnswerListener() {
                                                        @Override
                                                        public void onAnswerRetrieved() {
                                                            listener.properlyAdded();
                                                        }

                                                        @Override
                                                        public void onError() {
                                                            listener.error();
                                                        }
                                                    }, force);
                                                }

                                                @Override public void onError() {}
                                            })
                                        );
                            }

                            @Override
                            public void error() {
                                listener.error();
                            }
                        });
                    }

                } else listener.error();
            }

            @Override
            public void onError() {
                listener.error();
            }
        });

        return instance;
    }

    public UserPhoneDAO createOfUser(@NonNull final User follower,
                                     @NonNull final Listeners.AnswerListener listener) {

        final String followerNode = "users/" + follower.getKey() + "/phones";

        isNodeValid(followerNode, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
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

                } else listener.onError();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    @Override
    public GenericWideDAO update(@NonNull UserPhone userPhone,
                                 @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public UserPhoneDAO delete(@NonNull final String phoneKey,
                               @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Use method with four " +
                "parameter instead.");
    }

    @Override @Deprecated
    public UserPhoneDAO delete(@NonNull final String userKey, @NonNull final String phoneKey,
                               @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Use method with four " +
                "parameter instead.");
    }

    public UserPhoneDAO delete(@NonNull final String userKey, @NonNull final String phoneKey,
                               @NonNull final Boolean cascadeCall, @NonNull final Listeners.AnswerListener listener) {

        final StringBuilder nodeBuilder = new StringBuilder();
        nodeBuilder.append("users/").append(userKey);

        isNodeValid(nodeBuilder.toString(), new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    nodeBuilder.append("/phones");

                    isNodeValid(nodeBuilder.toString(), new Listeners.ObjectListener<Boolean>() {
                        @Override
                        public void onObjectRetrieved(@NonNull Boolean retrievedObject) {
                            if (retrievedObject) {

                                final DatabaseReference thisReference = DAOHandler.getFirebaseDatabase(nodeBuilder.toString());
                                final Query query = thisReference.orderByChild("phoneKey").equalTo(phoneKey);

                                final ChildEventListener childEventListener = new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        final Boolean belongThisUser = dataSnapshot.getValue(UserPhone.class)
                                                .getUserKey().equals(userKey);

                                        if (belongThisUser && !cascadeCall) {
                                            deletePhoneFromFollowers(phoneKey, new Listeners.AnswerListener() {
                                                @Override
                                                public void onAnswerRetrieved() {
                                                    mutualAction(dataSnapshot);
                                                }

                                                @Override
                                                public void onError() {
                                                    listener.onError();
                                                }
                                            });
                                        } else {
                                            mutualAction(dataSnapshot);
                                        }
                                    }

                                    private void mutualAction(DataSnapshot dataSnapshot) {
                                        thisReference.child(dataSnapshot.getKey()).removeValue()
                                                .addOnFailureListener(e -> listener.onError())
                                                .addOnSuccessListener(aVoid -> {
                                                    if (!cascadeCall) {
                                                        LUserPhoneDAO.get().delete(null, phoneKey);
                                                    }
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

                            } else listener.onAnswerRetrieved();
                        }

                        @Override
                        public void onError() {
                            listener.onError();
                        }
                    });

                } else listener.onError();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    private UserPhoneDAO deletePhoneFromFollowers(@NonNull final String phoneKey,
                                                  @NonNull final Listeners.AnswerListener listener) {

        final ArrayList<String> keyList = new ArrayList<>();

        UserFollowerDAO.get().getUserKeyList(LUserDAO.get().getThisUserKey(),

                UserFollowerDAO.NODE_FOLLOWERS, new Listeners.ListListener<String>() {
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
                },

                new Listeners.AnswerListener() {
                    @Override
                    public void onAnswerRetrieved() {
                        if (!keyList.isEmpty()) {

                            final Integer[] done = {0};

                            for (final String userKey : keyList) {
                                delete(userKey, phoneKey, true, new Listeners.AnswerListener() {
                                    @Override
                                    public void onAnswerRetrieved() {
                                        if (++done[0] == keyList.size()) {
                                            listener.onAnswerRetrieved();
                                        }
                                    }

                                    @Override public void onError() {
                                        listener.onError();
                                    }
                                });
                            }

                        } else listener.onAnswerRetrieved();
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                }
        );

        return instance;
    }

    public UserPhoneDAO deleteOfUser(@NonNull final String targetUserKey,
                                     @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final Query query = getReference().orderByChild("userKey").equalTo(targetUserKey);

                    final ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            getReference().child(dataSnapshot.getKey()).removeValue()
                                    .addOnSuccessListener(aVoid ->
                                        LUserPhoneDAO.get().delete(targetUserKey, null)
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
                            listener.onAnswerRetrieved();
                            query.removeEventListener(childEventListener);
                            query.removeEventListener(this);
                        }

                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });

                } else listener.onError();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    public UserPhoneDAO exists(@NonNull final UserPhone userPhone,
                               @NonNull final Listeners.UserPhoneAnswerListener listener) {

        isNodeValid(userNode, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

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

                } else listener.error();
            }

            @Override
            public void onError() {
                listener.error();
            }
        });

        return instance;
    }

    @Override
    public UserPhoneDAO sync(@NonNull final Listeners.AnswerListener listener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedObject) {
                if (retrievedObject) {

                    Query query = getReference().orderByKey();

                    final Integer[] waitingFor = new Integer[] {0, 0};

                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);

                            waitingFor[0]++;
                            userPhone.getPhone(new Listeners.ObjectListener<Phone>() {
                                @Override
                                public void onObjectRetrieved(@NonNull Phone retrievedPhone) {
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

                }
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    public UserPhoneDAO getUserPhoneList(@NonNull final String userKey,
                                         @NonNull final Listeners.ListListener listListener,
                                         @NonNull final Listeners.AnswerListener answerListener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedObject) {
                if (retrievedObject) {

                    final Query query = DAOHandler.getFirebaseDatabase("users/" + userKey + "/phones")
                            .orderByChild("isNotification").equalTo(false);

                    final Integer[] waitingFor = new Integer[] {0, 0};

                    final ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);
                            if (!userPhone.getIsProperty()) {

                                waitingFor[0]++;
                                userPhone.getUser(new Listeners.ObjectListener<User>() {
                                    @Override
                                    public void onObjectRetrieved(@NonNull User retrievedUser) {

                                        userPhone.getPhone(new Listeners.ObjectListener<Phone>() {
                                            @Override
                                            public void onObjectRetrieved(@NonNull Phone retrievedPhone) {
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
            }

            @Override
            public void onError() {
                answerListener.onError();
            }
        });

        return instance;
    }

}
