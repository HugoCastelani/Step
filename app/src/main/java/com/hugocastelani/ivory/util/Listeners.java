package com.hugocastelani.ivory.util;

import android.support.annotation.NonNull;

/**
 * Created by Hugo Castelani
 * Date: 30/10/17
 * Time: 18:07
 */

public final class Listeners {
    public interface ObjectListener<T> {
        void onObjectRetrieved(@NonNull final T retrievedObject);
        void onError();
    }

    public interface ListListener<T> {
        void onItemAdded(@NonNull final T item);
        void onItemRemoved(@NonNull final T item);
    }

    public interface AnswerListener {
        void onAnswerRetrieved();
        void onError();
    }

    public interface UserPhoneAnswerListener {
        void alreadyAdded();
        void properlyAdded();
        void error();
    }
}
