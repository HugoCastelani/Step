package com.enoughspam.step.intro;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.enoughspam.step.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class LoginIntroFragment extends SlideFragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 7;

    private SignInButton googleButton;
    private GoogleApiClient googleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_login, container, false);

        googleButton = (SignInButton) view.findViewById(R.id.intro_google_button);
        googleButton.setSize(SignInButton.SIZE_STANDARD);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoogleButtonClick();
            }
        });

        GoogleSignInOptions signInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        return view;
    }

    private void onGoogleButtonClick() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Toast.makeText(getActivity(), "requestCode = RC_SIGN_IN", Toast.LENGTH_SHORT).show();

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();

                GoogleSignInAccount acct = result.getSignInAccount();
                Toast.makeText(getActivity(), acct.getDisplayName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), acct.getGivenName(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), acct.getEmail(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), acct.getId(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), "requestCode != RC_SIGN_IN", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
