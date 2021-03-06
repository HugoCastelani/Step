package com.hugocastelani.ivory.ui.addnumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.afollestad.aesthetic.AestheticFab;
import com.afollestad.materialcab.MaterialCab;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.database.domain.Phone;
import com.hugocastelani.ivory.domain.Call;
import com.hugocastelani.ivory.ui.intangible.AbstractActivity;
import com.hugocastelani.ivory.util.Listeners;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;

import java.util.List;

public final class AddNumberActivity extends AbstractActivity implements MaterialCab.Callback {

    private MaterialDialog mNumberProgressDialog;
    private MaterialDialog mNumbersProgressDialog;
    private AestheticFab mFAB;
    private MaterialCab mCAB;
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
        mCAB = new MaterialCab(this, R.id.cab_stub);
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

    public void setSelectedItems(@NonNull final Integer selected) {
        if (selected > 1) {
            if (!getResources().getString(R.string.selected_plural).isEmpty()) {
                mCAB.setTitle(selected + getResources().getString(R.string.selected_plural));
            } else {
                mCAB.setTitle(selected + getResources().getString(R.string.selected));
            }

        } else if (selected == 1) {

            if (mCAB.isActive()) {
                mCAB.setTitle(selected + getResources().getString(R.string.selected));
            } else {
                mCAB.setTitle("1" + getResources().getString(R.string.selected));
                mCAB.start(this);
                mFAB.show();
            }

        } else {

            mCAB.finish();
            mFAB.hide();
        }
    }

    @Override
    public boolean onCabCreated(MaterialCab cab, Menu menu) {
        return true;
    }

    @Override
    public boolean onCabItemClicked(MenuItem item) {
        return false;
    }

    @Override
    public boolean onCabFinished(MaterialCab cab) {
        mAddNumberFragment.getAdapter().deselectAllViews();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mCAB.isActive()) {
            mCAB.finish();
        } else {
            super.onBackPressed();
        }
    }
}
