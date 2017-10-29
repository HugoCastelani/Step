package com.enoughspam.step.database.wideDao;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.localDao.AreaDAO;
import com.enoughspam.step.database.localDao.CountryDAO;
import com.enoughspam.step.database.localDao.LPhoneDAO;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 09:20
 */

public class PhoneDAO {
    private static final String NODE = "phones";
    private static DatabaseReference database;

    private PhoneDAO() {}

    private static DatabaseReference getDatabase() {
        if (database == null) {
            database = DAOHandler.getFirebaseDatabase(NODE);
        }
        return database;
    }

    public static void create(@NonNull final Phone phone, @NonNull final PhoneListener listener) {
        exists(phone, retrievedPhone -> {
            if (retrievedPhone != null) {
                listener.onPhoneRetrieved(retrievedPhone);
                LPhoneDAO.get().create(retrievedPhone);

            } else {

                final Query query = getDatabase().orderByChild("id").limitToLast(1);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        phone.setID(dataSnapshot.getValue(Phone.class).getID() + 1);
                        getDatabase().push().setValue(phone);
                        listener.onPhoneRetrieved(phone);
                        LPhoneDAO.get().create(phone);
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

    public static void delete(@NonNegative final Integer id) {
        getDatabase().orderByChild("id")
                .equalTo(id)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        getDatabase().child(dataSnapshot.getKey()).removeValue();
                        LPhoneDAO.get().delete(id);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public static void findByID(@NonNegative final Integer id, @NonNull final PhoneListener listener) {
        getDatabase().orderByChild("id")
                .equalTo(id)
                .limitToFirst(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final Phone phone = dataSnapshot.getValue(Phone.class);

                        if (phone.getCountryID() == -1) {
                            phone.setArea(AreaDAO.get().findByColumn(AreaDAO.id,
                                    String.valueOf(phone.getAreaID())));
                        } else {
                            phone.setCountry(CountryDAO.get().findByColumn(AreaDAO.id,
                                    String.valueOf(phone.getCountryID())));
                        }

                        listener.onPhoneRetrieved(phone);
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private static void exists(@NonNull final Phone phone, @NonNull final PhoneListener listener) {
        getDatabase().orderByChild("numberACID")
                .equalTo(phone.getNumberACID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                                dataSnapshot.getChildren().iterator().hasNext()) {

                            getDatabase().orderByChild("numberACID")
                                    .equalTo(phone.getNumberACID())
                                    .limitToFirst(1)
                                    .addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            listener.onPhoneRetrieved(dataSnapshot.getValue(Phone.class));
                                        }

                                        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                        @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                        @Override public void onCancelled(DatabaseError databaseError) {}
                                    });

                        } else {

                            listener.onPhoneRetrieved(null);
                        }
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public interface PhoneListener {
        void onPhoneRetrieved(@NonNull final Phone retrievedPhone);
    }
}
