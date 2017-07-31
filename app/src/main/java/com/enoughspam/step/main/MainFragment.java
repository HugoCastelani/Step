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
import com.enoughspam.step.database.dao.NotificationDAO;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.UserPhoneDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.util.EndOffsetItemDecoration;
import com.enoughspam.step.util.ListDecorator;

import java.util.ArrayList;
import java.util.List;

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

        final BlockedNumbersAdapter adapter = new BlockedNumbersAdapter(getBlockedNumberList());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);


        recyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(recyclerView);
    }

    private List<PhoneSection> getBlockedNumberList() {
        final List<PhoneSection> phoneSectionList = new ArrayList<>();
        final User user = PersonalDAO.get();

        // user's numbers
        phoneSectionList.add(new PhoneSection(
                getResources().getString(R.string.my_numbers),
                UserPhoneDAO.getUserPhoneList(user.getId())));

        // friends' numbers
        phoneSectionList.addAll(NotificationDAO.getFriendsBlockedList(user.getId(), getContext()));

        // official numbers (its ID is 0)
        phoneSectionList.addAll(NotificationDAO.getFriendsBlockedList(0, getContext()));

        return phoneSectionList;
    }

}
