package com.hugocastelani.ivory.ui.intro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.heinrichreimersoftware.materialintro.app.SlideFragment;
import com.hugocastelani.ivory.R;
import com.hugocastelani.ivory.database.dao.local.LUserDAO;
import com.hugocastelani.ivory.database.dao.wide.UserDAO;
import com.hugocastelani.ivory.database.domain.User;
import com.hugocastelani.ivory.util.Listeners;

import java.util.ArrayList;

/**
 * Created by Hugo Castelani
 * Date: 08/12/17
 * Time: 01:32
 */

final public class UsernameIntroFragment extends SlideFragment {
    private boolean mCanGoForward = false;

    private View view;
    private MainIntroActivity mActivity;

    private User mUser;

    private EditText mUsernameField;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.intro_fragment_username, container, false);

        mActivity = (MainIntroActivity) getIntroActivity();
        mActivity.setUsernameSlide(this);

        if (mActivity.getLoginSlide().mMustSkip) nextSlide();

        initViews();

        return view;
    }

    private void initViews() {
        mUsernameField = view.findViewById(R.id.ifu_username);
    }

    private void setupViews() {
        mUsernameField.setText(mUser.getUsername().replace("&1nv", ""));
    }

    public void prepareNextButton() {
        // views must be setup only after previous slide has been completed
        mUser = LUserDAO.get().getThisUser();
        setupViews();

        mActivity.setButtonNextOnClickListener(view1 -> validateUsername());
    }

    private void validateUsername() {
        final String newUsername = mUsernameField.getText().toString();

        if (!newUsername.startsWith("&1nv")) {
            final ArrayList<User> userList = new ArrayList<>();

            // check if username already exists
            UserDAO.get().filterByUsername(
                    newUsername,

                    new Listeners.ListListener<User>() {
                        @Override
                        public void onItemAdded(@NonNull User user) {
                            userList.add(user);
                        }

                        @Override
                        public void onItemRemoved(@NonNull User user) {
                            userList.remove(user);
                        }
                    },

                    new Listeners.AnswerListener() {
                        @Override
                        public void onAnswerRetrieved() {
                            Integer acceptableSize;

                            // in case the user has already registered, its username won't
                            // contain "&1nv", so it's going to be considered as valid and
                            // will be added to the list, so its size is going to be 1

                            if (mActivity.getLoginSlide().mIsFirstLogin) {
                                acceptableSize = 0;
                            } else {
                                acceptableSize = 1;
                            }

                            if (userList.size() == acceptableSize) {
                                // username doesn't exist
                                UserDAO.get().update(
                                        new User(
                                                mUser.getKey(),
                                                mUser.getSocialKey(),
                                                newUsername,
                                                mUser.getPicURL()
                                        ),

                                        new Listeners.AnswerListener() {
                                            @Override
                                            public void onAnswerRetrieved() {
                                                nextSlide();
                                            }

                                            @Override
                                            public void onError() {
                                                mActivity.createSnackbar(R.string.something_went_wrong).show();
                                            }
                                        }
                                );

                            } else {

                                mActivity.createSnackbar(R.string.already_exists_username).show();
                            }
                        }

                        @Override
                        public void onError() {
                            mActivity.createSnackbar(R.string.something_went_wrong).show();
                        }
                    });

        } else {

            mActivity.createSnackbar(R.string.invalid_username).show();
        }
    }

    @Override
    public boolean nextSlide() {
        mCanGoForward = true;
        canGoForward();
        mActivity.getPhoneSlide().prepareNextButton();
        return super.nextSlide();
    }

    @Override
    public boolean canGoForward() {
        return mCanGoForward;
    }

    @Override
    public boolean canGoBackward() {
        return false;
    }
}
