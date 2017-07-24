package com.enoughspam.step.intro;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.intro.util.MessageCodeHandler;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class ConfirmationIntroFragment extends SlideFragment {

    private boolean mCanGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.intro_fragment_confirmation, container, false);

        final LinearLayout parentView = (LinearLayout) view.findViewById(R.id.intro_confirmation_parent);
        final EditText messageCode = (EditText) view.findViewById(R.id.intro_confirmation_code);
        final ImageView confirm = (ImageView) view.findViewById(R.id.intro_confirmation_go);

        confirm.setOnClickListener(v -> {
            parentView.requestFocus();

            if (messageCode.getText().toString().equals(
                MessageCodeHandler.decryptIt(MessageCodeHandler.sCode))) {

                PersonalDAO personalDAO = new PersonalDAO(getActivity());
                personalDAO.setAllSet(true);

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
