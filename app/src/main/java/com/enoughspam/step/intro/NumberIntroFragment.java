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
import com.enoughspam.step.intro.util.AutoItemSelectorTextWatcher;
import com.enoughspam.step.intro.util.FormHandler;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class NumberIntroFragment extends SlideFragment {

    private boolean canGoForward = false;

    private View view;

    private LinearLayout parentView;
    private Spinner spinner;
    private EditText countryCodeEditText;
    private EditText phoneNumberEditText;
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
        countryCodeEditText = (EditText) view.findViewById(R.id.intro_number_country_code);
        phoneNumberEditText = (EditText) view.findViewById(R.id.intro_number_phone);
        sendMessage = (ImageView) view.findViewById(R.id.intro_number_go);
    }

    private void initActions() {
        final FormHandler handler = new FormHandler(
                getActivity(), spinner, countryCodeEditText, phoneNumberEditText);
        final AutoItemSelectorTextWatcher textWatcher = new AutoItemSelectorTextWatcher(handler);

        spinner.setAdapter(createSpinnerAdapter());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textWatcher.setPaused(true);
                final String itemName = spinner.getSelectedItem().toString();
                handler.updateCountryCode(itemName);
                handler.updatePhoneNumberMask(itemName);
                textWatcher.setPaused(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        countryCodeEditText.addTextChangedListener(textWatcher);
        countryCodeEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                countryCodeEditText.getBackground().clearColorFilter();
            }
        });

        sendMessage.setOnClickListener(v -> sendMessage());

        phoneNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                phoneNumberEditText.getBackground().clearColorFilter();
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

        int countryCode = 0;
        long phoneNumber = 0;
        String error = "";

        // country code edittext input treatment

        try {
            countryCode = Integer.parseInt(countryCodeEditText.getText().toString());

        } catch (NumberFormatException e) {

            countryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

            if (e.getMessage().equals("For input string: \"\"")) {
                error = getResources().getString(R.string.empty_input_error);
            } else {
                error = getResources().getString(R.string.input_error);
            }
        }

        // phone edittext input treatment

        try {
            phoneNumber = Long.parseLong(phoneNumberEditText.getText().toString().replaceAll("\\D+",""));

        } catch (NumberFormatException e) {

            phoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

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

        if (countryDAO.findByCode(countryCode) != null) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(("+" + countryCode + phoneNumber))) {
                String phoneInputString = String.valueOf(phoneNumber);

                int area = Integer.parseInt(phoneInputString.substring(0, 2));
                long local = Long.parseLong(phoneInputString.substring(2, phoneInputString.length()));

                Snackbar.make(view, area + " " + local, Snackbar.LENGTH_LONG).show();

                // Phone phone = new Phone()

                canGoForward = true;
                canGoForward();
                nextSlide();

            } else {

                phoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                countryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                Snackbar.make(view, R.string.invalid_number, Snackbar.LENGTH_LONG).show();
            }

        } else {

            countryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            Snackbar.make(view, R.string.country_code_error, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }
}
