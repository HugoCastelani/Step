package com.enoughspam.step.intro;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.related.PersonalDAO;
import com.enoughspam.step.database.dao.related.UserDAO;
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

    private static final int RC_SIGN_IN = 7;
    private static final int GOOGLE_CODE = 1;

    private UserDAO userDAO;
    private PersonalDAO personalDAO;

    private SignInButton googleButton;
    private GoogleApiClient googleApiClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_login, container, false);

        userDAO = new UserDAO(getActivity());
        personalDAO = new PersonalDAO(getActivity());

        googleButton = (SignInButton) view.findViewById(R.id.intro_google_button);

        TextView textView = (TextView) googleButton.getChildAt(0);
        textView.setText(R.string.sign_in_google);

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

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();

                if (account.getId() != null) {
                    String idSocial = account.getId() + GOOGLE_CODE;
                    User user = new User(idSocial, account.getDisplayName());

                    if (userDAO.findByIdSocial(idSocial) == null) {
                        userDAO.create(user);
                    }

                    user.setId(userDAO.findIdByIdSocial(idSocial));
                    personalDAO.create(user);
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
