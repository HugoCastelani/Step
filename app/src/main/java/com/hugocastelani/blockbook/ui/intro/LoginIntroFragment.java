package com.hugocastelani.blockbook.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.wide.UserDAO;
import com.hugocastelani.blockbook.database.domain.User;
import com.hugocastelani.blockbook.util.Listeners;

public final class LoginIntroFragment extends SlideFragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final int GOOGLE_SIGN_IN = 3;
    private static final int GOOGLE_CODE = 1;

    private View view;
    private MainIntroActivity mActivity;

    private SignInButton mGoogleButton;
    private TextView mGoogleButtonText;

    private boolean mCanGoForward = false;

    private GoogleApiClient mGoogleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_login, container, false);
        mActivity = ((MainIntroActivity) getActivity());

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        mGoogleButton = (SignInButton) view.findViewById(R.id.ifl_google_button);

        mGoogleButtonText = (TextView) mGoogleButton.getChildAt(0);
        mGoogleButtonText.setText(R.string.sign_in_google);

        final GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    private void initActions() {
        mGoogleButton.setOnClickListener(v -> {
            final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        });
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String error;

        if (requestCode == GOOGLE_SIGN_IN) {
            error = handleGoogleLogin(data);
        } else error = getResources().getString(R.string.sign_in_error_unknown_request_code);

        if (error != null) {
            mActivity.createSnackbar(getResources().getString(R.string.sign_in_error) + error).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorMessage() != null) {
            mActivity.createSnackbar(connectionResult.getErrorMessage()).show();
        } else {
            mActivity.createSnackbar(R.string.something_went_wrong).show();
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

    @Override
    public boolean nextSlide() {
        mActivity.getPhoneSlide().prepareNextButton();
        return super.nextSlide();
    }

    private String handleGoogleLogin(@NonNull final Intent data) {
        final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            final GoogleSignInAccount account = result.getSignInAccount();

            if (account.getId() != null) {
                final String socialID = account.getId() + GOOGLE_CODE;

                String photoURL;
                try {
                    photoURL = account.getPhotoUrl().toString();
                } catch (NullPointerException e) {
                    photoURL = "";
                }

                User user = new User(
                        socialID,
                        account.getEmail().replace("@gmail.com", ""),
                        photoURL
                );

                UserDAO.get().create(user, new Listeners.AnswerListener() {
                    @Override
                    public void onAnswerRetrieved() {
                        mCanGoForward = true;
                        canGoForward();
                        nextSlide();
                    }

                    @Override
                    public void onError() {}
                });

                return null;

            } else return getResources().getString(R.string.sign_in_error_empty_id);

        } else return "\n" + result.getStatus().toString();
    }
}
