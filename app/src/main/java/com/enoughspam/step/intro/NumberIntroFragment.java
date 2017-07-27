package com.enoughspam.step.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.CountryDAO;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.PhoneDAO;
import com.enoughspam.step.database.domain.Phone;
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

    public void confirmNumber(@NonNull final String countryCode, @NonNull final String phoneNumber,
                              @NonNull final String mergePhoneNumber) {

        final String formattedPhoneNumber = "+" + countryCode + " " + phoneNumber + "\n";

        new MaterialDialog.Builder(getActivity())
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .positiveText(R.string.yes_button)
                .negativeText(R.string.cancel_button)
                .onPositive((dialog, which) -> {

                    final int spaceIndex = phoneNumber.indexOf(' ');

                    final Phone phone;

                    if (spaceIndex != -1) {
                        final long phoneNumberL = Long.parseLong(mergePhoneNumber.substring(spaceIndex));
                        final int areaCode = Integer.parseInt(phoneNumber.substring(0, spaceIndex));
                        phone = new Phone(
                                phoneNumberL,
                                areaCode,
                                PersonalDAO.get()
                        );

                    } else {

                        final int countryId = CountryDAO.findByCode(Integer.parseInt(countryCode)).getId();
                        final long phoneNumberL = Long.parseLong(mergePhoneNumber);
                        phone = new Phone(
                                countryId,
                                phoneNumberL,
                                PersonalDAO.get()
                        );
                    }

                    PhoneDAO.create(phone);

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
