package com.enoughspam.step.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.enoughspam.step.util.VariousUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Hugo Castelani
 * Date: 08/08/17
 * Time: 16:41
 */

public class PhoneCallReceiver extends BroadcastReceiver {
    private Context mContext;
    private Intent mIntent;
    private TelephonyManager mTelephonyManager;
    private AudioManager mAudioManager;

    @Override
    @SuppressWarnings("deprecation")
    public void onReceive(Context context, Intent intent) {
        if (!VariousUtils.isAboveMarshmallow()) {    // BlockedNumberProvider shouldn't do the work
            mContext = context;
            mIntent = intent;
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

            // mute until discover whether is number blocked
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);

            switch (intent.getAction()) {
                case "android.intent.action.PHONE_STATE":
                    handlePhoneCall();
                    break;
                case "android.provider.Telephony.SMS_RECEIVED":
                    handleSms();
                    break;
                default: break;
            }

            mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
        }
    }

    private void handlePhoneCall() {
        mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        final String number = mIntent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        final String iso = mTelephonyManager.getNetworkCountryIso().toUpperCase();

        // NullPointerException is going to be thrown if it's an outcoming call
        final boolean isBlocked;
        try {
            DAOHandler.init(mContext);
            final Phone phone = Phone.generateObject(number, iso);
            isBlocked = LUserPhoneDAO.isBlocked(new UserPhone(LUserDAO.getThisUser(), phone, false));
        } catch (NullPointerException e) {
            return;
        }

        // check if it's blocked
        if (isBlocked) disconnectPhoneITelephony();
    }

    @SuppressWarnings("deprecation")
    private void handleSms() {
        final Bundle bundle = mIntent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObject = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObject.length; i++) {

                    SmsMessage smsMessage;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(mIntent);
                        smsMessage = msgs[0];

                    } else {

                        Object pdus[] = (Object[]) bundle.get("pdus");
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    }

                    String phoneNumber = smsMessage.getDisplayOriginatingAddress();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void disconnectPhoneITelephony() {
        try {
            ITelephony telephonyService;
            Class aClass = Class.forName(mTelephonyManager.getClass().getName());
            Method method = aClass.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            telephonyService = (ITelephony) method.invoke(mTelephonyManager);
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
