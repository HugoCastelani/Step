package com.hugocastelani.ivory.ui.viewholder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.ui.lonelylayout.PhoneFormFragment;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 22:13
 */

public final class NumberFormViewHolder extends SectionedViewHolder {
    public NumberFormViewHolder(View itemView, Fragment fragment) {
        super(itemView);

        PhoneFormFragment phoneFormFragment = (PhoneFormFragment) fragment.getChildFragmentManager()
                .findFragmentByTag("numberFormFragmentTag");

        if (phoneFormFragment == null) {
            phoneFormFragment = new PhoneFormFragment();
            final FragmentTransaction fragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.inf_fragment_container, phoneFormFragment, "numberFormFragmentTag");
            fragmentTransaction.commit();
        }
    }
}
