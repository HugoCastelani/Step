package com.enoughspam.step.intro.util;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

import com.enoughspam.step.database.dao.local.LCountryDAO;

/**
 * Created by Hugo Castelani
 * Date: 08/07/17
 * Time: 13:18
 */

public class AutoItemSelectorTextWatcher implements TextWatcher {
    final private FormHandler mHandler;
    private boolean mPaused;

    public AutoItemSelectorTextWatcher(@NonNull final FormHandler handler) {
        mHandler = handler;
        mPaused = false;
    }

    public boolean isPaused() {
        return mPaused;
    }

    public void setPaused(final boolean paused) {
        mPaused = paused;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (!isPaused()) {
            mHandler.updateSpinnerSelection(s.toString());
            mHandler.updatePhoneNumberMask(LCountryDAO.CODE, s.toString());
        }
    }
}
