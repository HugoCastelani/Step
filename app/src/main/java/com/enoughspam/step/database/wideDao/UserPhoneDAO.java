package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:30
 */

public class UserPhoneDAO {
    private static final String NODE = "userPhones";
    private static DatabaseReference sDatabase;

    private UserPhoneDAO() {}

    private static DatabaseReference getDatabase() {
        if (sDatabase == null) {
            sDatabase = DAOHandler.getFirebaseDatabase(NODE);
        }
        return sDatabase;
    }

    public static void create(@NonNull final UserPhone userPhone) {
        PhoneDAO.create(userPhone.getPhone(), retrievedPhone -> {
            userPhone.setPhoneID(retrievedPhone.getID());
            exists(userPhone, retrievedBoolean -> {
                if (!retrievedBoolean) {
                    getDatabase().push().setValue(userPhone);
                }
                LUserPhoneDAO.create(userPhone);
            });
        });
    }

    private static void exists(@NonNull final UserPhone userPhone, @NonNull final BooleanListener listener) {
        getDatabase().orderByChild("userIDPhoneID")
                .equalTo(userPhone.getUserIDPhoneID())
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
    }

    public static void findByUserID(@NonNegative final int userID, @NonNull final UserPhoneListener listener) {
        getDatabase().orderByChild("userID")
                .equalTo(userID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);
                        listener.onUserPhoneRetrieved(userPhone);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public static void delete(@NonNegative final int userID, @NonNegative final int phoneID) {
        getDatabase().orderByChild("userIDPhoneID")
                .equalTo(String.valueOf(userID) + phoneID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getDatabase().child(dataSnapshot.getKey()).removeValue();
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        LUserPhoneDAO.delete(userID, phoneID);
    }

    public static void getUserPhoneList(@NonNegative final int userID,
                                        @NonNull final ListListener listListener,
                                        @Nullable final DAOHandler.AnswerListener answerListener) {
        UserDAO.findByID(userID, retrievedUser -> {

            final Query query = getDatabase().orderByChild("userID").equalTo(userID);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (answerListener != null) {
                        answerListener.onAnswerRetrieved();
                    }
                }

                @Override public void onCancelled(DatabaseError databaseError) {}
            });

            query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);
                            if (!userPhone.getIsProperty()) {
                                userPhoneGenerator(userPhone, retrievedUserPhone ->
                                        listListener.onItemAdded(retrievedUserPhone)
                                );
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            final UserPhone userPhone = dataSnapshot.getValue(UserPhone.class);
                            if (!userPhone.getIsProperty()) {
                                userPhoneGenerator(userPhone, retrievedUserPhone ->
                                        listListener.onItemRemoved(retrievedUserPhone)
                                );
                            }
                        }

                        private void userPhoneGenerator(@NonNull final UserPhone userPhone,
                                                        @NonNull final UserPhoneListener userPhoneListener) {
                            PhoneDAO.findByID(userPhone.getPhoneID(), retrievedPhone ->
                                    userPhoneListener.onUserPhoneRetrieved(
                                            new UserPhone(retrievedUser, retrievedPhone, userPhone.getIsProperty())
                                    )
                            );
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

        });
    }

    public interface UserPhoneListener {
        void onUserPhoneRetrieved(@NonNull final UserPhone retrievedUserPhone);
    }

    public interface ListListener<T> {
        void onItemAdded(@NonNull final T item);
        void onItemRemoved(@NonNull final T item);
    }

    public interface BooleanListener {
        void onBooleanRetrieved(final boolean retrievedBoolean);
    }
}
