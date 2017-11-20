package com.enoughspam.step.ui.viewholder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.enoughspam.step.R;
import com.enoughspam.step.ui.lonelyfragment.NumberFormFragment;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 22:13
 */

public class NumberFormViewHolder extends SectionedViewHolder {
    public NumberFormViewHolder(View itemView, Fragment fragment) {
        super(itemView);

        NumberFormFragment numberFormFragment = (NumberFormFragment) fragment.getChildFragmentManager()
                .findFragmentByTag("numberFormFragmentTag");

        if (numberFormFragment == null) {
            numberFormFragment = new NumberFormFragment();
            final FragmentTransaction fragmentTransaction = fragment.getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.number_form_fragment_container, numberFormFragment, "numberFormFragmentTag");
            fragmentTransaction.commit();
        }
    }
}
