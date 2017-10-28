package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LUserDAO;
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

public class UserDAO {
    private static final String NODE = "users";
    private static DatabaseReference sDatabase;

    private UserDAO() {}

    private static DatabaseReference getDatabase() {
        if (sDatabase == null) {
            sDatabase = DAOHandler.getFirebaseDatabase(NODE);
        }
        return sDatabase;
    }

    public static void create(@NonNull final User user) {
        exists(user, retrievedUser -> {
            if (retrievedUser != null) {
                LUserDAO.create(retrievedUser);

            } else {

                Query query = getDatabase().orderByChild("id").limitToLast(1);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        user.setID(dataSnapshot.getValue(User.class).getID() + 1);
                        getDatabase().push().setValue(user);
                        LUserDAO.create(user);
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
    }

    public static void exists(@NonNull final User user, @NonNull final UserListener listener) {
        getDatabase().orderByChild("socialID")
                .equalTo(user.getSocialID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                            dataSnapshot.getChildren().iterator().hasNext()) {

                            getDatabase().orderByChild("socialID")
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
    }

    public static void delete(@NonNegative final int id) {
        getDatabase().orderByChild("id")
                .equalTo(id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getDatabase().child(dataSnapshot.getKey()).removeValue();
                        LUserDAO.delete(id);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

        LUserDAO.delete(id);
    }

    public static void findByID(@NonNegative final int id, @NonNull final UserListener listener) {
        getDatabase().orderByChild("id")
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
    }

    public interface UserListener {
        void onUserRetrieved(@NonNull final User retrievedUser);
    }
}
