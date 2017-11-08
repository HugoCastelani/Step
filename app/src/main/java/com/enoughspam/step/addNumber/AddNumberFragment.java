package com.enoughspam.step.addNumber;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
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
import com.enoughspam.step.database.dao.local.LUserDAO;
import com.enoughspam.step.database.dao.local.LUserPhoneDAO;
import com.enoughspam.step.database.dao.wide.PhoneDAO;
import com.enoughspam.step.database.dao.wide.UserPhoneDAO;
import com.enoughspam.step.database.domain.Phone;
import com.enoughspam.step.database.domain.UserPhone;
import com.enoughspam.step.domain.Call;
import com.enoughspam.step.util.Listeners;
import com.enoughspam.step.util.ThemeHandler;
import com.enoughspam.step.util.decorator.EndOffsetItemDecoration;
import com.enoughspam.step.util.decorator.ListDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
            final String iso = cursor.getString(cursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO));

            // check if number already is on the list
            if (!numberList.contains(number)) {
                numberList.add(number);
            } else continue;

            // doesn't make sense show null numbers or outgoing calls
            if (number == null || number.equals("") || cursor.getInt(cursor.getColumnIndex
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
            final UserPhone userPhone = new UserPhone(LUserDAO.get().getThisUserKey(), "", false, false);
            userPhone.setPhone(phone);

            if (LUserPhoneDAO.get().isBlocked(userPhone)) {
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
        PhoneDAO.get().create(phone, new Listeners.PhoneListener() {
            @Override
            public void onPhoneRetrieved(@NonNull Phone retrievedPhone) {
                UserPhoneDAO.get().create(
                        new UserPhone(LUserDAO.get().getThisUserKey(),
                                retrievedPhone.getKey(), false, false),

                        new Listeners.UserPhoneAnswerListener() {
                            @Override
                            public void alreadyAdded() {
                                new MaterialDialog.Builder(getActivity())
                                        .title(R.string.something_went_wrong)
                                        .content(getResources().getString(R.string.number_already_added))
                                        .backgroundColor(ThemeHandler.getBackground())
                                        .positiveText(R.string.yes_button)
                                        .positiveColor(ThemeHandler.getAccent())
                                        .negativeText(R.string.cancel_button)
                                        .negativeColor(ThemeHandler.getAccent())
                                        .onPositive((dialog, which) -> forceSaveNumber(retrievedPhone))
                                        .show();
                            }

                            @Override
                            public void properlyAdded() {
                                getActivity().onBackPressed();
                            }

                            @Override
                            public void error() {
                                showSnackAndClose(R.string.something_went_wrong);
                            }
                        },

                        false
                );
            }

            @Override
            public void onError() {
                showSnackAndClose(R.string.something_went_wrong);
            }
        });
    }

    protected void forceSaveNumber(@NonNull final Phone phone) {
        UserPhoneDAO.get().create(
                new UserPhone(LUserDAO.get().getThisUserKey(), phone.getKey(), false, false),

                new Listeners.UserPhoneAnswerListener() {
                    @Override
                    public void alreadyAdded() {
                        showSnackAndClose(R.string.something_went_wrong);
                    }

                    @Override
                    public void properlyAdded() {
                        getActivity().onBackPressed();
                    }

                    @Override
                    public void error() {
                        showSnackAndClose(R.string.something_went_wrong);
                    }
                },

                true
        );
    }

    private void showSnackAndClose(@StringRes final int message) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().onBackPressed();
            }
        }, Snackbar.LENGTH_LONG);

        Snackbar.make(getActivity().findViewById(android.R.id.content),
                getResources().getString(message), Snackbar.LENGTH_LONG).show();
    }
}
