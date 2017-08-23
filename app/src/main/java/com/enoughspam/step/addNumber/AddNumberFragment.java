package com.enoughspam.step.addNumber;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BlockedNumberContract.BlockedNumbers;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.TelecomManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.enoughspam.step.R;
import com.enoughspam.step.addNumber.extra.CustomLinearLayoutManager;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.database.localDao.LUserDAO;
import com.enoughspam.step.database.localDao.LUserPhoneDAO;
import com.enoughspam.step.database.wideDao.UserPhoneDAO;
import com.enoughspam.step.domain.Call;
import com.enoughspam.step.util.ThemeHandler;
import com.enoughspam.step.util.VariousUtils;
import com.enoughspam.step.util.decorator.EndOffsetItemDecoration;
import com.enoughspam.step.util.decorator.ListDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 19:15
 */

public class AddNumberFragment extends Fragment {
    private ContentValues mValues;

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

        // this variable is initialized here to speed up saveNumber() method
        mValues = new ContentValues();

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
            final String iso = cursor.getString(cursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO));

            // check if number already is on the list
            if (!numberList.contains(number)) {
                numberList.add(number);
            } else continue;

            // doesn't make sense show null numbers or outgoing calls
            if (number == null || cursor.getInt(cursor.getColumnIndex
                    (CallLog.Calls.TYPE)) == CallLog.Calls.OUTGOING_TYPE) {
                continue;
            }

            // some names are unknown
            if (name == null || name.equals(number)) {
                name = getResources().getString(R.string.unknown);
            }

            Phone phone = Phone.generateObject(number, iso);
            if (phone == null) continue;    // number doesn't exist

            // check if number is already blocked
            if (LUserPhoneDAO.isBlocked(new UserPhone(LUserDAO.getThisUser(), phone, false))) {
                continue;
            }

            callList.add(new Call(name, phone));
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
        UserPhoneDAO.create(new UserPhone(LUserDAO.getThisUser(), phone, false));

        // add to BlockedNumberProvider if it's nougat or above
        if (VariousUtils.isAboveMarshmallow()) {
            TelecomManager telecomManager = (TelecomManager)
                    getContext().getSystemService(getContext().TELECOM_SERVICE);

            getContext().startActivity(telecomManager.createManageBlockedNumbersIntent(), null);

            mValues.clear();
            mValues.put(BlockedNumbers.COLUMN_ORIGINAL_NUMBER, phone.toString());
            getActivity().getContentResolver()
                    .insert(BlockedNumbers.CONTENT_URI, mValues);

        }

        getActivity().onBackPressed();
    }
}
