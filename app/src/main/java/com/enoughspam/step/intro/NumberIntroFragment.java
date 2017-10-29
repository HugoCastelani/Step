package com.enoughspam.step.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;
import com.enoughspam.step.intro.util.MessageCodeHandler;
import com.enoughspam.step.numberForm.NumberFormFragment;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import java.util.Random;

public class NumberIntroFragment extends SlideFragment {

    private boolean mCanGoForward = false;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_number, container, false);

        initFragment();

        return view;
    }

    private void initFragment() {
        NumberFormFragment numberFormFragment = (NumberFormFragment) getChildFragmentManager()
                .findFragmentByTag("numberFormFragmentTag");
        if (numberFormFragment == null) {
            numberFormFragment = new NumberFormFragment();
            final FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.number_form_fragment_container, numberFormFragment, "numberFormFragmentTag");
            fragmentTransaction.commit();
        }
    }

    public void confirmNumber(@NonNull final Phone phone) {

        int countryCode;
        try {
            countryCode = phone.getCountry().getCode();
        } catch (NullPointerException e) {
            countryCode = phone.getArea().getState().getCountry().getCode();
        }

        int areaCode;
        try {
            areaCode = phone.getArea().getCode();
        } catch (NullPointerException e) {
            areaCode = -1;
        }

        final long number = phone.getNumber();

        String formattedPhoneNumber;
        if (areaCode == -1) {
            formattedPhoneNumber = "+" + countryCode + " " + number + "\n";
        } else {
            formattedPhoneNumber = "+" + countryCode + " " + areaCode + " " + number + "\n";
        }

        new MaterialDialog.Builder(getActivity())
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .positiveText(R.string.yes_button)
                .negativeText(R.string.cancel_button)
                .onPositive((dialog, which) -> {

                    UserPhoneDAO.get().create(new UserPhone(LUserDAO.get().getThisUser(), phone, true));

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
        //String CODE = Integer.toString(new Random().nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE);
        final String code = Integer.toString(123456);
        final String cryptoPass = Integer.toString(new Random().nextInt((MAX_VALUE - MIN_VALUE) + 1) + MIN_VALUE);

        MessageCodeHandler.sPassword = cryptoPass;
        MessageCodeHandler.sCode = MessageCodeHandler.encryptIt(code);

        // PhoneUtils.sendSmsSilent(mCountryCode + mMergePhoneNumber, CODE);
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
