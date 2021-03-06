package com.hugocastelani.ivory.ui.settings.preference;

import android.content.Context;
import android.graphics.PorterDuff;
import android.preference.Preference;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.andexert.expandablelayout.library.ExpandableLayout;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.ui.settings.ExpandablePreferenceAdapter;
import com.hugocastelani.ivory.util.ThemeHandler;

/**
 * Created by Hugo Castelani
 * Date: 20/07/17
 * Time: 23:37
 */

public final class ExpandablePreference extends Preference {

    public ExpandablePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ExpandablePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ExpandablePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandablePreference(Context context) {
        super(context);
    }

    private ImageView mExpand;
    private ExpandableLayout mExpandableLayout;

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        mExpand = (ImageView) view.findViewById(R.id.pe_drawable);
        mExpand.setColorFilter(ThemeHandler.getSecondaryText(), PorterDuff.Mode.SRC_IN);

        mExpandableLayout = (ExpandableLayout) view.findViewById(R.id.pe_layout);
        mExpandableLayout.setOnStartShowingListener(expandableLayout -> rotateExpand());
        mExpandableLayout.setOnStartHidingListener(expandableLayout -> rotateExpand());

        final AestheticRecyclerView recyclerView = (AestheticRecyclerView)
                view.findViewById(R.id.pe_recycler_view);

        final ExpandablePreferenceAdapter adapter = new ExpandablePreferenceAdapter(view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void rotateExpand() {
        mExpandableLayout.setEnabled(false);
        mExpand.animate().rotationBy(180F).setInterpolator(new AccelerateDecelerateInterpolator())
                .setDuration(300).start();
        mExpandableLayout.setEnabled(true);
    }
}
