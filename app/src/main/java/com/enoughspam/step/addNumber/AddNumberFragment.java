package com.enoughspam.step.addNumber;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.util.EndOffsetItemDecoration;
import com.enoughspam.step.util.ListDecorator;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 19:15
 */

public class AddNumberFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_number_fragment, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        final AestheticRecyclerView recyclerView = (AestheticRecyclerView)
                view.findViewById(R.id.add_number_recycler_view);

        final AddNumberAdapter adapter = new AddNumberAdapter();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(recyclerView);
    }

}
