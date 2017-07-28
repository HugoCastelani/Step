package com.enoughspam.step.addNumber;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.database.dao.CountryDAO;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.PhoneDAO;
import com.enoughspam.step.database.domain.Phone;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_number_fragment, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        final AestheticRecyclerView recyclerView = (AestheticRecyclerView)
                view.findViewById(R.id.add_number_recycler_view);

        final AddNumberAdapter adapter = new AddNumberAdapter(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        recyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(recyclerView);
    }

    public void confirmNumber(@NonNull final String countryCode, @NonNull final String phoneNumber,
                              @NonNull final String mergePhoneNumber) {

        final String formattedPhoneNumber = "+" + countryCode + " " + phoneNumber + "\n";

        new MaterialDialog.Builder(getActivity())
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .backgroundColor(ThemeHandler.getBackground())
                .positiveText(R.string.yes_button)
                .positiveColor(ThemeHandler.getAccent())
                .negativeText(R.string.cancel_button)
                .negativeColor(ThemeHandler.getAccent())
                .onPositive((dialog, which) -> {

                    final int spaceIndex = phoneNumber.indexOf(' ');

                    final Phone phone;

                    if (spaceIndex != -1) {
                        final long phoneNumberL = Long.parseLong(mergePhoneNumber.substring(spaceIndex));
                        final int areaCode = Integer.parseInt(phoneNumber.substring(0, spaceIndex));
                        phone = new Phone(
                                phoneNumberL,
                                areaCode,
                                PersonalDAO.get()
                        );

                    } else {

                        final int countryId = CountryDAO.findByCode(Integer.parseInt(countryCode)).getId();
                        final long phoneNumberL = Long.parseLong(mergePhoneNumber);
                        phone = new Phone(
                                countryId,
                                phoneNumberL,
                                PersonalDAO.get()
                        );
                    }

                    PhoneDAO.create(phone);

                    getActivity().onBackPressed();
                })
                .show();
    }

}
