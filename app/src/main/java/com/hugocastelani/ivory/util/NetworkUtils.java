package com.hugocastelani.ivory.util;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hugo Castelani
 * Date: 08/12/17
 * Time: 01:05
 */

public final class NetworkUtils {
    private NetworkUtils() {
        throw new UnsupportedOperationException("You shouldn't call this.");
    }

    private static Boolean isConnectedToInternet = null;

    public static void init() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnectedToInternet ->
                    NetworkUtils.isConnectedToInternet = isConnectedToInternet
                );
    }

    public static boolean isInitialized() {
        return isConnectedToInternet != null;
    }

    public static Boolean isConnectedToInternet() {
        if (isInitialized()) {
            return isConnectedToInternet;
        } else {
            throw new UnsupportedOperationException("You should call init() method first.");
        }
    }
}
