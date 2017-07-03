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

    private RxPermissions rxPermissions;
    private AestheticButton button;

    private boolean canGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_permission, container, false);

        rxPermissions = new RxPermissions(getActivity());

        button = (AestheticButton) view.findViewById(R.id.intro_permission_button);
        button.setOnClickListener(v -> onRequestButtonClick());

        return view;
    }

    private void onRequestButtonClick() {
        rxPermissions
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {
                        button.setEnabled(false);
                        button.setText(getResources().getString(R.string.granted_permissions));

                        canGoForward = true;
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
        return canGoForward;
    }
}
