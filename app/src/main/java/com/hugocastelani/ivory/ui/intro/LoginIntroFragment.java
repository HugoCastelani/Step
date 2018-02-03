package com.hugocastelani.ivory.ui.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.database.dao.local.LUserDAO;
import com.hugocastelani.ivory.database.dao.wide.UserDAO;
import com.hugocastelani.ivory.database.domain.User;
import com.hugocastelani.ivory.util.Listeners;
import com.hugocastelani.ivory.util.ThemeHandler;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;

public final class LoginIntroFragment extends SlideFragment implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final Integer GOOGLE_SIGN_IN = 3;
    private static final Integer TWITTER_SIGN_IN = TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE;
    private static final Integer FACEBOOK_SIGN_IN = CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();

    private static final String FACEBOOK_CODE = "_1";
    private static final String GOOGLE_CODE = "_2";
    private static final String TWITTER_CODE = "_3";

    private View view;
    private MainIntroActivity mActivity;

    private SignInButton mGoogleButton;
    private TextView mGoogleButtonText;

    private TwitterLoginButton mTwitterButton;
    private LoginButton mFacebookButton;

    private boolean mCanGoForward = false;

    private GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;

    public boolean mIsFirstLogin;
    public boolean mMustSkip = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_login, container, false);
        mActivity = ((MainIntroActivity) getActivity());

        mActivity.setLoginSlide(this);
        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        mGoogleButton = (SignInButton) view.findViewById(R.id.ifl_google_button);

        mGoogleButtonText = (TextView) mGoogleButton.getChildAt(0);
        mGoogleButtonText.setText(R.string.continue_with_google);

        final GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

        mTwitterButton = (TwitterLoginButton) view.findViewById(R.id.ifl_twitter_button);

        mFacebookButton = (LoginButton) view.findViewById(R.id.ifl_facebook_button);
    }

    private void initActions() {
        mGoogleButton.setOnClickListener(v -> {
            final Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        });

        mTwitterButton.setCallback(handleTwitterLogin());

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, handleFacebookLogin());
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String error = null;

        if (requestCode == FACEBOOK_SIGN_IN) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == GOOGLE_SIGN_IN) {
            error = handleGoogleLogin(data);
        } else if (requestCode == TWITTER_SIGN_IN) {
            mTwitterButton.onActivityResult(requestCode, resultCode, data);
        }

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
        mCanGoForward = true;
        canGoForward();
        mActivity.getUsernameSlide().prepareNextButton();
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

                createUser(new User(
                        socialID,
                        account.getEmail().replace("@gmail.com", ""),
                        photoURL
                ));

                return null;

            } else return "\n" + getResources().getString(R.string.sign_in_error_empty_id);

        } else return "\n" + result.getStatus().toString();
    }

    private Callback handleTwitterLogin() {
        return new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Call<com.twitter.sdk.android.core.models.User> user = TwitterCore.getInstance().getApiClient()
                        .getAccountService().verifyCredentials(false, false, false);

                user.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
                    @Override
                    public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                        createUser(new User(
                                Long.toString(result.data.id) + TWITTER_CODE,
                                result.data.screenName,
                                result.data.profileImageUrl.replace("_normal", "_400x400")
                        ));
                    }

                    @Override public void failure(TwitterException exception) {}
                });
            }

            @Override
            public void failure(TwitterException exception) {
                mActivity.createSnackbar(getResources().getString(R.string.sign_in_error) + exception.getMessage()).show();
            }
        };
    }

    private FacebookCallback<LoginResult> handleFacebookLogin() {
        return new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final AccessToken token = loginResult.getAccessToken();
                final Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name");

                new GraphRequest(token, "/" + token.getUserId(), parameters,
                        HttpMethod.GET, response -> {
                            try {
                                final JSONObject jsonObject = response.getJSONObject();

                                final String socialKey = jsonObject.getString("id") + FACEBOOK_CODE;
                                final String username = jsonObject.getString("first_name") +
                                        jsonObject.getString("last_name");

                                String picURL = "";
                                try {
                                    picURL = getFinalURL("http://graph.facebook.com/" +
                                            jsonObject.getString("id") + "/picture?type=large");
                                } catch (IOException e) {}

                                createUser(new User(socialKey, username, picURL));

                            } catch (JSONException e) {

                            }
                        }).executeAsync();
            }

            public String getFinalURL(@NonNull final String url) throws IOException {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setInstanceFollowRedirects(false);
                con.connect();
                return con.getHeaderField("Location");
            }

            @Override
            public void onCancel() {
                mActivity.createSnackbar(getResources().getString(R.string.something_went_wrong)).show();
            }

            @Override
            public void onError(FacebookException error) {
                mActivity.createSnackbar(getResources().getString(R.string.sign_in_error) + error.getMessage()).show();
            }
        };
    }

    private void createUser(@NonNull final User user) {
        // usernames tagged with "&1nv" are not valid
        user.setUsername("&1nv" + user.getUsername());

        UserDAO.get().create(user, new Listeners.AnswerListener() {
            @Override
            public void onAnswerRetrieved() {
                // if it isn't first login, the user won't be
                // created, so the username won't start with "&1nv"
                mIsFirstLogin = LUserDAO.get().getThisUser().getUsername().startsWith("&1nv");

                if (!mIsFirstLogin) {
                    new MaterialDialog.Builder(getActivity())
                            .title(R.string.already_registered_title)
                            .titleColor(ThemeHandler.getPrimaryText())
                            .content(R.string.already_registered_description)
                            .contentColor(ThemeHandler.getPrimaryText())
                            .backgroundColor(ThemeHandler.getBackground())
                            .positiveText(R.string.yes_button)
                            .positiveColor(ThemeHandler.getAccent())
                            .negativeText(R.string.no_button)
                            .negativeColor(ThemeHandler.getAccent())
                            .onPositive((dialog, which) -> {
                                mMustSkip = true;
                                nextSlide();
                            })
                            .onNegative((dialog, which) -> nextSlide())
                            .show();

                } else nextSlide();
            }

            @Override
            public void onError() {
                mActivity.createSnackbar(getResources().getString(R.string.sign_in_error)).show();
            }
        });
    }
}
