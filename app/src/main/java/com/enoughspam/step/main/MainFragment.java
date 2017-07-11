package com.enoughspam.step.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.notRelated.ThemeDAO;
import com.enoughspam.step.database.domains.ThemeData;
import com.enoughspam.step.util.EndOffsetItemDecoration;
import com.enoughspam.step.util.LeftRightOffsetItemDecoration;
import com.enoughspam.step.util.ScreenInfo;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import java.util.ArrayList;

// Created by Hugo on 01/04/17, 22:00

public class MainFragment extends Fragment {

    // data access objects
    private ThemeDAO themeDAO;
    private ThemeData themeData;

    // theme vars
    private int accentColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // preparing theme
        themeDAO = new ThemeDAO(view.getContext());
        themeData = themeDAO.getThemeData();
        accentColor = Color.parseColor(themeData.getAccentColor());

        // getting tap target view ready
        TapTargetView.showFor(getActivity(),
                TapTarget.forView(fab,
                        "O quê esse FAB faz?",
                        "No momento, só chama uma snackbar inútil...")
                        //.outerCircleColor(themeData.isDark() ? darkAccentColor : accentColor)
                        .outerCircleColor(R.color.md_cyan_500)
                        .textTypeface(Typeface.DEFAULT)
                        .targetCircleColor(themeData.isDark() ? R.color.colorPrimaryInverse : R.color.colorPrimary)
                        .transparentTarget(true),
                null);

        // getting recycler view ready
        AestheticRecyclerView recyclerView = (AestheticRecyclerView) view.findViewById(R.id.blocked_recyclerview);
        BlockedNumbersAdapter adapter = new BlockedNumbersAdapter(getBlockedNumbersList(), getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        decorateRecycler(recyclerView);

        return view;
    }

    private ArrayList<ArrayList<String>> getBlockedNumbersList() {
        ArrayList<ArrayList<String>> blockedNumbersList = new ArrayList<>();

        blockedNumbersList.add(new ArrayList<String>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Meus números");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(85) 98262-8443");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(66) 99906-5122");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(31) 99690-7113");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(77) 98814-7783");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(21) 99993-9964");

        blockedNumbersList.add(new ArrayList<String>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Números de Maria");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(95) 98539-9723");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(68) 98684-7763");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(38) 99711-4592");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(68) 98684-7763");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(79) 99871-4874");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(68) 98684-7763");

        blockedNumbersList.add(new ArrayList<String>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Números de João Boladão");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(15) 98490-9207");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(61) 99493-1778");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(96) 99274-9851");

        blockedNumbersList.add(new ArrayList<String>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Números de Septuspxío");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(15) 98490-9207");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(61) 99493-1778");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(15) 98490-9207");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(96) 99274-9851");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(67) 99865-8109");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(82) 98570-7099");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(67) 99865-8109");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(95) 98539-9723");

        blockedNumbersList.add(new ArrayList<String>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Números da comunidade oficial");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(15) 98490-9207");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(61) 99493-1778");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(15) 98490-9207");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(96) 99274-9851");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(67) 99865-8109");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(82) 98570-7099");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(67) 99865-8109");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(95) 98539-9723");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(68) 98684-7763");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(38) 99711-4592");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(68) 98684-7763");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(86) 99585-6067");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(79) 99871-4874");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(68) 98684-7763");

        return blockedNumbersList;
    }

    private void decorateRecycler(RecyclerView recyclerView) {
        ScreenInfo screenInfo = new ScreenInfo(getActivity());

        if (screenInfo.isScreenLarge()) {
            if (screenInfo.getOrientarion() == 1) {
                recyclerView.addItemDecoration(new LeftRightOffsetItemDecoration(screenInfo.getFabAlignMargin()));

            } else {

                recyclerView.addItemDecoration(new LeftRightOffsetItemDecoration(screenInfo.getTenPercentageMargin()));
            }

        } else {

            if (screenInfo.getOrientarion() == 2) {
                recyclerView.addItemDecoration(new LeftRightOffsetItemDecoration(screenInfo.getFabAlignMargin()));

            }
        }

        // screenInfo.getPixelDensity() * 12 should have same size that item_blocker margin's, but it don't
        recyclerView.addItemDecoration(new EndOffsetItemDecoration((int) screenInfo.getPixelDensity() * 16));
    }

}
