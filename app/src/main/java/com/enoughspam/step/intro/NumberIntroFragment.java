package com.enoughspam.step.intro;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.related.CountryDAO;
import com.enoughspam.step.database.dao.related.PersonalDAO;
import com.enoughspam.step.database.dao.related.PhoneDAO;
import com.enoughspam.step.generalClasses.AutoItemSelectorTextWatcher;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class NumberIntroFragment extends SlideFragment {

    private boolean canGoForward = false;

    private View view;

    private LinearLayout parentView;
    private Spinner spinner;
    private EditText countryCode;
    private EditText phoneNumber;
    private ImageView sendMessage;

    private CountryDAO countryDAO;
    private PhoneDAO phoneDAO;
    private PersonalDAO personalDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_number, container, false);
        countryDAO = new CountryDAO(getActivity());

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        parentView = (LinearLayout) view.findViewById(R.id.intro_number_parent);
        spinner = (Spinner) view.findViewById(R.id.intro_number_spinner);
        countryCode = (EditText) view.findViewById(R.id.intro_number_country_code);
        phoneNumber = (EditText) view.findViewById(R.id.intro_number_phone);
        sendMessage = (ImageView) view.findViewById(R.id.intro_number_go);
    }

    private void initActions() {
        final AutoItemSelectorTextWatcher textWatcher = new AutoItemSelectorTextWatcher(
                getActivity(), spinner, phoneNumber);

        spinner.setAdapter(createSpinnerAdapter());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textWatcher.setPaused(true);
                countryCode.setText(countryDAO.findCodeByName(spinner.getSelectedItem().toString()));
                textWatcher.setPaused(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        countryCode.addTextChangedListener(textWatcher);
        countryCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                countryCode.getBackground().clearColorFilter();
            }
        });

        sendMessage.setOnClickListener(v -> sendMessage());

        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                phoneNumber.getBackground().clearColorFilter();
            }
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        return new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_simple_spinner_dropdown_item,
                countryDAO.getCountryNameList()
        );
    }

    private void sendMessage() {
        parentView.requestFocus();

        int countryInput = 0;
        long phoneInput = 0;
        String error = "";

        // country code edittext input treatment

        try {
            countryInput = Integer.parseInt(countryCode.getText().toString());

        } catch (NumberFormatException e) {

            countryCode.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            if (e.getMessage().equals("For input string: \"\"")) {
                error = getResources().getString(R.string.empty_input_error);
            } else {
                error = getResources().getString(R.string.input_error);
            }
        }

        // phone edittext input treatment

        try {
            phoneInput = Long.parseLong(phoneNumber.getText().toString().replaceAll("\\D+",""));

        } catch (NumberFormatException e) {

            phoneNumber.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            if (e.getMessage().equals("For input string: \"\"")) {
                if (error.isEmpty()) {
                    error = getResources().getString(R.string.empty_input_error);
                } else {
                    error = getResources().getString(R.string.empty_inputs_error);
                }

            } else {

                if (error.isEmpty()) {
                    error = getResources().getString(R.string.input_error);
                } else {
                    error = getResources().getString(R.string.inputs_error);
                }
            }
        }

        if (!error.isEmpty()) {
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (!countryDAO.findByCode(countryInput).isEmpty()) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(("+" + countryInput + phoneInput))) {
                String phoneInputString = String.valueOf(phoneInput);

                int area = Integer.parseInt(phoneInputString.substring(0, 2));
                long local = Long.parseLong(phoneInputString.substring(2, phoneInputString.length()));

                Snackbar.make(view, area + " " + local, Snackbar.LENGTH_LONG).show();

                // Phone phone = new Phone()

                canGoForward = true;
                canGoForward();
                nextSlide();

            } else {

                phoneNumber.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                countryCode.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                Snackbar.make(view, R.string.invalid_number, Snackbar.LENGTH_LONG).show();
            }

        } else {

            countryCode.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            Snackbar.make(view, R.string.country_code_error, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }
}
