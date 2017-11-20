package com.enoughspam.step.ui.abstracts;

import android.support.v4.app.Fragment;

/**
 * Created by Hugo Castelani
 * Date: 19/11/17
 * Time: 23:37
 */

public abstract class AbstractFragment extends Fragment {
    abstract protected void initViews();

    abstract protected void initActions();
}
