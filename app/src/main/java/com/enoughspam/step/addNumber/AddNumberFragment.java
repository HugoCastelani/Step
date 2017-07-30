package com.enoughspam.step.addNumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.UserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.util.EndOffsetItemDecoration;
import com.enoughspam.step.util.ListDecorator;
import com.enoughspam.step.util.ThemeHandler;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 19:15
 */

public class AddNumberFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private CustomLinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_number_fragment, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.add_number_recycler_view);

        final AddNumberAdapter adapter = new AddNumberAdapter(this);
        mLayoutManager = new CustomLinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(mRecyclerView);
    }

    public CustomLinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public void confirmNumber(@NonNull final Phone phone) {

        int countryCode;
        try {
            countryCode = phone.getCountry().getCode();
        } catch (NullPointerException e) {
            countryCode = phone.getArea().getState().getCountry().getCode();
        }

        int areaCode;
        try {
            areaCode = phone.getArea().getCode();
        } catch (NullPointerException e) {
            areaCode = -1;
        }

        final long number = phone.getNumber();

        String formattedPhoneNumber;
        if (areaCode == -1) {
            formattedPhoneNumber = "+" + countryCode + " " + number + "\n";
        } else {
            formattedPhoneNumber = "+" + countryCode + " " + areaCode + " " + number + "\n";
        }

        new MaterialDialog.Builder(getActivity())
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .backgroundColor(ThemeHandler.getBackground())
                .positiveText(R.string.yes_button)
                .positiveColor(ThemeHandler.getAccent())
                .negativeText(R.string.cancel_button)
                .negativeColor(ThemeHandler.getAccent())
                .onPositive((dialog, which) -> saveNumber(phone))
                .show();
    }

    public void saveNumber(@NonNull final Phone phone) {
        UserPhoneDAO.create(new UserPhone(PersonalDAO.get(), phone, false));
        getActivity().onBackPressed();
    }
}
