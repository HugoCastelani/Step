package com.enoughspam.step.search;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.profile.ProfileActivity;
import com.enoughspam.step.util.AnimUtils;
import com.enoughspam.step.util.Listeners;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hugo Castelani
 * Date: 29/10/17
 * Time: 23:09
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
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
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item_profile, parent, false);
        return new SearchAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        final CardView mCardView;
        final ProgressBar mProgressBar;
        final CircleImageView mUserPic;
        final TextView mUsername;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.search_item_profile_card);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.search_item_profile_progress_bar);
            mUserPic = (CircleImageView) itemView.findViewById(R.id.search_item_profile_user_pic);
            mUsername = (TextView) itemView.findViewById(R.id.search_item_profile_username);
        }
    }
}