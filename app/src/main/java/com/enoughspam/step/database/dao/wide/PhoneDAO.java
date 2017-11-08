package com.enoughspam.step.database.dao.wide;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.abstracts.GenericWideDAO;
import com.enoughspam.step.database.dao.local.LPhoneDAO;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.util.Listeners;
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

public class PhoneDAO extends GenericWideDAO<Phone> {
    private static PhoneDAO instance;

    @Override
    protected void prepareFields() {
        node = "users/" + LUserDAO.get().getThisUserKey() + "/phones";
    }

    private PhoneDAO() {}

    public static PhoneDAO get() {
        if (instance == null) instance = new PhoneDAO();
        return instance;
    }

    @Override @Deprecated
    public PhoneDAO create(@NonNull Phone phone,
                           @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this. Call overloaded method" +
                " with phone listener instead.");
    }

    public PhoneDAO create(@NonNull final Phone phone, @NonNull final Listeners.PhoneListener listener) {
        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {
                exists(phone, new Listeners.PhoneListener() {
                    @Override
                    public void onPhoneRetrieved(@NonNull Phone retrievedPhone) {
                        if (retrievedPhone != null) {
                            LPhoneDAO.get().create(retrievedPhone);
                            listener.onPhoneRetrieved(retrievedPhone);

                        } else {

                            final DatabaseReference cumbuca = getReference().push();

                            phone.setKey(cumbuca.getKey());

                            cumbuca.setValue(phone)
                                    .addOnFailureListener(e -> listener.onError())
                                    .addOnSuccessListener(aVoid -> {
                                        LPhoneDAO.get().create(phone);
                                        listener.onPhoneRetrieved(phone);
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

    @Override @Deprecated
    public PhoneDAO update(@NonNull Phone phone,
                           @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public PhoneDAO delete(@NonNegative String key1, @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public PhoneDAO delete(@NonNegative String key1, @NonNegative String key2,
                           @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public PhoneDAO sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo!");
    }

    public PhoneDAO findByKey(@NonNegative final String key,
                              @NonNull final Listeners.PhoneListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByKey().equalTo(key).limitToFirst(1);

                final ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        listener.onPhoneRetrieved(dataSnapshot.getValue(Phone.class));
                    }

                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    @Override public void onCancelled(DatabaseError databaseError) {}
                };

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        query.removeEventListener(childEventListener);
                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

                query.addChildEventListener(childEventListener);

            } else listener.onError();
        });

        return instance;
    }

    public PhoneDAO exists(@NonNull final Phone phone,
                           @NonNull final Listeners.PhoneListener listener) {

        isNodeValid(node, retrievedBoolean -> {
            if (retrievedBoolean) {

                final Query query = getReference().orderByChild("numberACKey").equalTo(phone.getNumberACKey());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getChildren() != null &&
                            dataSnapshot.getChildren().iterator().hasNext()) {

                            query.limitToFirst(1).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    listener.onPhoneRetrieved(dataSnapshot.getValue(Phone.class));
                                    query.removeEventListener(this);
                                }

                                @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                @Override public void onCancelled(DatabaseError databaseError) {}
                            });

                        } else listener.onPhoneRetrieved(null);

                        query.removeEventListener(this);
                    }

                    @Override public void onCancelled(DatabaseError databaseError) {}
                });

            } else listener.onError();
        });

        return instance;
    }
}
