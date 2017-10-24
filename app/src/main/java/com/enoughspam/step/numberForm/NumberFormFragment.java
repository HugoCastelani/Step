package com.enoughspam.step.numberForm;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.enoughspam.step.R;
import com.enoughspam.step.addNumber.AddNumberFragment;
import com.enoughspam.step.database.domain.Area;
import com.enoughspam.step.database.domain.Country;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.localDao.AreaDAO;
import com.enoughspam.step.database.localDao.CountryDAO;
import com.enoughspam.step.intro.NumberIntroFragment;
import com.enoughspam.step.intro.util.AutoItemSelectorTextWatcher;
import com.enoughspam.step.intro.util.FormHandler;
import com.enoughspam.step.util.ThemeHandler;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hugo Castelani
 * Date: 26/07/17
 * Time: 11:54
 */

public class NumberFormFragment extends Fragment {
    private View view;
    private Fragment parentFragment;

    private LinearLayout mParentView;
    private Spinner mSpinner;
    private List<String> mSpinnerList;
    private EditText mCountryCodeEditText;
    private EditText mPhoneNumberEditText;
    private ImageView mSendMessage;

    private String mCountryCode;
    private String mPhoneNumber;
    private String mMergePhoneNumber;
    private String mError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.number_form_layout, container, false);
        parentFragment = getParentFragment();

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        mParentView = (LinearLayout) view.findViewById(R.id.number_form_parent);
        mSpinner = (Spinner) view.findViewById(R.id.number_form_spinner);
        mCountryCodeEditText = (EditText) view.findViewById(R.id.number_form_country_code);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.number_form_phone);
        mSendMessage = (ImageView) view.findViewById(R.id.number_form_go);
    }

    private void initActions() {
        final FormHandler handler = new FormHandler(mSpinner, mCountryCodeEditText, mPhoneNumberEditText);
        final AutoItemSelectorTextWatcher textWatcher = new AutoItemSelectorTextWatcher(handler);

        mSpinner.setAdapter(createSpinnerAdapter());
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textWatcher.setPaused(true);
                final String itemName = mSpinner.getSelectedItem().toString();
                handler.updateCountryCode(itemName);
                handler.updatePhoneNumberMask(itemName);
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
                        final String countryName = CountryDAO.findByISO(getCountryISO(location)).getName();

                        int i;
                        for (i = 0; i < mSpinnerList.size(); i++) {
                            if (mSpinnerList.get(i).contains(countryName)) {
                                mSpinner.setSelection(i);
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
        mSpinnerList = CountryDAO.getColumnList(CountryDAO.NAME);
        return new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_simple_spinner_dropdown_item,
                mSpinnerList
        );
    }

    private String getCountryISO(@NonNull final Location location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryCode();
            }
        } catch (IOException ignored) {}
        return null;
    }

    public void validateNumber() {
        mParentView.requestFocus();
        mError = "";

        // country sCode edittext input treatment
        mCountryCode = mCountryCodeEditText.getText().toString();

        if (mCountryCode.isEmpty()) {
            mCountryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mError = getResources().getString(R.string.empty_input_error);
        }

        // phone edittext input treatment
        mPhoneNumber = mPhoneNumberEditText.getText().toString();
        mMergePhoneNumber = mPhoneNumber.replaceAll("\\D+","");

        if (mPhoneNumber.isEmpty()) {
            mPhoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            if (mError.isEmpty()) {
                mError = getResources().getString(R.string.empty_input_error);
            } else {
                mError = getResources().getString(R.string.empty_inputs_error);
            }
        }

        if (!mError.isEmpty()) {
            Snackbar.make(view, mError, Snackbar.LENGTH_LONG).show();
            return;
        }

        final Country country = CountryDAO.findByCode(Integer.parseInt(mCountryCode));

        if (country != null) {
            final int spaceIndex = mPhoneNumber.indexOf(' ');

            Phone phone = null;

            if (spaceIndex != -1) {
                final long phoneNumberL = Long.parseLong(mMergePhoneNumber.substring(spaceIndex));
                final Area area = AreaDAO.findByCode(Integer.parseInt(mPhoneNumber.substring(0, spaceIndex)));

                if (area == null) {
                    mError = getResources().getString(R.string.area_code_error);
                    mPhoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

                } else {

                    phone = new Phone(
                            phoneNumberL,
                            area
                    );
                }

            } else {

                final long phoneNumberL = Long.parseLong(mMergePhoneNumber);
                phone = new Phone(
                        phoneNumberL,
                        country
                );
            }

            if (mError.isEmpty()) {
                confirmNumber(phone);
            }

        } else {

            mCountryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mError = getResources().getString(R.string.country_code_error);
        }

        if (!mError.isEmpty()) {
            Snackbar.make(view, mError, Snackbar.LENGTH_LONG).show();
        }
    }

    private void confirmNumber(@NonNull final Phone phone) {
        if (parentFragment instanceof NumberIntroFragment) {
            ((NumberIntroFragment) parentFragment)
                    .confirmNumber(phone);
        } else {
            ((AddNumberFragment) parentFragment)
                    .confirmNumber(phone);
        }
    }
}
