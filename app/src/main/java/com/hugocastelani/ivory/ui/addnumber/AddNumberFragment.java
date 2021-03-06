package com.hugocastelani.ivory.ui.addnumber;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.aesthetic.AestheticRecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ConvertUtils;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.database.dao.local.LUserDAO;
import com.hugocastelani.ivory.database.dao.local.LUserPhoneDAO;
import com.hugocastelani.ivory.database.dao.wide.PhoneDAO;
import com.hugocastelani.ivory.database.dao.wide.UserPhoneDAO;
import com.hugocastelani.ivory.database.domain.Phone;
import com.hugocastelani.ivory.database.domain.UserPhone;
import com.hugocastelani.ivory.domain.Call;
import com.hugocastelani.ivory.ui.addnumber.extra.CustomLinearLayoutManager;
import com.hugocastelani.ivory.ui.intangible.AbstractFragment;
import com.hugocastelani.ivory.util.AnimUtils;
import com.hugocastelani.ivory.util.Listeners;
import com.hugocastelani.ivory.util.ThemeHandler;
import com.hugocastelani.ivory.util.decorator.EndOffsetItemDecoration;
import com.hugocastelani.ivory.util.decorator.ListDecorator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugo Castelani
 * Date: 25/07/17
 * Time: 19:15
 */

public final class AddNumberFragment extends AbstractFragment {

    private AddNumberActivity mActivity;

    private View view;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
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
        mActivity = (AddNumberActivity) getActivity();

        initViews();
        initActions();

        return view;
    }

    @Override
    protected void initViews() {
        mProgressBar = (ProgressBar) view.findViewById(R.id.anf_progress_bar);

        mRecyclerView = (AestheticRecyclerView) view.findViewById(R.id.anf_recycler_view);

        mAdapter = new AddNumberAdapter(getCallList(), this);
        mLayoutManager = new CustomLinearLayoutManager(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(ConvertUtils.dp2px(16)));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        ListDecorator.addAdaptableMargins(mRecyclerView, -1);
    }

    @Override
    protected void initActions() {}

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
            userPhone.setUser(LUserDAO.get().getThisUser());
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

        new MaterialDialog.Builder(mActivity)
                .title(R.string.confirmation_dialog_title)
                .content(formattedPhoneNumber + getResources().getString(R.string.confirmation_dialog_content))
                .backgroundColor(ThemeHandler.getBackground())
                .positiveText(R.string.yes_button)
                .positiveColor(ThemeHandler.getAccent())
                .negativeText(R.string.cancel_button)
                .negativeColor(ThemeHandler.getAccent())
                .onPositive((dialog, which) -> {
                        mActivity.showNumberProgressDialog();
                        saveNumber(phone, new Listeners.AnswerListener() {
                            @Override
                            public void onAnswerRetrieved() {
                                mActivity.hideNumberProgressDialog();
                            }

                            @Override
                            public void onError() {
                                mActivity.createSnackbarAndClose(R.string.something_went_wrong);
                            }
                        });
                })
                .show();
    }

    protected void saveNumber(@NonNull final Phone phone,
                              @NonNull final Listeners.AnswerListener listener) {

        PhoneDAO.get().create(phone, new Listeners.ObjectListener<Phone>() {
            @Override
            public void onObjectRetrieved(@NonNull Phone retrievedPhone) {
                UserPhoneDAO.get().create(
                        new UserPhone(LUserDAO.get().getThisUserKey(),
                                retrievedPhone.getKey(), false, false),

                        new Listeners.UserPhoneAnswerListener() {
                            @Override
                            public void alreadyAdded() {
                                new MaterialDialog.Builder(mActivity)
                                        .title(R.string.something_went_wrong)
                                        .content(getResources().getString(R.string.number_already_added))
                                        .backgroundColor(ThemeHandler.getBackground())
                                        .positiveText(R.string.yes_button)
                                        .positiveColor(ThemeHandler.getAccent())
                                        .negativeText(R.string.cancel_button)
                                        .negativeColor(ThemeHandler.getAccent())
                                        .onPositive((dialog, which) ->
                                                forceSaveNumber(retrievedPhone, listener))
                                        .show();
                            }

                            @Override
                            public void properlyAdded() {
                                listener.onAnswerRetrieved();
                            }

                            @Override
                            public void error() {
                                listener.onError();
                            }
                        },

                        false
                );
            }

            @Override
            public void onError() {
                mActivity.createSnackbarAndClose(R.string.something_went_wrong);
            }
        });
    }

    protected void forceSaveNumber(@NonNull final Phone phone,
                                   @NonNull final Listeners.AnswerListener listener) {
        UserPhoneDAO.get().create(
                new UserPhone(LUserDAO.get().getThisUserKey(), phone.getKey(), false, false),

                new Listeners.UserPhoneAnswerListener() {
                    @Override
                    public void alreadyAdded() {
                        listener.onError();
                    }

                    @Override
                    public void properlyAdded() {
                        listener.onAnswerRetrieved();
                    }

                    @Override
                    public void error() {
                        listener.onError();
                    }
                },

                true
        );
    }

    public void showRecyclerView() {
        AnimUtils.fadeOutFadeIn(mProgressBar, mRecyclerView);
    }
}
