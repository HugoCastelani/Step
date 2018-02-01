package com.hugocastelani.ivory.util;

import android.support.annotation.NonNull;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hugo Castelani
 * Date: 08/12/17
 * Time: 01:05
 */

public final class NetworkObserver {
    private NetworkObserver() {
        throw new UnsupportedOperationException("You shouldn't call this.");
    }

    private static Boolean isConnected = null;

    public static void init() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet ->
                    NetworkObserver.isConnected = isConnectedToInternet
                );
    }

    public static boolean isInitialized() {
        return isConnected != null;
    }

    public static void isConnectedToInternet(@NonNull final Listeners.ObjectListener<Boolean> listener) {
        if (isInitialized()) {
            listener.onObjectRetrieved(isConnected);

        } else {

            // observer from init() did not retrieve any value yet,
            // so the solution is call single result observer below
            ReactiveNetwork.checkInternetConnectivity().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listener::onObjectRetrieved);
        }
    }
}
