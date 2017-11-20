package com.enoughspam.step.ui.intro;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.afollestad.aesthetic.AestheticEditText;
import com.afollestad.aesthetic.AestheticImageView;
import com.enoughspam.step.R;
import com.enoughspam.step.ui.intro.util.MessageCodeHandler;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public final class ConfirmationIntroFragment extends SlideFragment {

    private boolean mCanGoForward = false;

    private static final String KEY_ALL_SET = "intro_all_set";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_confirmation, container, false);

        final LinearLayout parentView = (LinearLayout) view.findViewById(R.id.ifc_parent);
        final AestheticEditText messageCode = (AestheticEditText) view.findViewById(R.id.ifc_code);
        final AestheticImageView confirm = (AestheticImageView) view.findViewById(R.id.ifc_go);

        confirm.setOnClickListener(v -> {
            parentView.requestFocus();

            if (messageCode.getText().toString().equals(
                MessageCodeHandler.decryptIt(MessageCodeHandler.sCode))) {

                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                        .putBoolean(KEY_ALL_SET, true).apply();

                mCanGoForward = true;
                canGoForward();
                nextSlide();

            } else {

                messageCode.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                Snackbar.make(view, getResources().getString(R.string.invalid_code_error),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        messageCode.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                messageCode.getBackground().clearColorFilter();
            }
        });

        return view;
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }
}
