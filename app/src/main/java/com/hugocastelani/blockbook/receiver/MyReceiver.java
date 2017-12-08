package com.hugocastelani.blockbook.receiver;

/**
 * Created by Hugo Castelani
 * Date: 01/12/17
 * Time: 20:06
 */

public final class MyReceiver extends PhoneCallReceiver {
    /*Boolean silencingCall = false;

    @Override
    protected void onIncomingCallStarted(Context context, String number, Date start) {
        final Integer[] services = Hawk.get(HockeyProvider.SERVICES, HockeyProvider.SERVICES_DF);

        // true: calls is a selected service
        if (services[0] == 0) {
            muteDevice(context);

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String iso = telephonyManager.getNetworkCountryIso().toUpperCase();

            final Phone phone = Phone.generateObject(number, iso);

            if (phone != null) {
                final UserPhone userPhone = new UserPhone(LUserDAO.get().getThisUserKey(), phone.getKey(), false, false);

                if (LUserPhoneDAO.get().isBlocked(userPhone)) {
                    disconnectPhoneITelephony(context);
                    unmuteDevice(context);
                    return;
                }

                final Integer minDenunciationAmount = Hawk.get(HockeyProvider.DENUNCIATION_AMOUNT,
                        HockeyProvider.DENUNCIATION_AMOUNT_DF);

                DenunciationDAO.get().getDenunciations(phone.getKey(),

                        new Listeners.ListListener<Denunciation>() {
                            @Override
                            public void onItemAdded(@NonNull Denunciation denunciation) {
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
                                        default: unmuteDevice(context);
                                    }
                                }
                            }

                            @Override public void onItemRemoved(@NonNull Denunciation denunciation) {}
                        },

                        new Listeners.AnswerListener() {
                            @Override public void onAnswerRetrieved() {}
                            @Override public void onError() {}
                        }
                );
            }
        }
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
        if (Hawk.get(HockeyProvider.SHOW_FEEDBACK, HockeyProvider.SHOW_FEEDBACK_DF)) {
            // show feedback dialog
        }
    }

    @Override protected void onMissedCall(Context context, String number, Date start) {
        if (silencingCall) {
            unmuteDevice(context);
        }
    }

    @Override protected void onOutgoingCallStarted(Context context, String number, Date start) {}
    @Override protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {}

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
    }*/
}
