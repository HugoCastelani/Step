package com.enoughspam.step.ui.notification;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.aesthetic.AestheticProgressBar;
import com.afollestad.aesthetic.AestheticRecyclerView;
import com.enoughspam.step.R;
import com.enoughspam.step.ui.intangible.AsynchronousContentFragment;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 23:36
 */

public final class NotificationFragment extends AsynchronousContentFragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notification_fragment, container, false);

        initViews();
        initActions();

        return view;
    }

    @Override
    protected void initViews() {
        mProgressBar = (AestheticProgressBar) view.findViewById(R.id.nf_progress_bar);

        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.nf_recycler_view);

        final NotificationAdapter adapter = new NotificationAdapter(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

        mPlaceHolder = (ImageView) view.findViewById(R.id.nf_place_holder);
    }

    @Override
    protected void initActions() {

    }
}
