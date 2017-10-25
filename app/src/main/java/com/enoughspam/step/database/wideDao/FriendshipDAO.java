package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LFriendshipDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 04/08/17
 * Time: 21:57
 */

public class FriendshipDAO {
    public static final String NODE = "friendships";
    private static DatabaseReference sDatabase;

    private FriendshipDAO() {}

    private static DatabaseReference getDatabase() {
        if (sDatabase == null) {
            sDatabase = FirebaseDatabase.getInstance().getReference(NODE);
        }

        return sDatabase;
    }

    public static void create(@NonNull final Friendship friendship) {
        exists(friendship, retrievedBoolean -> {
            if (!retrievedBoolean) {
                getDatabase().push().setValue(friendship);
            }
            LFriendshipDAO.create(friendship);
        });
    }

    private static void exists(@NonNull final Friendship friendship,
                              @NonNull final UserPhoneDAO.BooleanListener listener) {
        getDatabase().orderByChild("addedIDAddingID")
                .equalTo(friendship.getAddedIDAddingID())
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

    public static void delete(@NonNegative final int addedID, @NonNegative final int addingID) {
        getDatabase().orderByChild("addedIDAddingID")
                .equalTo(String.valueOf(addedID) + addingID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getDatabase().child(dataSnapshot.getKey()).removeValue();
                        LFriendshipDAO.delete(addedID, addingID);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public static void findUserFriends(@NonNegative final int userID,
                                       @NonNull final UserPhoneDAO.ListListener listener) {
        getDatabase().orderByChild("addingID")
                .equalTo(userID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        UserDAO.findByID(
                                dataSnapshot.getValue(Friendship.class).getAddedID(),
                                retrievedUser -> listener.onItemAdded(retrievedUser)
                        );
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public static void getFriendsBlockedList(@NonNegative final int userID,
                                             @NonNull final UserPhoneDAO.ListListener listener) {
        findUserFriends(userID, new UserPhoneDAO.ListListener<User>() {
            @Override
            public void onItemAdded(@NonNull User user) {
                UserPhoneDAO.getPhoneList(user.getID(), listener);
            }

            @Override public void onItemRemoved(@NonNull User user) {}
        });
    }
}
