package com.enoughspam.step.intro;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.afollestad.aesthetic.AestheticButton;
import com.enoughspam.step.R;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.maxcruz.reactivePermissions.entity.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.functions.Consumer;
import kotlin.Pair;

import java.util.ArrayList;

public class PermissionIntroFragment extends SlideFragment {

    private static final int REQUEST_CODE = 4;
    private RxPermissions rxPermissions;

    private boolean canGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_permission, container, false);

        rxPermissions = new RxPermissions(getActivity());
        AestheticButton button = (AestheticButton) view.findViewById(R.id.intro_permission_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestButtonClick();
            }
        });

        return view;
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }

    private void onRequestButtonClick() {
        rxPermissions
                .request(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.READ_CONTACTS)
                .subscribe(granted -> {
                    if (granted) {

                    }
                }
        });

        Permission phoneState = new Permission(
                Manifest.permission.READ_PHONE_STATE, // Permission constant to request
                R.string.rationale_phone_state, // String resource with rationale explanation
                false // Define if the app can continue without the permission
        );

        Permission sms = new Permission(
                Manifest.permission.READ_SMS,
                R.string.rationale_sms,
                false
        );

        Permission contacts = new Permission(
                Manifest.permission.READ_CONTACTS,
                R.string.rationale_contacts,
                true
        );

        ArrayList<Permission> permissions = new ArrayList<>();
        permissions.add(phoneState);
        permissions.add(sms);
        permissions.add(contacts);

        reactive = new ReactivePermissions(this, REQUEST_CODE);

        reactive.observeResultPermissions().subscribe(new Consumer<Pair<String, Boolean>>() {
            @Override
            public void accept(Pair<String, Boolean> event) throws Exception {
                switch (event.getFirst()) {
                    case Manifest.permission.READ_PHONE_STATE:
                        if (event.getSecond()) {

                        }
                }
        })
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(getActivity(), "requestCode = REQUEST_CODE", Toast.LENGTH_SHORT).show();
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Both permissions granted", Toast.LENGTH_SHORT).show();
                canGoForward = true;
                canGoForward();
                nextSlide();
            }
        } else {
            Toast.makeText(getActivity(), "requestCode != REQUEST_CODE", Toast.LENGTH_SHORT).show();
        }
    }
}
