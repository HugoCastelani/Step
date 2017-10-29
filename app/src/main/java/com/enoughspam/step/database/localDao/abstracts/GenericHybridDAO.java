package com.enoughspam.step.database.localDao.abstracts;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.abstracts.Domain;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hugo Castelani
 * Date: 28/10/17
 * Time: 11:05
 */

public abstract class GenericHybridDAO<T> extends GenericLocalDAO<T> {
    private DatabaseReference database;
    protected String node;

    protected Class aClass;

    public GenericHybridDAO() {}

    protected DatabaseReference getFirebase() {
        if (database == null) {
            database = DAOHandler.getFirebaseDatabase(node);
        }
        return database;
    }

    public GenericHybridDAO<T> sync(@NonNull final DAOHandler.AnswerListener listener) {
        Query query = getFirebase().orderByChild("id");

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

                if (domain.getID() != 0) {
                    if (findByColumn(id, String.valueOf(domain.getID())) == null) {
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
