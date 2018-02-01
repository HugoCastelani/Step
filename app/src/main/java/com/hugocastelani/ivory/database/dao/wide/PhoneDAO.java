package com.hugocastelani.ivory.database.dao.wide;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hugocastelani.ivory.database.dao.intangible.GenericWideDAO;
import com.hugocastelani.ivory.database.dao.local.LPhoneDAO;
import com.hugocastelani.ivory.database.domain.Phone;
import com.hugocastelani.ivory.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 28/06/17
 * Time: 09:20
 */

public final class PhoneDAO extends GenericWideDAO<Phone> {
    private static PhoneDAO instance;

    @Override
    protected void prepareFields() {
        node = "/phones";
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

    public PhoneDAO create(@NonNull final Phone phone,
                           @NonNull final Listeners.ObjectListener<Phone> listener) {

        exists(phone, new Listeners.ObjectListener<Phone>() {
            @Override
            public void onObjectRetrieved(@NonNull Phone retrievedPhone) {
                if (retrievedPhone != null) {
                    LPhoneDAO.get().create(retrievedPhone);
                    listener.onObjectRetrieved(retrievedPhone);

                } else {

                    final DatabaseReference cumbuca = getReference().push();

                    phone.setKey(cumbuca.getKey());

                    cumbuca.setValue(phone)
                            .addOnFailureListener(e -> listener.onError())
                            .addOnSuccessListener(aVoid -> {
                                LPhoneDAO.get().create(phone);
                                listener.onObjectRetrieved(phone);
                            });
                }
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    @Override @Deprecated
    public PhoneDAO update(@NonNull Phone phone,
                           @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public PhoneDAO delete(@NonNull String key1, @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override @Deprecated
    public PhoneDAO delete(@NonNull String key1, @NonNull String key2,
                           @NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("You shouldn't do this.");
    }

    @Override
    public PhoneDAO sync(@NonNull Listeners.AnswerListener listener) {
        throw new UnsupportedOperationException("Support this, Hugo!");
    }

    public PhoneDAO findByKey(@NonNull final String key,
                              @NonNull final Listeners.ObjectListener<Phone> listener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final Query query = getReference().orderByKey().equalTo(key).limitToFirst(1);

                    final ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            listener.onObjectRetrieved(dataSnapshot.getValue(Phone.class));
                        }

                        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        @Override public void onCancelled(DatabaseError databaseError) {}
                    };

                    query.addChildEventListener(childEventListener);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            query.removeEventListener(childEventListener);
                            query.removeEventListener(this);
                        }

                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });

                } else listener.onError();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }

    public PhoneDAO exists(@NonNull final Phone phone,
                           @NonNull final Listeners.ObjectListener<Phone> listener) {

        isNodeValid(node, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
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
                                        listener.onObjectRetrieved(dataSnapshot.getValue(Phone.class));
                                        query.removeEventListener(this);
                                    }

                                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
                                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                                    @Override public void onCancelled(DatabaseError databaseError) {}
                                });

                            } else listener.onObjectRetrieved(null);

                            query.removeEventListener(this);
                        }

                        @Override public void onCancelled(DatabaseError databaseError) {}
                    });

                } else listener.onError();
            }

            @Override
            public void onError() {
                listener.onError();
            }
        });

        return instance;
    }
}
