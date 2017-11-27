package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.database.dao.intangible.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LUserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 31/07/17
 * Time: 16:15
 */

public final class NotificationDAO extends GenericWideDAO<UserPhone> {
    private static NotificationDAO instance;

    @Override
    protected void prepareFields() {
        node = userNode + "/phones";
    }

    private NotificationDAO() {}

    public static NotificationDAO get() {
        if (instance == null) instance = new NotificationDAO();
        return instance;
    }

    @Override
    public NotificationDAO create(@NonNull final UserPhone userPhone,
                                  @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final ArrayList<String> keyList = new ArrayList<>();

                    UserFollowerDAO.get().getUserKeyList(
                            userPhone.getUserKey(), UserFollowerDAO.NODE_FOLLOWERS,

                            new Listeners.ListListener<String>() {
                                @Override
                                public void onItemAdded(@NonNull String key) {
                                    keyList.add(key);
                                }

                                @Override public void onItemRemoved(@NonNull String key) {}
                            },

                            new Listeners.AnswerListener() {
                                @Override
                                public void onAnswerRetrieved() {
                                    if (keyList.isEmpty()) {
                                        listener.onAnswerRetrieved();
                                    } else {
                                        sendPhoneToFollowers(keyList, userPhone, listener);
                                    }
                                }

                                @Override
                                public void onError() {
                                    listener.onError();
                                }
                            }
                    );

                }
                listener.onError();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    private NotificationDAO sendPhoneToFollowers(@NonNull final ArrayList<String> keyList,
                                                 @NonNull final UserPhone userPhone,
                                                 @NonNull final Listeners.AnswerListener listener) {

        for (int i = 0; i < keyList.size(); i++) {

            final Integer thisPosition = i;
            final String followerNode = "users/" + keyList.get(thisPosition);

            isNodeValid(followerNode, new Listeners.ObjectListener<Boolean>() {
                @Override
                public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                    if (retrievedBoolean) {

                        final DatabaseReference innerReference = DAOHandler
                                .getFirebaseDatabase(followerNode + "/phones");

                        final Query query = innerReference.orderByChild("phoneKey")
                                .equalTo(userPhone.getPhoneKey());

                        if (thisPosition == keyList.size() - 1) {
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot == null || dataSnapshot.getChildren() == null ||
                                            !dataSnapshot.getChildren().iterator().hasNext()) {
                                        // phone doesn't exist
                                        innerReference.push().setValue(userPhone);
                                    }

                                    // last query
                                    listener.onAnswerRetrieved();
                                    query.removeEventListener(this);
                                }

                                @Override public void onCancelled(DatabaseError databaseError) {}
                            });

                        } else {

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot == null || dataSnapshot.getChildren() == null ||
                                            !dataSnapshot.getChildren().iterator().hasNext()) {
                                        // phone doesn't exist
                                        innerReference.push().setValue(userPhone);
                                    }

                                    query.removeEventListener(this);
                                }

                                @Override public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                    }
                }

                @Override
                public void onError() {
                    listener.onError();
                }
            });
        }

        return instance;
    }

    @Override @Deprecated
    public NotificationDAO update(@NonNull UserPhone userPhone,
                                  @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public NotificationDAO delete(@NonNull final String phoneKey,
                                  @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final Query query = getReference().orderByChild("phoneKey").equalTo(phoneKey);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            getReference().child(dataSnapshot.getKey()).removeValue()
                                    .addOnFailureListener(e -> listener.onError())
                                    .addOnSuccessListener(aVoid -> {
                                        LUserPhoneDAO.get().delete(null, phoneKey);
                                        listener.onAnswerRetrieved();
                                        query.removeEventListener(this);
                                    });
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

    @Override @Deprecated
    public NotificationDAO delete(@NonNull final String notifiedKey,
                                  @NonNull final String notifyingKey,
                                  @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call method with " +
                "one parameter instead.");
    }

    @Override @Deprecated
    public NotificationDAO sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    public NotificationDAO getNotificationList(@NonNull final Listeners.ListListener<UserPhone> listListener,
                                               @NonNull final Listeners.AnswerListener answerListener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final Query query = getReference().orderByChild("isNotification").equalTo(true);
                    final Integer[] listHandler = {0, 0};

                    final ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            listHandler[0]++;
                            mutualAction(dataSnapshot, true);
                        }

                        @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                            listHandler[0]--;
                            mutualAction(dataSnapshot, false);
                        }

                        private void mutualAction(DataSnapshot dataSnapshot, Boolean isAdding) {
                            final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);

                            userPhone.getUser(new Listeners.ObjectListener<User>() {
                                @Override
                                public void onObjectRetrieved(@NonNull User retrievedObject) {
                                    userPhone.getPhone(new Listeners.ObjectListener<Phone>() {
                                        @Override
                                        public void onObjectRetrieved(@NonNull Phone retrievedObject) {
                                            if (isAdding) {
                                                listListener.onItemAdded(userPhone);
                                            } else {
                                                listListener.onItemRemoved(userPhone);
                                            }

                                            if (++listHandler[1] == listHandler[0]) {
                                                answerListener.onAnswerRetrieved();
                                            }
                                        }

                                        @Override public void onError() {}
                                    });
                                }

                                @Override public void onError() {}
                            });
                        }

                        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    };

                    query.addChildEventListener(childEventListener);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (listHandler[0] == 0) {
                                answerListener.onAnswerRetrieved();
                                query.removeEventListener(childEventListener);
                                query.removeEventListener(this);
                            }
                        }

                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });
                }
            }

            @Override
            public void onError() {
                answerListener.onError();
            }
        });

        return instance;
    }

    public NotificationDAO add(@NonNull final String phoneKey,
                               @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final Query query = getReference().orderByChild("phoneKey").equalTo(phoneKey);

                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            getReference().child(dataSnapshot.getKey())
                                    .child("isNotification").setValue(false);
                            listener.onAnswerRetrieved();
                        }

                        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });

                } else listener.onAnswerRetrieved();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }
}
