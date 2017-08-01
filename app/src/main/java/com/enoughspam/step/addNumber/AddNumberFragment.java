package com.enoughspam.step.addNumber;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
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
import com.enoughspam.step.addNumber.extra.CustomLinearLayoutManager;
import com.enoughspam.step.database.dao.AreaDAO;
import com.enoughspam.step.database.dao.CountryDAO;
import com.enoughspam.step.database.dao.PersonalDAO;
import com.enoughspam.step.database.dao.UserPhoneDAO;
import com.enoughspam.step.database.domain.Area;
import com.enoughspam.step.database.domain.Country;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.domain.Call;
import com.enoughspam.step.util.EndOffsetItemDecoration;
import com.enoughspam.step.util.ListDecorator;
import com.enoughspam.step.util.ThemeHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 19:15
 */

public class AddNumberFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private CustomLinearLayoutManager mLayoutManager;
    private AddNumberAdapter mAdapter;

    public CustomLinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public AddNumberAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_number_fragment, container, false);

        initViews();

        return view;
    }

    private void initViews() {
        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.add_number_recycler_view);

        mAdapter = new AddNumberAdapter(getCallList(), this);
        mLayoutManager = new CustomLinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ListDecorator.init(getContext());
        ListDecorator.addAdaptableMargins(mRecyclerView);
    }

    private List<Call> getCallList() {
        Uri calls = Uri.parse("content://call_log/calls");
        Cursor cursor = getContext().getContentResolver().query(calls, null, null, null, null);

        List<Call> callList = new ArrayList<>();
        List<String> numberList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            final String countryCode = cursor.getString(cursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO));

            // doesn't make sense show null numbers or outgoing calls
            if (number == null ||
                    cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) == CallLog.Calls.OUTGOING_TYPE) {
                continue;
            }

            // some names are unknown
            if (name == null || name.equals(number)) {
                name = getResources().getString(R.string.unknown);
            }

            // removing any 0 left
            number = String.valueOf(Long.parseLong(number));

            final Country country = CountryDAO.findByIso(countryCode);
            Phone phone;

            // some countries don't have area code
            if (!country.getMask().isEmpty()) {

                // discover number's area and country
                StringBuilder areaCode = new StringBuilder(50);
                Area area = null;
                final int codeLength = String.valueOf(country.getCode()).length();

                for (int i = 0; i < codeLength; i++) {
                    areaCode.append(number.charAt(i));

                    area = AreaDAO.findByCode(Integer.valueOf(areaCode.toString()));
                    if (area != null && area.getState().getCountry().getId() == country.getId()) {
                        number = number.substring(i + 1, number.length());
                    }
                }

                if (area == null) continue;
                else phone = new Phone(Long.parseLong(number), area);

            } else {

                phone = new Phone(Long.parseLong(number), country);
            }

            // see if number is already blocked
            if (UserPhoneDAO.isAlreadyBlocked(new UserPhone(PersonalDAO.get(), phone, false))) {
                continue;
            }

            final Call call = new Call(name, phone);

            if (!numberList.contains(number)) {
                callList.add(call);
                numberList.add(number);
            }
        }

        return callList;
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

    protected void saveNumber(@NonNull final Phone phone) {
        UserPhoneDAO.create(new UserPhone(PersonalDAO.get(), phone, false));
        getActivity().onBackPressed();
    }
}
