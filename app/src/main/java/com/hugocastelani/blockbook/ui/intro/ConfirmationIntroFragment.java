package com.hugocastelani.blockbook.ui.intro;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticEditText;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.persistence.HockeyProvider;
import com.hugocastelani.blockbook.ui.intro.util.MessageCodeHandler;
import com.orhanobut.hawk.Hawk;

public final class ConfirmationIntroFragment extends SlideFragment {
    private boolean mCanGoForward = false;

    private View view;
    private MainIntroActivity mActivity;

    private LinearLayout mParentView;
    private AestheticEditText mMessageCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_confirmation, container, false);
        mActivity = (MainIntroActivity) getIntroActivity();

        mActivity.setConfirmationSlide(this);
        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        mParentView = (LinearLayout) view.findViewById(R.id.ifc_parent);
        mMessageCode = (AestheticEditText) view.findViewById(R.id.ifc_code);
    }

    private void initActions() {
        mMessageCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mMessageCode.getBackground().clearColorFilter();
            }
        });
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    public void testCode() {
        mParentView.requestFocus();

        if (mMessageCode.getText().toString().equals(
                MessageCodeHandler.decryptIt(MessageCodeHandler.sCode))) {

            Hawk.put(HockeyProvider.IS_INTRO_ALL_SET, true);

            mCanGoForward = true;
            canGoForward();
            nextSlide();

        } else {

            mMessageCode.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            mActivity.createSnackbar(R.string.invalid_code_error).show();
        }
    }

    public void prepareNextButton() {
        mActivity.setButtonNextOnClickListener(view1 -> testCode());
    }

    @Override
    public boolean nextSlide() {
        mActivity.getReadySlide().prepareNextButton();
        return super.nextSlide();
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
