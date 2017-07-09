package com.enoughspam.step.intro;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.enoughspam.step.R;
import com.enoughspam.step.main.MainActivity;
import com.heinrichreimersoftware.materialintro.app.SlideFragment;

public class ReadyIntroFragment extends SlideFragment {

    private boolean canGoForward = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_ready, container, false);

        Button button = (Button) view.findViewById(R.id.intro_ready_button);
        button.setOnClickListener(v -> {
            canGoForward = true;
            canGoForward();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() != null) {
            if (isVisibleToUser) {
                ((MainIntroActivity) getActivity()).setButtonNextVisible(false);
            }
            else {
                ((MainIntroActivity) getActivity()).setButtonNextVisible(true);
            }
        }
    }

    @Override
    public boolean canGoForward() {
        return canGoForward;
    }
}
