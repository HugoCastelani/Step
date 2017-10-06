package com.enoughspam.step.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enoughspam.step.R;

/**
 * Created by Hugo Castelani
 * Date: 05/10/17
 * Time: 16:20
 */

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

}
