package com.enoughspam.step.ui.intro.util;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;

import com.azimolabs.maskformatter.MaskFormatter;
import com.enoughspam.step.database.dao.local.LCountryDAO;
import com.enoughspam.step.database.domain.Country;

import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 11/07/17
 * Time: 15:02
 */

public final class FormHandler {
    final private Spinner mSpinner;
    final private EditText mCountryCodeEditText;
    private EditText mPhoneNumberEditText;
    private MaskFormatter mMaskFormatter;

    public FormHandler(@NonNull final Spinner spinner, @NonNull final EditText countryCodeEditText,
                       @NonNull final EditText phoneNumberEditText) {
        mSpinner = spinner;
        mCountryCodeEditText = countryCodeEditText;
        mPhoneNumberEditText = phoneNumberEditText;
    }

    public void updateSpinnerSelection(@NonNull final String countryCode) {
        final Country matchingCountry = LCountryDAO.get().findByColumn(LCountryDAO.CODE, countryCode);

        if (matchingCountry != null) {
            final List<String> countryList = LCountryDAO.get().getColumnStringList(LCountryDAO.NAME);

            final int matchingPosition = countryList.indexOf(matchingCountry.getName());
            mSpinner.setSelection(matchingPosition);
        }
    }

    public void updateCountryCode(@NonNull final String countryName) {
        mCountryCodeEditText.setText(String.valueOf(LCountryDAO.get()
                .findByColumn(LCountryDAO.NAME, countryName).getCode()));
    }

    public void updatePhoneNumberMask(@NonNull final String column, @NonNull final String value) {
        // value might be code or name
        final Country matchingCountry = LCountryDAO.get().findByColumn(column, value);

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
     * When country getThisUser changed, new mark formatter is applied, but you
     * got to type something to update it, and that's method does
     */
    private void finishUpdating() {
        final Editable editable = mPhoneNumberEditText.getText();
        final String number = editable.toString();
        editable.clear();
        editable.insert(0, number);
    }
}
