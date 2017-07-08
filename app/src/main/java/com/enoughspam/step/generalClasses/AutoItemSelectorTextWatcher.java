package com.enoughspam.step.generalClasses;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Spinner;

import com.enoughspam.step.database.dao.related.CountryDAO;

import java.util.List;


/**
 * Created by hugo
 * Date: 08/07/17
 * Time: 13:18
 */


public class AutoItemSelectorTextWatcher implements TextWatcher {
    private CountryDAO countryDAO;
    private Spinner spinner;

    public AutoItemSelectorTextWatcher(Context context, Spinner spinner) {
        countryDAO = new CountryDAO(context);
        this.spinner = spinner;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        int countryCode;

        try {
            countryCode = Integer.parseInt(s.toString());
        } catch (NumberFormatException e) {
            return;
        }

        List<String> matchingCountry = countryDAO.findNameByCode(countryCode);

        if (!matchingCountry.isEmpty()) {
            List<String> countryList = countryDAO.getCountryNameList();

            int matchingPosition = countryList.indexOf(matchingCountry.get(0));
            spinner.setSelection(matchingPosition);
        }
    }
}
