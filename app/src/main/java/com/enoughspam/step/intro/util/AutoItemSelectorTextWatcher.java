package com.enoughspam.step.intro.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by hugo
 * Date: 08/07/17
 * Time: 13:18
 */

public class AutoItemSelectorTextWatcher implements TextWatcher {
    private FormHandler handler;
    private boolean paused;

    public AutoItemSelectorTextWatcher(FormHandler handler) {
        this.handler = handler;
        this.paused = false;
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

            handler.updateSpinnerSelection(countryCode);
            handler.updatePhoneNumberMask(countryCode);
        }
    }
}
