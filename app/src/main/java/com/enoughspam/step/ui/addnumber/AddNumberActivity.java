package com.enoughspam.step.ui.addnumber;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.afollestad.aesthetic.AestheticFab;
import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.domain.Call;
import com.enoughspam.step.ui.intangible.AbstractActivity;
import com.enoughspam.step.util.Listeners;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.List;

public final class AddNumberActivity extends AbstractActivity {

    private MaterialDialog mNumberProgressDialog;
    private MaterialDialog mNumbersProgressDialog;
    private AestheticFab mFAB;
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
        mNumberProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.adding_number)
                .content(R.string.please_wait)
                .progress(true, 0)
                .cancelable(false)
                .build();

        mNumbersProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.adding_numbers)
                .content(R.string.please_wait)
                .progress(true, 0)
                .cancelable(false)
                .build();

        mFAB = (AestheticFab) findViewById(R.id.ana_fab);
    }

    @Override
    protected void initActions() {
        mFAB.setOnClickListener(view -> {
            showNumbersProgressDialog();

            final List<Call> callList = mAddNumberFragment.getAdapter().getSelectedItems();

            final Listeners.AnswerListener listener = new Listeners.AnswerListener() {
                Integer count = 0;

                @Override
                public void onAnswerRetrieved() {
                    mutualAction();
                }
                
                @Override
                public void onError() {
                    createSnackbar(R.string.adding_number_error).show();
                    mutualAction();
                }

                private void mutualAction() {
                    if (++count == callList.size()) {
                        hideNumbersProgressDialog();
                        onBackPressed();
                    }
                }
            };

            for (int i = 0; i < callList.size(); i++) {
                final Phone phone = callList.get(i).getPhone();

                if (phone.getCountry() == null) {
                    mAddNumberFragment.saveNumber(
                            new Phone(phone.getNumber(), phone.getArea().getKey(), "-1"), listener
                    );
                } else {
                    mAddNumberFragment.saveNumber(
                            new Phone(phone.getNumber(), "-1", phone.getCountry().getKey()), listener
                    );
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
            fragmentTransaction.replace(R.id.ana_fragment_container, mAddNumberFragment, "addNumberFragmentTag");
            fragmentTransaction.commit();
        }
    }

    public void showNumberProgressDialog() {
        mNumberProgressDialog.show();
    }

    public void hideNumberProgressDialog() {
        mNumberProgressDialog.hide();
    }

    public void showNumbersProgressDialog() {
        mNumbersProgressDialog.show();
    }

    public void hideNumbersProgressDialog() {
        mNumbersProgressDialog.hide();
    }

    public void showFAB() {
        mFAB.show();
    }

    public void hideFAB() {
        mFAB.hide();
    }
}
