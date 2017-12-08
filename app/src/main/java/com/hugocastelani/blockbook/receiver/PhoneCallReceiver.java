package com.hugocastelani.blockbook.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.local.LUserDAO;
import com.hugocastelani.blockbook.database.dao.local.LUserPhoneDAO;
import com.hugocastelani.blockbook.database.dao.wide.DenunciationDAO;
import com.hugocastelani.blockbook.database.dao.wide.UserPhoneDAO;
import com.hugocastelani.blockbook.database.domain.Denunciation;
import com.hugocastelani.blockbook.database.domain.Phone;
import com.hugocastelani.blockbook.database.domain.User;
import com.hugocastelani.blockbook.database.domain.UserPhone;
import com.hugocastelani.blockbook.persistence.HockeyProvider;
import com.hugocastelani.blockbook.persistence.Treatments;
import com.hugocastelani.blockbook.util.Listeners;
import com.orhanobut.hawk.Hawk;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hugo Castelani
 * Date: 08/08/17
 * Time: 16:41
 */

public class PhoneCallReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;
    private static AudioManager mAudioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Entrei no onReceive", Toast.LENGTH_LONG).show();

        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (!Hawk.isBuilt()) {
            Hawk.init(context).build();
        }

        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }
    }

    Boolean silencingCall = false;

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context context, String number, Date start){
        final Integer[] services = Hawk.get(HockeyProvider.SERVICES, HockeyProvider.SERVICES_DF);

        // true: calls is a selected service
        if (services[0] == 0) {
            muteDevice();

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String iso = telephonyManager.getNetworkCountryIso().toUpperCase();

            final Phone phone = Phone.generateObject(number, iso);

            if (phone != null) {
                final User user = LUserDAO.get().getThisUser();
                final UserPhone userPhone = new UserPhone(user.getKey(), phone.getKey(), false, false);
                userPhone.setUser(user);
                userPhone.setPhone(phone);

                if (LUserPhoneDAO.get().isBlocked(userPhone)) {
                    disconnectPhoneITelephony(context);
                    unmuteDevice();
                    return;
                }

                ReactiveNetwork.checkInternetConnectivity()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(isConnectedToInternet -> {
                            if (isConnectedToInternet) {

                                final Integer minDenunciationAmount = Hawk.get(HockeyProvider.DENUNCIATION_AMOUNT,
                                        HockeyProvider.DENUNCIATION_AMOUNT_DF);
                                final Integer[] denunciationAmount = {0};

                                DenunciationDAO.get().getDenunciations(phone.getKey(),

                                        new Listeners.ListListener<Denunciation>() {
                                            @Override
                                            public void onItemAdded(@NonNull Denunciation denunciation) {
                                                denunciationAmount[0]++;

                                                if (denunciation.getAmount() >= minDenunciationAmount) {
                                                    final Integer option = Hawk.get(HockeyProvider.DESCRIPTION +
                                                            denunciation.getDescription(), HockeyProvider.DESCRIPTION_DF);

                                                    final Treatments treatments = new Treatments(option);
                                                    switch (treatments.getOption()) {
                                                        case SILENCE: silencingCall = true;
                                                            break;
                                                        case BLOCK: blockPhone(phone, context);
                                                            break;

                                                        // case DONOTHING must get in here
                                                        default: unmuteDevice();
                                                    }
                                                }
                                            }

                                            @Override public void onItemRemoved(@NonNull Denunciation denunciation) {}
                                        },

                                        new Listeners.AnswerListener() {
                                            @Override public void onAnswerRetrieved() {
                                                if (denunciationAmount[0] == 0) {
                                                    unmuteDevice();
                                                }
                                            }
                                            @Override public void onError() {
                                                if (denunciationAmount[0] == 0) {
                                                    unmuteDevice();
                                                }
                                            }
                                        }
                                );

                            } else {
                                unmuteDevice();
                            }
                        });
            }
        }
    }

    protected void onOutgoingCallStarted(Context context, String number, Date start){}

    protected void onIncomingCallEnded(Context context, String number, Date start, Date end){
        if (Hawk.get(HockeyProvider.SHOW_FEEDBACK, HockeyProvider.SHOW_FEEDBACK_DF)) {
            // show feedback dialog
        }
    }

    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end){}

    protected void onMissedCall(Context context, String number, Date start){
        if (silencingCall) {
            unmuteDevice();
        }
    }

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state){
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss
                    onMissedCall(context, savedNumber, callStartTime);
                } else if (isIncoming) {
                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                }
                break;
        }
        lastState = state;
    }

    @SuppressWarnings("deprecation")
    protected PhoneCallReceiver muteDevice() {
        mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
        return this;
    }

    @SuppressWarnings("deprecation")
    protected PhoneCallReceiver unmuteDevice() {
        mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
        return this;
    }

    private void blockPhone(@NonNull final Phone phone, @NonNull final Context context) {
        final UserPhone userPhone = new UserPhone(LUserDAO.get().getThisUserKey(), phone.getKey(), false, false);

        UserPhoneDAO.get().create(userPhone, new Listeners.UserPhoneAnswerListener() {
                    @Override
                    public void alreadyAdded() {
                        UserPhoneDAO.get().create(userPhone, new Listeners.UserPhoneAnswerListener() {
                            @Override public void alreadyAdded() {}

                            @Override
                            public void properlyAdded() {
                                sendNotification(phone, context);
                            }

                            @Override public void error() {}
                        }, true);
                    }

                    @Override public void properlyAdded() {
                        sendNotification(phone, context);
                    }

                    @Override public void error() {}
                },  false
        );
    }

    private void sendNotification(@NonNull final Phone phone, @NonNull final Context context) {
        final String title = context.getResources().getString(R.string.properly_added_title);

        final StringBuilder content = new StringBuilder();
        content.append(context.getResources().getString(R.string.properly_added_content_1))
                .append(phone.getNumber())
                .append(context.getResources().getString(R.string.properly_added_content_2));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.intro_1)
                .setContentTitle(title)
                .setContentText(content)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        NotificationManager manager = (NotificationManager)
                context.getSystemService(context.NOTIFICATION_SERVICE);
        manager.notify();
        manager.notify(8055, builder.build());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void disconnectPhoneITelephony(@NonNull final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            ITelephony telephonyService;
            Class aClass = Class.forName(telephonyManager.getClass().getName());
            Method method = aClass.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            telephonyService = (ITelephony) method.invoke(telephonyManager);
            telephonyService.endCall();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
