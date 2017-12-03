package com.hugocastelani.blockbook.ui.intro;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticButton;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.blockbook.R;
import com.tbruyelle.rxpermissions2.RxPermissions;

public final class PermissionIntroFragment extends SlideFragment {

    private RxPermissions mRxPermissions;
    private AestheticButton mButton;

    private boolean mCanGoForward = false;

    private MainIntroActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_permission, container, false);

        mRxPermissions = new RxPermissions(getActivity());

        mButton = (AestheticButton) view.findViewById(R.id.ifp_button);
        mButton.setOnClickListener(v -> onRequestButtonClick());

        return view;
    }

    private void onRequestButtonClick() {
        final NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N &&
                !notificationManager.isNotificationPolicyAccessGranted()) {

            final Intent intentB = new Intent(android.provider.Settings
                    .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intentB);

        } else {

            mRxPermissions
                    .request(Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.PROCESS_OUTGOING_CALLS,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.MODIFY_AUDIO_SETTINGS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.RECEIVE_BOOT_COMPLETED)

                    .subscribe(granted -> {
                        if (granted) {
                            mButton.setEnabled(false);
                            mButton.setText(getResources().getString(R.string.granted_permissions));

                            mCanGoForward = true;
                            canGoForward();
                            nextSlide();

                        } else {

                            ((MainIntroActivity) getActivity())
                                    .createSnackbar(R.string.all_permissions_required).show();
                        }
                    });
        }
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }
}
