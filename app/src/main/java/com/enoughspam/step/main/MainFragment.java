package com.enoughspam.step.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.util.EndOffsetItemDecoration;
import com.enoughspam.step.util.ListDecorator;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 22:00
 */

public class MainFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        final AestheticRecyclerView recyclerView = (AestheticRecyclerView) view.findViewById(R.id.main_recycler_view);

        final BlockedNumbersAdapter adapter = new BlockedNumbersAdapter(getBlockedNumbersList());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);


        recyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(recyclerView);
    }

    private ArrayList<ArrayList<String>> getBlockedNumbersList() {
        final ArrayList<ArrayList<String>> blockedNumbersList = new ArrayList<>();

        blockedNumbersList.add(new ArrayList<>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Meus números");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(85) 98262-8443");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(66) 99906-5122");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(31) 99690-7113");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(77) 98814-7783");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(21) 99993-9964");

        blockedNumbersList.add(new ArrayList<>());
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

        blockedNumbersList.add(new ArrayList<>());
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("Números de João Boladão");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(15) 98490-9207");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(61) 99493-1778");
        blockedNumbersList.get(blockedNumbersList.size() - 1).add("(96) 99274-9851");

        blockedNumbersList.add(new ArrayList<>());
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

        blockedNumbersList.add(new ArrayList<>());
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

}
