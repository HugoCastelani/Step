package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LFriendshipDAO;
import com.enoughspam.step.database.localDao.LPhoneDAO;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.enoughspam.step.database.localDao.abstracts.GenericWideDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:30
 */

public class UserPhoneDAO extends GenericWideDAO<UserPhone> {
    private static UserPhoneDAO instance;

    @Override
    protected void prepareFields() {
        node = "userPhones";
    }

    private UserPhoneDAO() {}

    public static UserPhoneDAO get() {
        if (instance == null) instance = new UserPhoneDAO();
        return instance;
    }

    @Override
    public UserPhoneDAO create(@NonNull final UserPhone userPhone) {
        PhoneDAO.create(userPhone.getPhone(), retrievedPhone -> {
            userPhone.setPhoneID(retrievedPhone.getID());
            exists(userPhone, retrievedBoolean -> {
                if (!retrievedBoolean) {
                    getReference().push().setValue(userPhone);
                }
                LUserPhoneDAO.get().create(userPhone);
            });
        });

        return instance;
    }

    @Override
    public GenericWideDAO update(@NonNull UserPhone userPhone) {
        throw new UnsupportedOperationException("You shouldn't do this");
    }

    @Override
    public UserPhoneDAO delete(@NonNegative final Integer userID, @NonNegative final Integer phoneID) {
        getReference().orderByChild("userIDPhoneID")
                .equalTo(String.valueOf(userID) + phoneID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue();
                        LUserPhoneDAO.get().delete(userID, phoneID);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        return instance;
    }

    public UserPhoneDAO exists(@NonNull final UserPhone userPhone, @NonNull final BooleanListener listener) {
        getReference().orderByChild("userIDPhoneID")
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

        return instance;
    }

    @Override
    // this one can be fucking long
    public UserPhoneDAO sync(@NonNull final DAOHandler.AnswerListener listener) {
        List<User> userList = LFriendshipDAO.get().findUserFriends(
                LUserDAO.get().getThisUser().getID());
        userList.add(0, LUserDAO.get().getThisUser());

        for (int i = 0; i < userList.size(); i++) {
            DAOHandler.AnswerListener answerListener = null;
            if (i == userList.size() - 1) {
                answerListener = () -> listener.onAnswerRetrieved();
            }

            getUserPhoneList(userList.get(i).getID(),
                    new UserPhoneDAO.ListListener<UserPhone>() {
                        @Override
                        public void onItemAdded(@NonNull UserPhone userPhone) {
                            PhoneDAO.findByID(userPhone.getPhoneID(),
                                    retrievedPhone -> LPhoneDAO.get().create(retrievedPhone));
                            create(userPhone);
                        }

                        @Override
                        public void onItemRemoved(@NonNull UserPhone userPhone) {
                            LPhoneDAO.get().delete(userPhone.getPhoneID());
                        }
                    }, answerListener);
        }

        return instance;
    }

    public UserPhoneDAO getUserPhoneList(@NonNegative final Integer userID,
                                        @NonNull final ListListener listListener,
                                        @Nullable final DAOHandler.AnswerListener answerListener) {
        UserDAO.get().findByID(userID, retrievedUser -> {

            final Query query = getReference().orderByChild("userID").equalTo(userID);

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

                        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });

        });

        return instance;
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
