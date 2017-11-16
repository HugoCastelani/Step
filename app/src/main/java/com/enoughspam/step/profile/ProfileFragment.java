package com.enoughspam.step.profile;


import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.enoughspam.step.myprofile.MyProfileAdapter;
import com.enoughspam.step.util.AnimUtils;
import com.enoughspam.step.util.decorator.EndOffsetItemDecoration;
import com.enoughspam.step.util.decorator.ListDecorator;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hugo Castelani
 * Date: 05/10/17
 * Time: 16:20
 */

public class ProfileFragment extends Fragment {
    private ProfileActivity mActivity;
    private View view;

    private User mUser;

    private AestheticRecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private ImageView mPlaceHolder;

    private MaterialDialog mAddingProgressDialog;
    private MaterialDialog mRemovingProgressDialog;
    private MaterialDialog mDeletingProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        mActivity = ((ProfileActivity) getActivity());
        mUser = mActivity.getUser();

        initViews();
        initActions();

        return view;
    }

    private void initViews() {
        // init progress dialog
        mAddingProgressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.profile_adding_following)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        mRemovingProgressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.profile_remove_following)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        mDeletingProgressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.profile_deleting_account)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        // init progress bar
        mProgressBar = (ProgressBar) view.findViewById(R.id.profile_progress_bar);

        // init recycler view
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.profile_recycler_view);

        if (mUser.getKey().equals(LUserDAO.get().getThisUserKey())) {
            mRecyclerView.setAdapter(new MyProfileAdapter(mUser, this));
        } else {
            mRecyclerView.setAdapter(new ProfileAdapter(mUser, this));
        }

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                mRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ListDecorator.addAdaptableMargins(mRecyclerView, 1);


        // init place holder image view
        mPlaceHolder = (ImageView) view.findViewById(R.id.profile_place_holder);
    }

    public void showSnackbar(@StringRes final int message) {
        Snackbar.make(mActivity.findViewById(android.R.id.content),
                getResources().getString(message), Snackbar.LENGTH_LONG).show();
    }

    public void showSnackAndClose(@StringRes final int message) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mActivity.onBackPressed();
            }
        }, Snackbar.LENGTH_LONG);

        Snackbar.make(mActivity.findViewById(android.R.id.content),
                getResources().getString(message), Snackbar.LENGTH_LONG).show();
    }

    public ProfileFragment showRecyclerView() {
        AnimUtils.fadeOutFadeIn(mProgressBar, mRecyclerView);
        return this;
    }

    public ProfileFragment showPlaceHolder() {
        AnimUtils.fadeOutFadeIn(mProgressBar, mPlaceHolder);
        return this;
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

    private void initActions() {
        /*if (isPhoneSectionListEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            mPlaceHolder.setVisibility(View.VISIBLE);
        }*/
    }
}
