package com.enoughspam.step.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.afollestad.aesthetic.AestheticButton;
import com.afollestad.aesthetic.AestheticProgressBar;
import com.afollestad.aesthetic.AestheticTextView;
import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.database.domain.User;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hugo Castelani
 * Date: 01/10/17
 * Time: 15:30
 */

public class ProfileActivity extends AbstractActivity {
    private User mUser;

    private CircleImageView mCircleImageView;
    private AestheticProgressBar mProgressBar;
    private AestheticTextView mUserName;
    private AestheticTextView mSocialMedia;
    private AestheticButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mUser = (User) getIntent().getExtras().getSerializable("user");

        initViews();
        initActions();
        initFragment();
    }

    @Override
    protected void initViews() {
        // init button
        mButton = (AestheticButton) findViewById(R.id.profile_delete_account);

        // init progress bar
        mProgressBar = (AestheticProgressBar) findViewById(R.id.profile_progress_bar);

        // init user photo
        mCircleImageView = (CircleImageView) findViewById(R.id.profile_circle_view);

        // init username text view
        mUserName = (AestheticTextView) findViewById(R.id.profile_user_name);
        mUserName.setText("@" + mUser.getUserName());

        // init social media text view
        mSocialMedia = (AestheticTextView) findViewById(R.id.profile_social_media);

        CharSequence socialMedia = "";
        switch (mUser.getSocialID().charAt(mUser.getSocialID().length() - 1)) {    // last char
            case '1':    // google code
                socialMedia = getResources().getText(R.string.profile_signed_via_google);
                break;
            default: mSocialMedia.setVisibility(View.GONE);
        }

        mSocialMedia.setText(socialMedia);
    }

    @Override
    protected void initActions() {
        Picasso.with(getBaseContext()).load(mUser.getPhotoURL()).into(mCircleImageView, new Callback() {
            @Override
            public void onSuccess() {
                final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
                final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);

                fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
                fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());

                fadeOut.setDuration(200);
                fadeIn.setDuration(200);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mCircleImageView.setVisibility(View.VISIBLE);
                    }
                });

                mProgressBar.startAnimation(fadeOut);
                mCircleImageView.startAnimation(fadeIn);
            }

            @Override public void onError() {}
        });
    }

    @Override
    protected void initFragment() {
        ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager()
                .findFragmentByTag("profileFragmentTag");

        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.profile_fragment_container, profileFragment, "profileFragmentTag");
            fragmentTransaction.commit();
        }
    }
}
