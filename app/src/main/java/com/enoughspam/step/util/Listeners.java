package com.enoughspam.step.util;

import android.support.annotation.NonNull;

import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.domain.UserPhone;

/**
 * Created by Hugo Castelani
 * Date: 30/10/17
 * Time: 18:07
 */

public class Listeners {
    public interface ObjectListener<T> {
        void onObjectRetrieved(@NonNull final T retrievedObject);
    }

    public interface PhoneListener {
        void onPhoneRetrieved(@NonNull final Phone retrievedPhone);
        void onError();
    }

    public interface UserListener {
        void onUserRetrieved(@NonNull final User retrievedUser);
        void onError();
    }

    public interface UserPhoneListener {
        void onUserPhoneRetrieved(@NonNull final UserPhone retrievedUserPhone);
        void onError();
    }

    public interface ListListener<T> {
        void onItemAdded(@NonNull final T item);
        void onItemRemoved(@NonNull final T item);
    }

    public interface BooleanListener {
        void onBooleanRetrieved(final boolean retrievedBoolean);
    }

    public interface KeyListener {
        void onKeyRetrieved(@NonNull final String retrievedKey);
        void onError();
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
