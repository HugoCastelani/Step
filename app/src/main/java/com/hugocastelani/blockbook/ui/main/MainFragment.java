package com.hugocastelani.blockbook.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.afollestad.aesthetic.AestheticRecyclerView;
import com.blankj.utilcode.util.ConvertUtils;
import com.hugocastelani.blockbook.R;
import com.hugocastelani.blockbook.database.dao.local.LRelationshipDAO;
import com.hugocastelani.blockbook.database.dao.local.LUserDAO;
import com.hugocastelani.blockbook.database.dao.local.LUserPhoneDAO;
import com.hugocastelani.blockbook.domain.PhoneSection;
import com.hugocastelani.blockbook.ui.intangible.AbstractFragment;
import com.hugocastelani.blockbook.ui.viewholder.PhoneHeaderViewHolder;
import com.hugocastelani.blockbook.util.decorator.EndOffsetItemDecoration;
import com.hugocastelani.blockbook.util.decorator.ListDecorator;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 22:00
 */

public final class MainFragment extends AbstractFragment {
    private View view;

    private AestheticRecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<PhoneSection> mPhoneSectionList;

    private ImageView mPlaceHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);

        initViews();
        initActions();

        return view;
    }

    @Override
    protected void initViews() {
        // init recycler view
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.mf_recycler_view);

        mPhoneSectionList = getBlockedNumberList();
        mAdapter = new MainAdapter(mPhoneSectionList, this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ListDecorator.addAdaptableMargins(mRecyclerView, -1);

        // init place holder image view
        mPlaceHolder = (ImageView) view.findViewById(R.id.mf_place_holder);
    }

    @Override
    protected void initActions() {
        // init recycler view's actions
        final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mAdapter.removeItem(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof PhoneHeaderViewHolder
                    && ((PhoneHeaderViewHolder) viewHolder).mIsSwipeable) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    return 0;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        // init place holder's actions
        if (isPhoneSectionListEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<PhoneSection> getBlockedNumberList() {
        final ArrayList<PhoneSection> phoneSectionList = new ArrayList<>();

        // user's numbers
        phoneSectionList.add(new PhoneSection(
                getResources().getString(R.string.my_numbers),
                LUserPhoneDAO.get().getPhoneList(LUserDAO.get().getThisUserKey())));

        // friends' numbers
        phoneSectionList.addAll(LRelationshipDAO.get()
                .getFriendsBlockedList(LUserDAO.get().getThisUserKey()));

        return phoneSectionList;
    }

    public void showPlaceHolder() {
        final AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setDuration(200);

        // setting layout params instead of setting visibility gone to
        // avoid a graphical glitch that happens in section name view
        final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(0, 0);
        mRecyclerView.setLayoutParams(layoutParams);

        mPlaceHolder.setVisibility(View.VISIBLE);

        mPlaceHolder.startAnimation(fadeIn);
    }

    public void showRecyclerView() {
        final AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);

        fadeOut.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeOut.setDuration(200);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                mPlaceHolder.setVisibility(View.GONE);

                // setting layout params instead of setting visibility gone to
                // avoid a graphical glitch that happens in section name view
                final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mRecyclerView.setLayoutParams(layoutParams);
            }
        });

        mPlaceHolder.startAnimation(fadeOut);
    }

    public boolean isPhoneSectionListEmpty() {
        int phoneCount = 0;
        for (int i = 0; i < mPhoneSectionList.size(); i++) {
            phoneCount += mPhoneSectionList.get(i).getPhoneList().size();
        }

        return phoneCount == 0 ? true : false;
    }
}