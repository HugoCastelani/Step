package com.enoughspam.step.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.domain.User;
import com.enoughspam.step.database.localDao.LNotificationDAO;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.removeItem(viewHolder.getAdapterPosition());
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof BlockedNumbersAdapter.MyViewHolder
                        && ((BlockedNumbersAdapter.MyViewHolder) viewHolder).isSwipeable) {
                    return super.getSwipeDirs(recyclerView, viewHolder);
                } else {
                    return 0;
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(recyclerView);
    }

    private List<PhoneSection> getBlockedNumberList() {
        final List<PhoneSection> phoneSectionList = new ArrayList<>();
        final User user = LUserDAO.getThisUser();

        // user's numbers
        phoneSectionList.add(new PhoneSection(
                getResources().getString(R.string.my_numbers),
                LUserPhoneDAO.getPhoneList()));

        // friends' numbers
        phoneSectionList.addAll(LNotificationDAO.getFriendsBlockedList(user.getId()));

        // official numbers (its ID is 0)
        phoneSectionList.addAll(LNotificationDAO.getFriendsBlockedList(0));

        return phoneSectionList;
    }

}
