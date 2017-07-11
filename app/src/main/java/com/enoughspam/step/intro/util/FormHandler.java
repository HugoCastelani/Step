package com.enoughspam.step.intro.util;

import android.content.Context;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;

import com.azimolabs.maskformatter.MaskFormatter;
import com.enoughspam.step.database.dao.related.CountryDAO;
import com.enoughspam.step.database.domains.Country;

import java.util.List;

/**
 * Created by hugo
 * Date: 11/07/17
 * Time: 15:02
 */


public class FormHandler {
    private CountryDAO countryDAO;
    private Spinner spinner;
    private EditText countryCodeEditText;
    private EditText phoneNumberEditText;
    private MaskFormatter maskFormatter;

    public FormHandler(Context context, Spinner spinner, EditText countryCode,
                       EditText phoneNumberEditText) {
        this.countryDAO = new CountryDAO(context);
        this.spinner = spinner;
        this.countryCodeEditText = countryCode;
        this.phoneNumberEditText = phoneNumberEditText;
    }

    public void updateSpinnerSelection(int countryCode) {
        Country matchingCountry = countryDAO.findByCode(countryCode);

        if (matchingCountry != null) {
            List<String> countryList = countryDAO.getCountryNameList();

            int matchingPosition = countryList.indexOf(matchingCountry.getName());
            this.spinner.setSelection(matchingPosition);
        }
    }

    public void updateCountryCode(String countryName) {
        countryCodeEditText.setText(countryDAO.findCodeByName(countryName));
    }

    public void updatePhoneNumberMask(String countryName) {
        Country matchingCountry = countryDAO.findByName(countryName);

        removeMaskFormatterIfExists();

        if (matchingCountry != null) {
            String mask = matchingCountry.getMask();
            if (!mask.isEmpty()) {
                this.maskFormatter = new MaskFormatter(matchingCountry.getMask(), phoneNumberEditText);
                phoneNumberEditText.addTextChangedListener(this.maskFormatter);
            }
        }

        finishUpdating();
    }

    public void updatePhoneNumberMask(int countryCode) {
        Country matchingCountry = countryDAO.findByCode(countryCode);

        removeMaskFormatterIfExists();

        if (matchingCountry != null) {
            String mask = matchingCountry.getMask();
            if (!mask.isEmpty()) {
                this.maskFormatter = new MaskFormatter(matchingCountry.getMask(), phoneNumberEditText);
                phoneNumberEditText.addTextChangedListener(this.maskFormatter);
            }
        }

        finishUpdating();
    }

    private void removeMaskFormatterIfExists() {
        try { phoneNumberEditText.removeTextChangedListener(this.maskFormatter);
        } catch (Exception e) {}
    }

    /*
     * When country get changed, new mark formatter is applied, but you got to type something
     * to update it, and that's this method does
     */
    private void finishUpdating() {
        Editable editable = phoneNumberEditText.getText();
        String number = editable.toString();
        editable.clear();
        editable.insert(0, number);
    }
}
