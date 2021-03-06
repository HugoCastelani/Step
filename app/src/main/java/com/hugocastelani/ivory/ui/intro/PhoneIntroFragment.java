package com.hugocastelani.ivory.ui.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.database.dao.local.LUserDAO;
import com.hugocastelani.ivory.database.dao.wide.PhoneDAO;
import com.hugocastelani.ivory.database.dao.wide.UserPhoneDAO;
import com.hugocastelani.ivory.database.domain.Phone;
import com.hugocastelani.ivory.database.domain.UserPhone;
import com.hugocastelani.ivory.ui.intro.util.MessageCodeHandler;
import com.hugocastelani.ivory.ui.lonelylayout.PhoneFormFragment;
import com.hugocastelani.ivory.util.Listeners;

import java.util.Random;

public final class PhoneIntroFragment extends SlideFragment {

    private boolean mCanGoForward = false;

    private View view;
    private MainIntroActivity mActivity;

    private PhoneFormFragment mPhoneFormFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_phone, container, false);
        mActivity = (MainIntroActivity) getIntroActivity();

        mActivity.setPhoneSlide(this);

        if (mActivity.getLoginSlide().mMustSkip) nextSlide();

        initFragment();

        return view;
    }

    private void initFragment() {
        mPhoneFormFragment = (PhoneFormFragment) getChildFragmentManager()
                .findFragmentByTag("numberFormFragmentTag");
        if (mPhoneFormFragment == null) {
            mPhoneFormFragment = new PhoneFormFragment();
            final FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.ifn_fragment_container, mPhoneFormFragment, "numberFormFragmentTag");
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
                .onPositive((dialog, which) ->

                    PhoneDAO.get().create(phone, new Listeners.ObjectListener<Phone>() {
                        @Override
                        public void onObjectRetrieved(@NonNull Phone retrievedPhone) {

                            final UserPhone userPhone = new UserPhone(LUserDAO.get().getThisUserKey(),
                                    retrievedPhone.getKey(), true, false);

                            userPhone.getUser(null);
                            userPhone.setPhone(retrievedPhone);

                            UserPhoneDAO.get().create(userPhone, new Listeners.UserPhoneAnswerListener() {
                                @Override
                                public void alreadyAdded() {
                                    done();
                                }

                                @Override
                                public void properlyAdded() {
                                    done();
                                }

                                private void done() {
                                    sendMessage();
                                    nextSlide();
                                }

                                @Override public void error() {
                                    mActivity.createSnackbar(R.string.something_went_wrong).show();
                                }
                            }, false);

                        }

                        @Override public void onError() {
                            mActivity.createSnackbar(R.string.something_went_wrong).show();
                        }
                    })

                ).show();
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

    public void prepareNextButton() {
        mActivity.setButtonNextOnClickListener(view1 -> mPhoneFormFragment.validateNumber());
    }

    @Override
    public boolean nextSlide() {
        mCanGoForward = true;
        canGoForward();
        mActivity.getConfirmationSlide().prepareNextButton();
        return super.nextSlide();
    }

    @Override
    public boolean previousSlide() {
        mActivity.resetButtonNextOnClickListener();
        return super.previousSlide();
    }
}
