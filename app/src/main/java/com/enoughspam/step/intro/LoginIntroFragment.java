package com.enoughspam.step.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.UserDAO;
import com.enoughspam.step.database.domains.User;
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

    private static final int RC_SIGN_IN = 3;
    private static final int GOOGLE_CODE = 1;

    private View view;
    private boolean mCanGoForward = false;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_login, container, false);

        final SignInButton googleButton = (SignInButton) view.findViewById(R.id.intro_google_button);

        final TextView textView = (TextView) googleButton.getChildAt(0);
        textView.setText(R.string.sign_in_google);

        googleButton.setOnClickListener(v -> onGoogleButtonClick());

        final GoogleSignInOptions signInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        return view;
    }

    private void onGoogleButtonClick() {
        final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String error;

        if (requestCode == RC_SIGN_IN) {

            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                final GoogleSignInAccount account = result.getSignInAccount();

                if (account.getId() != null) {
                    final String idSocial = account.getId() + GOOGLE_CODE;
                    final User user = new User(idSocial, account.getDisplayName());

                    if (UserDAO.findBySocialId(idSocial) == null) {
                        UserDAO.create(user);
                    }

                    user.setId(UserDAO.findIdByIdSocial(idSocial));
                    PersonalDAO.create(user);

                    mCanGoForward = true;
                    canGoForward();
                    nextSlide();
                    return;

                } else error = getResources().getString(R.string.sign_in_error_empty_id);

            } else error = "\n" + result.getStatus().toString();

        } else error = getResources().getString(R.string.sign_in_error_unknown_request_code);


        Snackbar.make(view, getResources().getString(R.string.sign_in_error) + error,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorMessage() != null) {
            Snackbar.make(view, connectionResult.getErrorMessage(), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, R.string.something_went_wrong, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
