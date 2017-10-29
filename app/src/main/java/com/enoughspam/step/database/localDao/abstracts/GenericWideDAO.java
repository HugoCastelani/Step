package com.enoughspam.step.database.localDao.abstracts;

import android.support.annotation.NonNull;

import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.DAOHandler;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Hugo Castelani
 * Date: 28/10/17
 * Time: 18:29
 */

public abstract class GenericWideDAO<T> {
    private DatabaseReference reference;
    protected String node;

    // must set node
    protected abstract void prepareFields();

    public GenericWideDAO() {
        prepareFields();
    }

    protected DatabaseReference getReference() {
        if (reference == null) reference = DAOHandler.getFirebaseDatabase(node);
        return reference;
    }

    public abstract GenericWideDAO create(@NonNull final T t);

    public abstract GenericWideDAO update(@NonNull final T t);

    public abstract GenericWideDAO delete(@NonNegative final Integer id1, @NonNegative final Integer id2);

    public abstract GenericWideDAO sync(@NonNull final DAOHandler.AnswerListener listener);
}
