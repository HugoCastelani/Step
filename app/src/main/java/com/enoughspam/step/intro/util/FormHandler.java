package com.enoughspam.step.intro.util;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;

import com.azimolabs.maskformatter.MaskFormatter;
import com.enoughspam.step.annotation.NonNegative;
import com.enoughspam.step.database.dao.CountryDAO;
import com.enoughspam.step.database.domains.Country;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 11/07/17
 * Time: 15:02
 */

public class FormHandler {
    final private CountryDAO mCountryDAO;
    final private Spinner mSpinner;
    final private EditText mCountryCodeEditText;
    private EditText mPhoneNumberEditText;
    private MaskFormatter mMaskFormatter;

    public FormHandler(@NonNull final Spinner spinner, @NonNull final EditText countryCodeEditText,
                       @NonNull final EditText phoneNumberEditText) {
        mCountryDAO = new CountryDAO(spinner.getContext());
        mSpinner = spinner;
        mCountryCodeEditText = countryCodeEditText;
        mPhoneNumberEditText = phoneNumberEditText;
    }

    public void updateSpinnerSelection(@NonNegative final int countryCode) {
        final Country matchingCountry = mCountryDAO.findByCode(countryCode);

        if (matchingCountry != null) {
            final List<String> countryList = mCountryDAO.getColumnList(mCountryDAO.NAME);

            final int matchingPosition = countryList.indexOf(matchingCountry.getName());
            mSpinner.setSelection(matchingPosition);
        }
    }

    public void updateCountryCode(@NonNull final String countryName) {
        mCountryCodeEditText.setText(mCountryDAO.findCodeByName(countryName));
    }

    public void updatePhoneNumberMask(@NonNull final String countryName) {
        final Country matchingCountry = mCountryDAO.findByName(countryName);

        removeMaskFormatterIfExists();

        if (matchingCountry != null) {
            final String mask = matchingCountry.getMask();
            if (!mask.isEmpty()) {
                mMaskFormatter = new MaskFormatter(matchingCountry.getMask(), mPhoneNumberEditText);
                mPhoneNumberEditText.addTextChangedListener(mMaskFormatter);
            }
        }

        finishUpdating();
    }

    public void updatePhoneNumberMask(@NonNegative final int countryCode) {
        final Country matchingCountry = mCountryDAO.findByCode(countryCode);

        removeMaskFormatterIfExists();

        if (matchingCountry != null) {
            final String mask = matchingCountry.getMask();
            if (!mask.isEmpty()) {
                mMaskFormatter = new MaskFormatter(matchingCountry.getMask(), mPhoneNumberEditText);
                mPhoneNumberEditText.addTextChangedListener(mMaskFormatter);
            }
        }

        finishUpdating();
    }

    private void removeMaskFormatterIfExists() {
        try { mPhoneNumberEditText.removeTextChangedListener(mMaskFormatter);
        } catch (Exception e) {}
    }

    /*
     * When country get changed, new mark formatter is applied, but you
     * got to type something to update it, and that's method does
     */
    private void finishUpdating() {
        final Editable editable = mPhoneNumberEditText.getText();
        final String number = editable.toString();
        editable.clear();
        editable.insert(0, number);
    }
}
