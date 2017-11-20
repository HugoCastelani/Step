package com.enoughspam.step.ui.notification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enoughspam.step.R;
import com.enoughspam.step.ui.abstracts.AbstractFragment;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 23:36
 */

public final class NotificationFragment extends AbstractFragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification_fragment, container, false);
        init();
        return view;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initActions() {

    }
}
