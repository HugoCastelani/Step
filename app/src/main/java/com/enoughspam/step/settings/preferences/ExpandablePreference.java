package com.enoughspam.step.settings.preferences;

import android.content.Context;
import android.preference.Preference;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.enoughspam.step.R;
import com.enoughspam.step.settings.ExpandablePreferenceAdapter;

/**
 * Created by Hugo Castelani
 * Date: 20/07/17
 * Time: 23:37
 */

public class ExpandablePreference extends Preference {

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

    @Override
    public void onBindView(View view) {
        super.onBindView(view);

        final AestheticRecyclerView recyclerView = (AestheticRecyclerView) view.findViewById(R.id.preference_expandable_recyclerview);

        final ExpandablePreferenceAdapter adapter = new ExpandablePreferenceAdapter(getContext());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
