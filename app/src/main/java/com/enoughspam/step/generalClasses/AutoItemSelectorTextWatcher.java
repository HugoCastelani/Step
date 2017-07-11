package com.enoughspam.step.generalClasses;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;

import com.azimolabs.maskformatter.MaskFormatter;
import com.enoughspam.step.database.dao.related.CountryDAO;
import com.enoughspam.step.database.domains.Country;

import java.util.List;


/**
 * Created by hugo
 * Date: 08/07/17
 * Time: 13:18
 */


public class AutoItemSelectorTextWatcher implements TextWatcher {
    private CountryDAO countryDAO;
    private Spinner spinner;
    private EditText phoneNumber;
    private boolean paused;

    public AutoItemSelectorTextWatcher(Context context, Spinner spinner, EditText phoneNumber) {
        countryDAO = new CountryDAO(context);
        this.spinner = spinner;
        this.phoneNumber = phoneNumber;
        paused = false;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (!isPaused()) {
            int countryCode;

            try {
                countryCode = Integer.parseInt(s.toString());
            } catch (NumberFormatException e) {
                return;
            }

            List<Country> matchingCountry = countryDAO.findByCode(countryCode);

            if (!matchingCountry.isEmpty()) {
                List<String> countryList = countryDAO.getCountryNameList();

                int matchingPosition = countryList.indexOf(matchingCountry.get(0).getName());
                spinner.setSelection(matchingPosition);

                phoneNumber.addTextChangedListener(new MaskFormatter(
                        matchingCountry.get(0).getMask(),
                        phoneNumber
                ));
            }
        }
    }
}
