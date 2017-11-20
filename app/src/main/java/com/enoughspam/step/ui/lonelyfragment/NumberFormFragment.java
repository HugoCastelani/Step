package com.enoughspam.step.ui.lonelyfragment;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.blankj.utilcode.util.ActivityUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.local.LAreaDAO;
import com.enoughspam.step.database.dao.local.LCountryDAO;
import com.enoughspam.step.database.domain.Area;
import com.enoughspam.step.database.domain.Country;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.ui.abstracts.AbstractFragment;
import com.enoughspam.step.ui.abstracts.SnackbarTrigger;
import com.enoughspam.step.ui.addnumber.AddNumberFragment;
import com.enoughspam.step.ui.intro.NumberIntroFragment;
import com.enoughspam.step.ui.intro.util.AutoItemSelectorTextWatcher;
import com.enoughspam.step.ui.intro.util.FormHandler;
import com.enoughspam.step.util.ThemeHandler;
import com.enoughspam.step.util.blankj.LocationUtils;
import com.google.android.gms.location.LocationServices;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 26/07/17
 * Time: 11:54
 */

public final class NumberFormFragment extends AbstractFragment implements SnackbarTrigger {

    private View view;
    private Fragment mFragment;

    private LinearLayout mParentView;
    private Spinner mSpinner;
    private List<String> mSpinnerList;
    private EditText mCountryCodeEditText;
    private EditText mPhoneNumberEditText;
    private ImageView mSendMessage;

    private String mCountryCode;
    private String mPhoneNumber;
    private String mMergePhoneNumber;
    private Integer mError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.number_form_layout, container, false);

        mFragment = getParentFragment();

        initViews();
        initActions();

        return view;
    }

    @Override
    protected void initViews() {
        mParentView = (LinearLayout) view.findViewById(R.id.nfl_content);
        mSpinner = (Spinner) view.findViewById(R.id.nfl_spinner);
        mCountryCodeEditText = (EditText) view.findViewById(R.id.nfl_country_code);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.nfl_phone);
        mSendMessage = (ImageView) view.findViewById(R.id.nfl_go);
    }

    @Override
    protected void initActions() {
        final FormHandler handler = new FormHandler(mSpinner, mCountryCodeEditText, mPhoneNumberEditText);
        final AutoItemSelectorTextWatcher textWatcher = new AutoItemSelectorTextWatcher(handler);

        mSpinner.setAdapter(createSpinnerAdapter());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textWatcher.setPaused(true);
                final String itemName = mSpinner.getSelectedItem().toString();
                handler.updateCountryCode(itemName);
                handler.updatePhoneNumberMask(LCountryDAO.NAME, itemName);
                textWatcher.setPaused(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // init with current location
        LocationServices.getFusedLocationProviderClient(getActivity())
                .getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        final String countryISO = LocationUtils
                                .getAddress(location.getLatitude(), location.getLongitude())
                                .getCountryCode();

                        final String countryName = LCountryDAO.get()
                                .findByColumn(LCountryDAO.ISO, countryISO).getName();

                        int i;
                        for (i = 0; i < mSpinnerList.size(); i++) {
                            if (mSpinnerList.get(i).contains(countryName)) {
                                mSpinner.setSelection(i);
                                if (mFragment instanceof AddNumberFragment) {
                                    ((AddNumberFragment) mFragment).showRecyclerView();
                                }
                                return;
                            }
                        }
                    }
                });

        mCountryCodeEditText.addTextChangedListener(textWatcher);
        mCountryCodeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mCountryCodeEditText.getBackground().setColorFilter(
                        ThemeHandler.getAccent(), PorterDuff.Mode.SRC_IN);
            }
        });

        mSendMessage.setOnClickListener(v -> validateNumber());

        mPhoneNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPhoneNumberEditText.getBackground().setColorFilter(
                        ThemeHandler.getAccent(), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        mSpinnerList = LCountryDAO.get().getColumnStringList(LCountryDAO.NAME);
        return new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_simple_spinner_dropdown_item,
                mSpinnerList
        );
    }

    public void validateNumber() {
        mParentView.requestFocus();
        mError = -1;

        // country CODE edittext input treatment
        mCountryCode = mCountryCodeEditText.getText().toString();

        if (mCountryCode.isEmpty()) {
            mCountryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mError = R.string.empty_input_error;
        }

        // phone edittext input treatment
        mPhoneNumber = mPhoneNumberEditText.getText().toString();
        mMergePhoneNumber = mPhoneNumber.replaceAll("\\D+","");

        if (mPhoneNumber.isEmpty()) {
            mPhoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            if (mError == -1) {
                mError = R.string.empty_input_error;
            } else {
                mError = R.string.empty_inputs_error;
            }
        }

        if (mError != -1) {
            createSnackbar(mError).show();
            return;
        }

        final Country country = LCountryDAO.get().findByColumn(LCountryDAO.CODE, mCountryCode);

        if (country != null) {
            final int spaceIndex = mPhoneNumber.indexOf(' ');

            Phone phone = null;

            if (spaceIndex != -1) {
                final long phoneNumberL = Long.parseLong(mMergePhoneNumber.substring(spaceIndex));
                final Area area = LAreaDAO.get().findByColumn(LAreaDAO.CODE,
                        mPhoneNumber.substring(0, spaceIndex));

                if (area == null) {
                    mError = R.string.area_code_error;
                    mPhoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                } else {

                    phone = new Phone(phoneNumberL, area.getKey(), "-1");
                }

            } else {

                final long phoneNumberL = Long.parseLong(mMergePhoneNumber);
                phone = new Phone(phoneNumberL, "-1", country.getKey());
            }

            if (mError == -1) {
                confirmNumber(phone);
            }

        } else {

            mCountryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mError = R.string.country_code_error;
        }

        if (mError != -1) {
            createSnackbar(mError).show();
        }
    }

    private void confirmNumber(@NonNull final Phone phone) {
        if (mFragment instanceof NumberIntroFragment) {
            ((NumberIntroFragment) mFragment).confirmNumber(phone);
        } else {
            ((AddNumberFragment) mFragment).confirmNumber(phone);
        }
    }

    @Override
    public Snackbar createSnackbar(final @NonNull @StringRes Integer message) {
        return buildSnackbar(getResources().getString(message));
    }

    @Override
    public Snackbar createSnackbar(final @NonNull String message) {
        return buildSnackbar(message);
    }

    @NonNull
    public Snackbar buildSnackbar(final @NonNull String message) {
        return Snackbar.make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG);
    }

    @Override
    public Snackbar createSnackbarAndClose(final @NonNull @StringRes Integer message) {
        final Snackbar snackbar = createSnackbar(message);

        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                ActivityUtils.finishActivity(getActivity());
            }
        });

        return snackbar;
    }
}
