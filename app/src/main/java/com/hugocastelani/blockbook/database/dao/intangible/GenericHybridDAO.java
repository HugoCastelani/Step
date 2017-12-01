package com.hugocastelani.blockbook.database.dao.intangible;

import android.support.annotation.NonNull;
import com.google.firebase.database.*;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.domain.abstracts.Domain;
import com.hugocastelani.blockbook.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 28/10/17
 * Time: 11:05
 */

public abstract class GenericHybridDAO<T> extends GenericLocalDAO<T> {
    private DatabaseReference reference;
    protected String node;

    protected Class aClass;

    public GenericHybridDAO() {}

    protected DatabaseReference getReference() {
        if (reference == null) reference = DAOHandler.getFirebaseDatabase(node);
        return reference;
    }

    @Override
    public GenericHybridDAO<T> sync(@NonNull final Listeners.AnswerListener listener) {
        Query query = getReference().orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onAnswerRetrieved();
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Domain domain = (Domain) dataSnapshot.getValue(aClass);
                final String domainKey = domain.getKey();

                if (domainKey != null && !domainKey.isEmpty()) {
                    if (findByColumn(key, domainKey) == null) {
                        create((T) domain);
                    }
                }
            }

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

        return this;
    }
}
