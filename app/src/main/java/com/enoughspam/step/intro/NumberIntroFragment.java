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

    private boolean mCanGoForward = false;

    private View view;

    private LinearLayout mParentView;
    private Spinner mSpinner;
    private EditText mCountryCodeEditText;
    private EditText mPhoneNumberEditText;
    private ImageView mSendMessage;

    private CountryDAO mCountryDAO;
    private PhoneDAO mPhoneDAO;
    private PersonalDAO mPersonalDAO;

    private String mCountryCode;
    private String mPhoneNumber;
    private String mMergePhoneNumber;
    private String mError;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_number, container, false);
        mCountryDAO = new CountryDAO(getActivity());
        mPhoneDAO = new PhoneDAO(getActivity());
        mPersonalDAO = new PersonalDAO(getActivity());

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        mParentView = (LinearLayout) view.findViewById(R.id.intro_number_parent);
        mSpinner = (Spinner) view.findViewById(R.id.intro_number_spinner);
        mCountryCodeEditText = (EditText) view.findViewById(R.id.intro_number_country_code);
        mPhoneNumberEditText = (EditText) view.findViewById(R.id.intro_number_phone);
        mSendMessage = (ImageView) view.findViewById(R.id.intro_number_go);
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
                mCountryCodeEditText.getBackground().clearColorFilter();
            }
        });

        mSendMessage.setOnClickListener(v -> validateNumber());

        mPhoneNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mPhoneNumberEditText.getBackground().clearColorFilter();
            }
        });
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        return new ArrayAdapter<>(
                getActivity(),
                R.layout.custom_simple_spinner_dropdown_item,
                mCountryDAO.getColumnList(mCountryDAO.NAME)
        );
    }

    private void validateNumber() {
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

        if (mCountryDAO.findByCode(Integer.parseInt(mCountryCode)) != null) {
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
        final String formattedPhoneNumber = "+" + mCountryCode + " " + mPhoneNumber + "\n";

        new MaterialDialog.Builder(getActivity())
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .positiveText(R.string.yes_button)
                .negativeText(R.string.cancel_button)
                .onPositive((dialog, which) -> {

                    final int spaceIndex = mPhoneNumber.indexOf(' ');

                    final Phone phone;

                    if (spaceIndex != -1) {
                        final long phoneNumberL = Long.parseLong(mMergePhoneNumber.substring(spaceIndex));
                        final int areaCode = Integer.parseInt(mPhoneNumber.substring(0, spaceIndex));
                        phone = new Phone(
                                phoneNumberL,
                                areaCode,
                                mPersonalDAO.get()
                        );

                    } else {

                        final int countryId = mCountryDAO.findByCode(Integer.parseInt(mCountryCode)).getId();
                        final long phoneNumberL = Long.parseLong(mMergePhoneNumber);
                        phone = new Phone(
                                countryId,
                                phoneNumberL,
                                mPersonalDAO.get()
                        );
                    }

                    mPhoneDAO.create(phone);

                    sendMessage();
                    mCanGoForward = true;
                    canGoForward();
                    nextSlide();

                })
                .show();
    }

    private void sendMessage() {
        final int MAX_VALUE = 999999;
        final int MIN_VALUE = 111111;
        //String sCode = Integer.toString(new Random().nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE);
        final String code = Integer.toString(123456);
        final String cryptoPass = Integer.toString(new Random().nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE);

        MessageCodeHandler.sPassword = cryptoPass;
        MessageCodeHandler.sCode = MessageCodeHandler.encryptIt(code);

        // PhoneUtils.sendSmsSilent(mCountryCode + mMergePhoneNumber, sCode);
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
