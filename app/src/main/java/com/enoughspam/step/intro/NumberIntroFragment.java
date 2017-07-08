package com.enoughspam.step.intro;


import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.related.CountryDAO;
import com.enoughspam.step.generalClasses.AutoItemSelectorTextWatcher;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class NumberIntroFragment extends SlideFragment {

    private boolean canGoForward = false;

    private View view;

    private Spinner spinner;
    private EditText countryCode;
    private EditText phoneNumber;
    private ImageView sendMessage;

    private CountryDAO countryDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_number, container, false);
        countryDAO = new CountryDAO(getActivity());

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        spinner = (Spinner) view.findViewById(R.id.intro_number_spinner);
        countryCode = (EditText) view.findViewById(R.id.intro_number_country_code);
        phoneNumber = (EditText) view.findViewById(R.id.intro_number_phone);
        sendMessage = (ImageView) view.findViewById(R.id.intro_number_go);
    }

    private void initActions() {
        spinner.setAdapter(createSpinnerAdapter());
        countryCode.addTextChangedListener(new AutoItemSelectorTextWatcher(getActivity(), spinner));
        sendMessage.setOnClickListener(v -> sendMessage());
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        return new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                countryDAO.getCountryNameList()
        );
    }

    private void sendMessage() {
        final int countryInput;
        final int phoneInput;

        try {
            countryInput = Integer.parseInt(countryCode.getText().toString());
        } catch (NumberFormatException e) {
            countryCode.setError(getResources().getString(R.string.empty_input_error));
            return;
        }

        try {
            phoneInput = Integer.parseInt(phoneNumber.getText().toString().replaceAll("\\D+",""));

        } catch (NumberFormatException e) {

            if (e.getMessage().equals("For input string: \"\"")) {
                phoneNumber.setError(getResources().getString(R.string.empty_input_error));
            } else {
                phoneNumber.setError(getResources().getString(R.string.input_error));
            }

            return;
        }

        if (!countryDAO.findByCode(countryInput).isEmpty()) {
            if (PhoneNumberUtils.isGlobalPhoneNumber(("+" + countryInput + phoneInput))) {
                Toast.makeText(getActivity(), "+" + countryInput + phoneInput + " is a valid number", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "+" + countryInput + phoneInput + " is not a valid number", Toast.LENGTH_SHORT).show();
            }

        } else {

            countryCode.setError(getResources().getString(R.string.country_code_error));
        }
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }
}
