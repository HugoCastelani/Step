package com.enoughspam.step.profile;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.database.domain.User;

/**
 * Created by Hugo Castelani
 * Date: 01/10/17
 * Time: 15:30
 */

public class ProfileActivity extends AbstractActivity {
    private Boolean mRanOnRestart = false;

    private User mUser;

    protected User getUser() {
        return mUser;
    }

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
    protected void initViews() {}

    @Override
    protected void initActions() {}

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

    // both methods below work to reload activity when user opens another ProfileActivity

    @Override
    protected void onRestart() {
        super.onRestart();
        mRanOnRestart = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRanOnRestart) {
            recreate();
        }
    }

    public void onBackPressed(View view) {
        super.onBackPressed();
    }
}
