package com.enoughspam.step.addNumber;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.numberForm.NumberFormFragment;

public class AddNumberActivity extends AbstractActivity {

    private FloatingActionButton mFab;
    private AddNumberFragment mAddNumberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_number_activity);

        initToolbar(true);
        initViews();
        initActions();
        initFragment();
    }

    @Override
    protected void initViews() {
        mFab = (FloatingActionButton) findViewById(R.id.main_fab);
    }

    @Override
    protected void initActions() {
        mFab.setOnClickListener(view ->
                 ((NumberFormFragment) mAddNumberFragment.getChildFragmentManager()
                        .findFragmentByTag("numberFormFragmentTag")).validateNumber()
        );
    }

    @Override
    protected void initFragment() {
        mAddNumberFragment = (AddNumberFragment) getSupportFragmentManager()
                .findFragmentByTag("addNumberFragmentTag");
        if (mAddNumberFragment == null) {
            mAddNumberFragment = new AddNumberFragment();
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.add_number_fragment_container, mAddNumberFragment, "addNumberFragmentTag");
            fragmentTransaction.commit();
        }
    }
}
