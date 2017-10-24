package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 29/07/17
 * Time: 15:30
 */

public class UserPhoneDAO {
    private static final String NODE = "userPhones";
    private static DatabaseReference database;

    private UserPhoneDAO() {}

    private static DatabaseReference getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance().getReference(NODE);
        }
        return database;
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

    public static void findUserByID(@NonNegative final int userID, @NonNull final UserPhoneListener listener) {
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

    public static List<Phone> getPhoneList(@NonNegative final int id) {
        /*final Cursor cursor = DAOHandler.getLocalDatabase().query(
                UserPhoneDAO.TABLE, new String[] {UserPhoneDAO.PHONE_ID},
                UserPhoneDAO.USER_ID + " = ? AND " + UserPhoneDAO.IS_PROPERTY + " = ?",
                new String[] {String.valueOf(id), "0"}, null, null, null);

        final List<Phone> phoneList = new ArrayList<>();

        while (cursor.moveToNext()) {
            phoneList.add(PhoneDAO.findByID(
                    cursor.getInt(cursor.getColumnIndex(UserPhoneDAO.PHONE_ID)))
            );
        }

        cursor.close();*/
        final List<Phone> phoneList = new ArrayList<>();
        return phoneList;
    }

    public interface UserPhoneListener {
        void onUserPhoneRetrieved(@NonNull final UserPhone retrievedUserPhone);
    }

    public interface BooleanListener {
        void onBooleanRetrieved(final boolean retrievedBoolean);
    }
}
