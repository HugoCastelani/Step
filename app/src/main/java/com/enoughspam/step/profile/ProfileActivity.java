package com.enoughspam.step.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.afollestad.aesthetic.AestheticButton;
import com.afollestad.aesthetic.AestheticProgressBar;
import com.afollestad.aesthetic.AestheticTextView;
import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.database.domain.Friendship;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LFriendshipDAO;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.wideDao.FriendshipDAO;
import com.enoughspam.step.util.AnimUtils;
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
    private User mThisUser;

    private CircleImageView mUserPic;
    private AestheticProgressBar mUserPicProgressBar;
    private AestheticTextView mUsername;
    private AestheticTextView mSocialMedia;
    private AestheticButton mButton;

    protected User getUser() {
        return mUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        mUser = (User) getIntent().getExtras().getSerializable("user");
        mThisUser = LUserDAO.getThisUser();

        initViews();
        initActions();
        initFragment();
    }

    @Override
    protected void initViews() {
        // init user photo
        mUserPic = (CircleImageView) findViewById(R.id.profile_circle_view);

        // init progress bar
        mUserPicProgressBar = (AestheticProgressBar) findViewById(R.id.profile_progress_bar);

        // init username text view
        mUsername = (AestheticTextView) findViewById(R.id.profile_user_name);
        mUsername.setText("@" + mUser.getUsername());

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

        // init button
        mButton = (AestheticButton) findViewById(R.id.profile_delete_account);
    }

    @Override
    protected void initActions() {
        // init user photo and progress bar actions
        Picasso.with(getBaseContext()).load(mUser.getPicURL()).into(mUserPic, new Callback() {
            @Override
            public void onSuccess() {
                AnimUtils.fadeOutFadeIn(mUserPicProgressBar, mUserPic);
            }

            @Override public void onError() {}
        });

        // init button actions
        if (LFriendshipDAO.findByIDs(mUser.getID(), mThisUser.getID()) == null) {
            setButtonAsAddable();
        } else {
            setButtonAsRemovable();
        }
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

    private void setButtonAsAddable() {
        mButton.setText(getResources().getString(R.string.profile_add_friend));
        mButton.setOnClickListener(view -> {
            FriendshipDAO.create(new Friendship(mUser, mThisUser));
            setButtonAsRemovable();
        });
    }

    private void setButtonAsRemovable() {
        mButton.setText(getResources().getString(R.string.profile_remove_friend));
        mButton.setOnClickListener(view -> {
            FriendshipDAO.delete(mUser.getID(), mThisUser.getID());
            setButtonAsAddable();
        });
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}
