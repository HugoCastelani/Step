package com.enoughspam.step.intro;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.CountryDAO;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.PhoneDAO;
import com.enoughspam.step.database.domains.Phone;
import com.enoughspam.step.intro.util.AutoItemSelectorTextWatcher;
import com.enoughspam.step.intro.util.FormHandler;
import com.enoughspam.step.intro.util.MessageCodeHandler;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import java.util.Random;

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

    private String countryCode;
    private String phoneNumber;
    private String mergePhoneNumber;
    private String error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_number, container, false);
        countryDAO = new CountryDAO(getActivity());
        phoneDAO = new PhoneDAO(getActivity());
        personalDAO = new PersonalDAO(getActivity());

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

        sendMessage.setOnClickListener(v -> validateNumber());

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

    private void validateNumber() {
        parentView.requestFocus();
        error = "";

        // country code edittext input treatment
        countryCode = countryCodeEditText.getText().toString();

        if (countryCode.isEmpty()) {
            countryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            error = getResources().getString(R.string.empty_input_error);
        }

        // phone edittext input treatment
        phoneNumber = phoneNumberEditText.getText().toString();
        mergePhoneNumber = phoneNumber.replaceAll("\\D+","");

        if (phoneNumber.isEmpty()) {
            phoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            if (error.isEmpty()) {
                error = getResources().getString(R.string.empty_input_error);
            } else {
                error = getResources().getString(R.string.empty_inputs_error);
            }
        }

        if (!error.isEmpty()) {
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
            return;
        }

        if (countryDAO.findByCode(Integer.parseInt(countryCode)) != null) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(("+" + countryCode + mergePhoneNumber))) {
                confirmNumber();

            } else {

                phoneNumberEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                countryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                error = getResources().getString(R.string.invalid_number);
            }

        } else {

            countryCodeEditText.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            error = getResources().getString(R.string.country_code_error);
        }

        if (!error.isEmpty()) {
            Snackbar.make(view, error, Snackbar.LENGTH_LONG).show();
        }
    }

    private void confirmNumber() {
        String formattedPhoneNumber = "+" + countryCode + " " + phoneNumber + "\n";

        new MaterialDialog.Builder(getActivity())
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .positiveText(R.string.yes_button)
                .negativeText(R.string.cancel_button)
                .onPositive((dialog, which) -> {

                    int spaceIndex = this.phoneNumber.indexOf(' ');

                    Phone phone;

                    if (spaceIndex != -1) {
                        long phoneNumberL = Long.parseLong(mergePhoneNumber.substring(spaceIndex));
                        int areaCode = Integer.parseInt(this.phoneNumber.substring(0, spaceIndex));
                        phone = new Phone(
                                phoneNumberL,
                                areaCode,
                                personalDAO.get()
                        );

                    } else {

                        int countryId = countryDAO.findByCode(Integer.parseInt(countryCode)).getId();
                        long phoneNumberL = Long.parseLong(mergePhoneNumber);
                        phone = new Phone(
                                countryId,
                                phoneNumberL,
                                personalDAO.get()
                        );
                    }

                    phoneDAO.create(phone);

                    sendMessage();
                    canGoForward = true;
                    canGoForward();
                    nextSlide();

                })
                .show();
    }

    private void sendMessage() {
        final int MAX_VALUE = 999999;
        final int MIN_VALUE = 111111;
        //String code = Integer.toString(new Random().nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE);
        String code = Integer.toString(123456);
        String cryptoPass = Integer.toString(new Random().nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE);

        MessageCodeHandler.cryptoPass = cryptoPass;
        MessageCodeHandler.code = MessageCodeHandler.encryptIt(code);

        // PhoneUtils.sendSmsSilent(countryCode + mergePhoneNumber, code);
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
