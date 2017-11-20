package com.enoughspam.step.ui.intro;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.NetworkUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.DAOHandler;
import com.enoughspam.step.util.AnimUtils;
import com.enoughspam.step.util.Listeners;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hugo Castelani
 * Date: 08/11/17
 * Time: 10:38
 */

public class AboutIntroFragment extends SlideFragment {
    private View view;

    private Boolean mCanGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_about, container, false);

        final LinearLayout beforeLoadLayout = view.findViewById(R.id.ifa_before_load);
        final LinearLayout errorLayout = view.findViewById(R.id.ifa_error);
        final LinearLayout afterLoadLayout = view.findViewById(R.id.ifa_after_load);

        if (NetworkUtils.isConnected()) {
            Listeners.AnswerListener listener = new Listeners.AnswerListener() {
                @Override
                public void onAnswerRetrieved() {
                    mCanGoForward = true;
                    canGoForward();
                    AnimUtils.fadeOutFadeIn(beforeLoadLayout, afterLoadLayout);
                }

                @Override public void onError() {
                    showSnackAndClose(R.string.something_went_wrong);
                }
            };

            DAOHandler.syncStaticTables(listener);

        } else {

            AnimUtils.fadeOutFadeIn(beforeLoadLayout, errorLayout);
        }

        return view;
    }

    private void showSnackAndClose(@StringRes final Integer message) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().finishAndRemoveTask();
            }
        }, 3000);

        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getResources().getString(message), 3000).show();
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }
}
