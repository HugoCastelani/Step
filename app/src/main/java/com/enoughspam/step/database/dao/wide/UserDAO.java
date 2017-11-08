package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.abstracts.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.util.Listeners;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 27/06/17
 * Time: 17:01
 */

public class UserDAO extends GenericWideDAO<User> {
    private static UserDAO instance;

    @Override
    protected void prepareFields() {
        node = "users";
    }

    private UserDAO() {}

    public static UserDAO get() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    @Override
    public UserDAO create(@NonNull final User user,
                          @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                exists(user, new Listeners.UserListener() {
                    @Override
                    public void onUserRetrieved(@NonNull User retrievedUser) {
                        if (retrievedUser != null) {
                            LUserDAO.get().create(retrievedUser);
                            listener.onAnswerRetrieved();

                        } else {

                            final DatabaseReference cumbuca = getReference().push();

                            user.setKey(cumbuca.getKey());

                            cumbuca.setValue(user)
                                    .addOnFailureListener(e -> listener.onError())
                                    .addOnSuccessListener(aVoid -> {
                                        LUserDAO.get().create(user);
                                        listener.onAnswerRetrieved();
                                    });
                        }
                    }

                    @Override
                    public void onError() {
                        listener.onError();
                    }
                });

            } else listener.onError();
        });

        return instance;
    }

    @Override
    public UserDAO update(@NonNull final User user,
                          @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo!");
    }

    @Override
    public UserDAO delete(@NonNull final String userKey,
                          @NonNull final Listeners.AnswerListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                getReference().child(userKey).removeValue()
                        .addOnFailureListener(e -> listener.onError())
                        .addOnSuccessListener(aVoid -> {
                            LUserDAO.get().delete(userKey);
                            listener.onAnswerRetrieved();
                        });

            } else listener.onError();
        });

        return instance;
    }

    @Override @Deprecated
    public UserDAO delete(@NonNull final String key1, @NonNull final String key2,
                          @NonNull final Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call overloaded method" +
                " with one parameter instead.");
    }

    @Override
    public UserDAO sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo.");
    }

    public UserDAO exists(@NonNull final User user,
                          @NonNull final Listeners.UserListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("socialKey").equalTo(user.getSocialKey());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                            dataSnapshot.getChildren().iterator().hasNext()) {

                            query.limitToFirst(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    listener.onUserRetrieved(dataSnapshot.getValue(User.class));
                                    query.removeEventListener(this);
                                }

                                @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                @Override public void onCancelled(DatabaseError databaseError) {}
                            });

                        } else {

                            listener.onUserRetrieved(null);
                        }

                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {
                        query.removeEventListener(this);
                    }
                });

            } else listener.onError();
        });

        return instance;
    }

    public UserDAO findByKey(@NonNegative final String key,
                             @NonNull final Listeners.UserListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByKey().equalTo(key);

                query.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final User user = dataSnapshot.getValue(User.class);
                        listener.onUserRetrieved(user);
                        query.removeEventListener(this);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                    @Override public void onCancelled(DatabaseError databaseError) {
                        query.removeEventListener(this);
                    }
                });

            } else listener.onError();
        });

        return instance;
    }
}
