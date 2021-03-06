package com.hugocastelani.ivory.database.dao.intangible;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hugocastelani.ivory.database.dao.DAOHandler;
import com.hugocastelani.ivory.database.dao.local.LUserDAO;
import com.hugocastelani.ivory.util.Listeners;
import com.hugocastelani.ivory.util.NetworkObserver;

/**
 * Created by Hugo Castelani
 * Date: 28/10/17
 * Time: 18:29
 */

public abstract class GenericWideDAO<T> {
    private DatabaseReference reference;
    protected String node;
    protected String userNode;

    // must set node
    protected abstract void prepareFields();

    public GenericWideDAO() {
        userNode = "users/" + LUserDAO.get().getThisUserKey();
        prepareFields();
    }

    protected DatabaseReference getReference() {
        if (reference == null) reference = DAOHandler.getFirebaseDatabase(node);
        return reference;
    }

    protected void isNodeValid(@NonNull final String node,
                               @NonNull final Listeners.ObjectListener<Boolean> listener) {
        NetworkObserver.isConnectedToInternet(new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean isConnected) {
                if (isConnected) {

                    DAOHandler.getFirebaseDatabase(null).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            listener.onObjectRetrieved(dataSnapshot.hasChild(node));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.onObjectRetrieved(false);
                        }
                    });

                } else listener.onError();
            }

            @Override public void onError() {}
        });
    }

    public abstract GenericWideDAO<T> create(@NonNull final T t,
                                          @NonNull final Listeners.AnswerListener listener);

    public abstract GenericWideDAO<T> update(@NonNull final T t,
                                          @NonNull final Listeners.AnswerListener listener);

    public abstract GenericWideDAO<T> delete(@NonNull final String key1,
                                          @NonNull final Listeners.AnswerListener listener);

    public abstract GenericWideDAO<T> delete(@NonNull final String key1, @NonNull final String key2,
                                          @NonNull final Listeners.AnswerListener listener);

    public abstract GenericWideDAO<T> sync(@NonNull final Listeners.AnswerListener listener);
}
