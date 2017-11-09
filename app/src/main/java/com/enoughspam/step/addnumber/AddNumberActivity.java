package com.enoughspam.step.addnumber;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;

import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.domain.Call;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.List;

public class AddNumberActivity extends AbstractActivity {

    private MaterialDialog mProgressDialog;
    private FloatingActionButton mFAB;
    private AddNumberFragment mAddNumberFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_number_activity);

        initToolbar(true);
        initViews();
        initActions();
        initFragment();

        KeyboardVisibilityEvent.setEventListener(
                this,
                isOpen -> {
                    if (isOpen) {
                        mAddNumberFragment.getLayoutManager().setScrollEnabled(false);
                    } else {
                        mAddNumberFragment.getLayoutManager().setScrollEnabled(true);
                    }
                }
        );
    }

    @Override
    protected void initViews() {
        mProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.adding_number)
                .content(R.string.please_wait)
                .progress(true, 0)
                .build();

        mFAB = (FloatingActionButton) findViewById(R.id.add_number_fab);
    }

    @Override
    protected void initActions() {
        mFAB.setOnClickListener(view -> {
            final List<Call> callList = mAddNumberFragment.getAdapter().getSelectedItems();

            for (int i = 0; i < callList.size(); i++) {
                final Phone phone = callList.get(i).getPhone();

                if (phone.getCountry() == null) {
                    mAddNumberFragment.saveNumber(new Phone(phone.getNumber(), phone.getArea().getKey(), "-1"));
                } else {
                    mAddNumberFragment.saveNumber(new Phone(phone.getNumber(), "-1", phone.getCountry().getKey()));
                }
            }
        });
    }

    @Override
    protected void initFragment() {
        // i'll init add number fragment, but won't use it right now
        mAddNumberFragment = (AddNumberFragment) getSupportFragmentManager()
                .findFragmentByTag("addNumberFragmentTag");
        if (mAddNumberFragment == null) {
            mAddNumberFragment = new AddNumberFragment();
            final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.add_number_fragment_container, mAddNumberFragment, "addNumberFragmentTag");
            fragmentTransaction.commit();
        }
    }

    public void showProgressDialog() {
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    public void showFAB() {
        mFAB.show();
    }

    public void hideFAB() {
        mFAB.hide();
    }
}
