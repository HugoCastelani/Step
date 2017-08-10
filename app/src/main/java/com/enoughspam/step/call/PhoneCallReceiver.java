package com.enoughspam.step.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.enoughspam.step.database.DAOHandler;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Hugo Castelani
 * Date: 08/08/17
 * Time: 16:41
 */

public class PhoneCallReceiver extends BroadcastReceiver {
    private Context mContext;
    private TelephonyManager mTelephonyManager;
    private AudioManager mAudioManager;
    private Phone mPhone;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            mContext = context;
            mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

            // mute until discover whether is number blocked
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else {
                //noinspection deprecation
                mAudioManager.setStreamMute(AudioManager.STREAM_RING, true);
            }

            mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            final String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            final String iso = mTelephonyManager.getNetworkCountryIso().toUpperCase();

            try {
                DAOHandler.getContext();
            } catch (NullPointerException e) {
                DAOHandler.init(mContext);
            }

            mPhone = Phone.generateObject(number, iso);
            final boolean isBlocked;
            try {
                isBlocked = LUserPhoneDAO.isBlocked(new UserPhone(LUserDAO.getThisUser(), mPhone, false));
            } catch (NullPointerException e) {
                return;
            }

            // check if it's blocked
            if (isBlocked) {
                disconnectPhoneITelephony();

            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                } else {
                    //noinspection deprecation
                    mAudioManager.setStreamMute(AudioManager.STREAM_RING, false);
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void disconnectPhoneITelephony() {
        ITelephony telephonyService;
        TelephonyManager telephony = (TelephonyManager)
                mContext.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            Class aClass = Class.forName(telephony.getClass().getName());
            Method method = aClass.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            telephonyService = (ITelephony) method.invoke(telephony);
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
