package com.enoughspam.step.ui.search;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.ui.profile.ProfileActivity;
import com.enoughspam.step.ui.viewholder.UserViewHolder;
import com.enoughspam.step.util.AnimUtils;
import com.enoughspam.step.util.Listeners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 29/10/17
 * Time: 23:09
 */

public class SearchAdapter extends RecyclerView.Adapter<UserViewHolder> {
    final SearchActivity mActivity;

    final ArrayList<User> userList;

    Listeners.ListListener<User> mListListener;
    Listeners.AnswerListener mAnswerListener;

    public SearchAdapter(@NonNull final SearchActivity activity) {
        mActivity = activity;
        userList = new ArrayList<>();
    }

    public SearchAdapter setQuery(@NonNull final String query) {
        userList.clear();

        if (query.isEmpty()) {
            mActivity.showNoResults();

        } else {

            mActivity.showProgressBar();
            UserDAO.get().filterByUsername(query, getListListener(), getAnswerListener());
        }

        return this;
    }

    private Listeners.ListListener<User> getListListener() {
        if (mListListener == null) {
            mListListener = new Listeners.ListListener<User>() {
                @Override
                public void onItemAdded(@NonNull User user) {
                    for (final User loopUser : userList) {
                        if (loopUser.getKey().equals(user.getKey())) {
                            return;
                        }
                    }

                    userList.add(user);
                    notifyDataSetChanged();
                    mActivity.showRecyclerView();
                }

                @Override
                public void onItemRemoved(@NonNull User user) {
                    for (final User loopUser : userList) {
                        if (loopUser.getKey().equals(user.getKey())) {
                            userList.remove(loopUser);
                        }
                    }
                }
            };
        }

        return mListListener;
    }

    private Listeners.AnswerListener getAnswerListener() {
        if (mAnswerListener == null) {
            mAnswerListener = new Listeners.AnswerListener() {
                @Override public void onAnswerRetrieved() {
                    if (userList.isEmpty()) {
                        mActivity.showNoResults();
                    }
                }

                @Override
                public void onError() {
                    mActivity.showSnackbar(R.string.something_went_wrong);
                }
            };
        }

        return mAnswerListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        final User user = userList.get(position);

        Picasso.with(holder.mUsername.getContext()).load(user.getPicURL())
                .into(holder.mUserPic, new Callback() {
                    @Override
                    public void onSuccess() {
                        AnimUtils.fadeOutFadeIn(holder.mProgressBar, holder.mUserPic);
                    }

                    @Override public void onError() {}
                });

        holder.mUsername.setText("@" + user.getUsername());

        holder.mCardView.setOnClickListener(view -> {
            final Intent intent = new Intent(mActivity, ProfileActivity.class);
            intent.putExtra("user", user);
            mActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
