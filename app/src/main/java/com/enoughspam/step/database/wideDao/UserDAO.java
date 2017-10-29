package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.abstracts.GenericWideDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
        if (instance == null) instance = new UserDAO();
        return instance;
    }

    @Override
    public UserDAO create(@NonNull final User user) {
        exists(user, retrievedUser -> {
            if (retrievedUser != null) {
                LUserDAO.get().create(retrievedUser);

            } else {

                Query query = getReference().orderByChild("id").limitToLast(1);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        user.setID(dataSnapshot.getValue(User.class).getID() + 1);
                        getReference().push().setValue(user);
                        LUserDAO.get().create(user);
                        query.removeEventListener(this);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                };

                query.addChildEventListener(childEventListener);
            }
        });

        return instance;
    }

    @Override
    public GenericWideDAO update(@NonNull User user) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public GenericWideDAO delete(@NonNegative Integer id1, @NonNegative Integer id2) {
        throw new UnsupportedOperationException("You shouldn't do this. Call delete() method with" +
                "only one parameter instead");
    }

    public UserDAO delete(@NonNegative final Integer id) {
        getReference().orderByChild("id")
                .equalTo(id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getReference().child(dataSnapshot.getKey()).removeValue();
                        LUserDAO.get().delete(id);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        LUserDAO.get().delete(id);
        return instance;
    }

    @Override
    public GenericWideDAO sync(@NonNull DAOHandler.AnswerListener listener) {
        return null;
    }

    public UserDAO exists(@NonNull final User user, @NonNull final UserListener listener) {
        getReference().orderByChild("socialID")
                .equalTo(user.getSocialID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                            dataSnapshot.getChildren().iterator().hasNext()) {

                            getReference().orderByChild("socialID")
                                    .equalTo(user.getSocialID())
                                    .limitToFirst(1)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            listener.onUserRetrieved(
                                                    dataSnapshot.getValue(User.class)
                                            );
                                        }

                                        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                        @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                        @Override public void onCancelled(DatabaseError databaseError) {}
                                    });

                        } else {

                            listener.onUserRetrieved(null);
                        }
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        return instance;
    }

    public UserDAO findByID(@NonNegative final Integer id, @NonNull final UserListener listener) {
        getReference().orderByChild("id")
                .equalTo(id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final User user = dataSnapshot.getValue(User.class);
                        listener.onUserRetrieved(user);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        return instance;
    }

    public interface UserListener {
        void onUserRetrieved(@NonNull final User retrievedUser);
    }
}
