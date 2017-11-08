package com.enoughspam.step.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.wide.UserDAO;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.util.Listeners;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hugo Castelani
 * Date: 29/10/17
 * Time: 23:09
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    final ArrayList<User> userList;

    public SearchAdapter(@NonNull final String query) {
        userList = new ArrayList<>();

        UserDAO.get().findByKey("-Kwk6q46QWGzxvgi_OW4", new Listeners.UserListener() {
            @Override
            public void onUserRetrieved(@NonNull User retrievedUser) {
                userList.add(retrievedUser);
                notifyDataSetChanged();
            }

            @Override public void onError() {}
        });

        /*UserDAO.get().findByKey(2, retrievedUser -> {
            userList.add(retrievedUser);
            notifyDataSetChanged();
        });*/
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
        Picasso.with(holder.username.getContext()).load(user.getPicURL()).into(holder.userPic);
        holder.username.setText(user.getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {
        final CircleImageView userPic;
        final TextView username;

        public MyViewHolder(View itemView) {
            super(itemView);
            userPic = (CircleImageView) itemView.findViewById(R.id.search_item_profile_user_pic);
            username = (TextView) itemView.findViewById(R.id.search_item_profile_username);
        }
    }
}
