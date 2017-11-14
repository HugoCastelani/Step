package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.abstracts.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LUserPhoneDAO;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 31/07/17
 * Time: 16:15
 */

public class NotificationDAO extends GenericWideDAO<UserPhone> {
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

        isNodeValid(userNode, retrievedBoolean -> {
            if (retrievedBoolean) {

                final ArrayList<String> keyList = new ArrayList<>();

                UserFriendDAO.get().getUserFriendList(
                        userPhone.getUserKey(),

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
                                sendPhoneToFriends(keyList, userPhone, listener);
                            }

                            @Override
                            public void onError() {
                                listener.onError();
                            }
                        }
                );

            } listener.onError();
        });

        return instance;
    }

    private NotificationDAO sendPhoneToFriends(@NonNull final ArrayList<String> keyList,
                                    @NonNull final UserPhone userPhone,
                                    @NonNull final Listeners.AnswerListener listener) {

        for (int i = 0; i < keyList.size(); i++) {
            final Query query = getReference().orderByChild("phoneKey").equalTo(userPhone.getPhoneKey());

            if (i == keyList.size() - 1) {
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot == null || dataSnapshot.getChildren() == null ||
                            !dataSnapshot.getChildren().iterator().hasNext()) {
                            // phone doesn't exist
                            getReference().push().setValue(userPhone);
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
                            getReference().push().setValue(userPhone);
                        }

                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
            }
        }

        return instance;
    }

    @Override @Deprecated
    public NotificationDAO update(@NonNull UserPhone userPhone,
                                  @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public NotificationDAO delete(@NonNegative final String phoneKey,
                                  @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(userNode, retrievedBoolean -> {
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
        });

        return instance;
    }

    @Override @Deprecated
    public NotificationDAO delete(@NonNegative final String notifiedKey,
                                  @NonNegative final String notifyingKey,
                                  @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call method with " +
                "one parameter instead.");
    }

    @Override @Deprecated
    public NotificationDAO sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }
}