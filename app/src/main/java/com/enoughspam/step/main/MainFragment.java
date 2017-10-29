package com.enoughspam.step.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.enoughspam.step.R;
import com.enoughspam.step.database.localDao.LFriendshipDAO;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.enoughspam.step.domain.PhoneSection;
import com.enoughspam.step.util.decorator.EndOffsetItemDecoration;
import com.enoughspam.step.util.decorator.ListDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 01/04/17
 * Time: 22:00
 */

public class MainFragment extends Fragment {
    private View view;

    private AestheticRecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private List<PhoneSection> mPhoneSectionList;

    private ImageView mPlaceHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_fragment, container, false);

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        // init recycler view
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.main_recycler_view);

        mPhoneSectionList = getBlockedNumberList();
        mAdapter = new MainAdapter(mPhoneSectionList, this);

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
                if (viewHolder instanceof MainAdapter.MyViewHolder
                        && ((MainAdapter.MyViewHolder) viewHolder).mIsSwipeable) {
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

    private List<PhoneSection> getBlockedNumberList() {
        final List<PhoneSection> phoneSectionList = new ArrayList<>();

        // user's numbers
        phoneSectionList.add(new PhoneSection(
                getResources().getString(R.string.my_numbers),
                LUserPhoneDAO.get().getPhoneList(LUserDAO.get().getThisUser().getID())));

        // friends' numbers
        phoneSectionList.addAll(LFriendshipDAO.get()
                .getFriendsBlockedList(LUserDAO.get().getThisUser().getID()));

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
