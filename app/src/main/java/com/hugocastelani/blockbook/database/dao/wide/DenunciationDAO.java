package com.hugocastelani.blockbook.database.dao.wide;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hugocastelani.blockbook.database.dao.DAOHandler;
import com.hugocastelani.blockbook.database.dao.intangible.GenericWideDAO;
import com.hugocastelani.blockbook.database.domain.Denunciation;
import com.hugocastelani.blockbook.util.Listeners;

/**
 * Created by Hugo Castelani
 * Date: 01/12/17
 * Time: 21:06
 */

public class DenunciationDAO extends GenericWideDAO<Denunciation> {
    private static DenunciationDAO instance;

    @Override
    protected void prepareFields() {}

    private DenunciationDAO() {}

    public static DenunciationDAO get() {
        if (instance == null) instance = new DenunciationDAO();
        return instance;
    }

    @Override
    public DenunciationDAO create(@NonNull Denunciation denunciation, @NonNull Listeners.AnswerListener listener) {
        return null;
    }

    @Override
    public DenunciationDAO update(@NonNull Denunciation denunciation, @NonNull Listeners.AnswerListener listener) {
        return null;
    }

    @Override
    public DenunciationDAO delete(@NonNull String key1, @NonNull Listeners.AnswerListener listener) {
        return null;
    }

    @Override
    public DenunciationDAO delete(@NonNull String key1, @NonNull String key2, @NonNull Listeners.AnswerListener listener) {
        return null;
    }

    @Override
    public DenunciationDAO sync(@NonNull Listeners.AnswerListener listener) {
        return null;
    }

    public DenunciationDAO getDenunciations(@NonNull final String phoneKey,
                                            @NonNull final Listeners.ListListener<Denunciation> listListener,
                                            @NonNull final Listeners.AnswerListener answerListener) {
        final String thisNode = "phones/" + phoneKey + "/denunciations/";

        isNodeValid(thisNode, new Listeners.ObjectListener<Boolean>() {
            @Override
            public void onObjectRetrieved(@NonNull Boolean retrievedBoolean) {
                if (retrievedBoolean) {

                    final Query query = DAOHandler.getFirebaseDatabase(thisNode).orderByKey();
                    final ChildEventListener childEventListener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            listListener.onItemAdded(new Denunciation(
                                    Integer.valueOf(dataSnapshot.getKey()),
                                    dataSnapshot.getChildrenCount()
                            ));
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
                            answerListener.onAnswerRetrieved();
                            query.removeEventListener(childEventListener);
                            query.removeEventListener(this);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            answerListener.onAnswerRetrieved();
                            query.removeEventListener(childEventListener);
                            query.removeEventListener(this);
                        }
                    });

                } answerListener.onAnswerRetrieved();
            }

            @Override
            public void onError() {
                answerListener.onError();
            }
        });

        return instance;
    }
}
