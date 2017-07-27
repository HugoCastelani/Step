package com.enoughspam.step.numberForm;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.blankj.utilcode.util.ToastUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.addNumber.AddNumberFragment;
import com.enoughspam.step.database.dao.CountryDAO;
import com.enoughspam.step.intro.NumberIntroFragment;
import com.enoughspam.step.intro.util.AutoItemSelectorTextWatcher;
import com.enoughspam.step.intro.util.FormHandler;
import com.enoughspam.step.util.ThemeHandler;

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

        mCountryCodeEditText.addTextChangedListener(textWatcher);
        mCountryCodeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mCountryCodeEditText.getBackground().setColorFilter(
                        ThemeHandler.getAccent(), PorterDuff.Mode.SRC_IN);
            }
        });

        mSendMessage.setOnClickListener(v -> {
            ToastUtils.showShort("UPDATE THIS");
            validateNumber();
        });

        mPhoneNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPhoneNumberEditText.getBackground().setColorFilter(
                        ThemeHandler.getAccent(), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        return new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_simple_spinner_dropdown_item,
                CountryDAO.getColumnList(CountryDAO.NAME)
        );
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

        if (CountryDAO.findByCode(Integer.parseInt(mCountryCode)) != null) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(("+" + mCountryCode + mMergePhoneNumber))) {
                confirmNumber();

            } else {

                mPhoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                mCountryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                mError = getResources().getString(R.string.invalid_number);
            }

        } else {

            mCountryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mError = getResources().getString(R.string.country_code_error);
        }

        if (!mError.isEmpty()) {
            Snackbar.make(view, mError, Snackbar.LENGTH_LONG).show();
        }
    }

    private void confirmNumber() {
        if (parentFragment instanceof NumberIntroFragment) {
            ((NumberIntroFragment) parentFragment)
                    .confirmNumber(mCountryCode, mPhoneNumber, mMergePhoneNumber);
        } else {
            ((AddNumberFragment) parentFragment)
                    .confirmNumber(mCountryCode, mPhoneNumber, mMergePhoneNumber);
        }
    }
}
