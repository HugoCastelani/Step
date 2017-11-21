package com.enoughspam.step.ui.profile;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.ui.intangible.AsynchronousContentFragment;
import com.enoughspam.step.ui.myprofile.MyProfileAdapter;
import com.enoughspam.step.ui.viewholder.UserViewHolder;
import com.enoughspam.step.util.decorator.EndOffsetItemDecoration;
import com.enoughspam.step.util.decorator.ListDecorator;

/**
 * Created by Hugo Castelani
 * Date: 05/10/17
 * Time: 16:20
 */

public final class ProfileFragment extends AsynchronousContentFragment {
    private ProfileActivity mActivity;
    private View view;

    private User mUser;
    private Boolean mIsMyProfile;

    private MyProfileAdapter mMyProfileAdapter;

    private MaterialDialog mAddingProgressDialog;
    private MaterialDialog mRemovingProgressDialog;
    private MaterialDialog mDeletingProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        mActivity = ((ProfileActivity) getActivity());
        mUser = mActivity.getUser();

        mIsMyProfile = mUser.getKey().equals(LUserDAO.get().getThisUserKey());

        initViews();
        initActions();

        return view;
    }

    @Override
    protected void initViews() {
        // init progress dialog
        mAddingProgressDialog = new MaterialDialog.Builder(mActivity)
                .title(R.string.profile_adding_following)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        mRemovingProgressDialog = new MaterialDialog.Builder(mActivity)
                .title(R.string.profile_remove_following)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        mDeletingProgressDialog = new MaterialDialog.Builder(mActivity)
                .title(R.string.profile_deleting_account)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        // init progress bar
        mProgressBar = (ProgressBar) view.findViewById(R.id.pf_progress_bar);

        // init recycler view
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.pf_recycler_view);

        if (mIsMyProfile) {
            mMyProfileAdapter = new MyProfileAdapter(mUser, this);
            mRecyclerView.setAdapter(mMyProfileAdapter);
        } else {
            mRecyclerView.setAdapter(new ProfileAdapter(mUser, this));
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ListDecorator.addAdaptableMargins(mRecyclerView, 1);


        // init place holder image view
        mPlaceHolder = (ImageView) view.findViewById(R.id.pf_place_holder);
    }

    @Override
    protected void initActions() {
        if (mIsMyProfile) {
            final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    mMyProfileAdapter.removeItem(viewHolder.getAdapterPosition());
                }

                @Override
                public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                    if (viewHolder instanceof UserViewHolder
                        && ((UserViewHolder) viewHolder).mIsSwipeable) {
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    } else {
                        return 0;
                    }
                }
            };
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
    }

    public ProfileFragment showAddingProgressDialog() {
        mAddingProgressDialog.show();
        return this;
    }

    public ProfileFragment hideAddingProgressDialog() {
        mAddingProgressDialog.hide();
        return this;
    }

    public ProfileFragment showRemovingProgressDialog() {
        mRemovingProgressDialog.show();
        return this;
    }

    public ProfileFragment hideRemovingProgressDialog() {
        mRemovingProgressDialog.hide();
        return this;
    }

    public ProfileFragment showDeletingProgressDialog() {
        mDeletingProgressDialog.show();
        return this;
    }

    public ProfileFragment hideDeletingProgressDialog() {
        mDeletingProgressDialog.hide();
        return this;
    }
}
