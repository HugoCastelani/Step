package com.enoughspam.step.intro;

import android.Manifest;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticButton;
import com.enoughspam.step.R;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class PermissionIntroFragment extends SlideFragment {

    private RxPermissions mRxPermissions;
    private AestheticButton mButton;

    private boolean mCanGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_permission, container, false);

        mRxPermissions = new RxPermissions(getActivity());

        mButton = (AestheticButton) view.findViewById(R.id.intro_permission_button);
        mButton.setOnClickListener(v -> onRequestButtonClick());

        return view;
    }

    private void onRequestButtonClick() {
        mRxPermissions
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        mButton.setEnabled(false);
                        mButton.setText(getResources().getString(R.string.granted_permissions));

                        mCanGoForward = true;
                        canGoForward();
                        nextSlide();

                    } else {

                        Snackbar.make(getView(),
                                getResources().getString(R.string.all_permissions_required),
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }
}
