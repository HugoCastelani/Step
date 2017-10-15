package com.enoughspam.step.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LNotificationDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.util.decorator.EndOffsetItemDecoration;
import com.enoughspam.step.util.decorator.ListDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 05/10/17
 * Time: 16:20
 */

public class ProfileFragment extends Fragment {
    private View view;

    private User mUser;

    private AestheticRecyclerView mRecyclerView;
    private ImageView mPlaceHolder;

    List<PhoneSection> mPhoneSectionList;;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        mUser = ((ProfileActivity) getActivity()).getUser();

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        // init recycler view
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.profile_recycler_view);

        mPhoneSectionList = getBlockedNumberList();

        ProfileAdapter mAdapter = new ProfileAdapter(mPhoneSectionList);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(mRecyclerView);

        // init place holder image view
        mPlaceHolder = (ImageView) view.findViewById(R.id.main_place_holder);
    }

    private void initActions() {
        if (isPhoneSectionListEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private List<PhoneSection> getBlockedNumberList() {
        final List<PhoneSection> phoneSectionList = new ArrayList<>();

        // user's numbers
        phoneSectionList.add(new PhoneSection(
                getResources().getString(R.string.numbers_of) + mUser.getUserName(),
                UserPhoneDAO.getPhoneList(mUser.getID())));

        // friends' numbers
        phoneSectionList.addAll(LNotificationDAO.getFriendsBlockedList(mUser.getID()));

        return phoneSectionList;
    }

    public boolean isPhoneSectionListEmpty() {
        int phoneCount = 0;
        for (int i = 0; i < mPhoneSectionList.size(); i++) {
            phoneCount += mPhoneSectionList.get(i).getPhoneList().size();
        }

        return phoneCount == 0 ? true : false;
    }
}
