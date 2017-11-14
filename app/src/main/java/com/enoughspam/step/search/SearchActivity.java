package com.enoughspam.step.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;

public class SearchActivity extends AbstractActivity {
    EditText mEditText;
    ImageView mClearIcon;
    LinearLayout mProgressBar;
    LinearLayout mNoResults;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initToolbar(true);
        initViews();
        initActions();
        initFragment();
    }

    @Override
    protected void initViews() {
        mEditText = (EditText) findViewById(R.id.search_edit_text);
        mEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        mClearIcon = (ImageView) findViewById(R.id.search_clear_icon);
        mProgressBar = (LinearLayout) findViewById(R.id.search_progress_bar);
        mProgressBar = (LinearLayout) findViewById(R.id.search_progress_bar);
        mNoResults = (LinearLayout) findViewById(R.id.search_no_results);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
    }

    @Override
    protected void initActions() {
        mClearIcon.setOnClickListener(view -> mEditText.getText().clear());

        final SearchAdapter adapter = new SearchAdapter(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(layoutManager);

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString();

                if (query.length() > 0 && query.charAt(0) == '@') {
                    query = query.replaceFirst("@", "");
                }

                adapter.setQuery(query);
            }
        });
    }

    @Override
    protected void initFragment() {}

    public void showProgressBar() {
        if (mRecyclerView.getVisibility() != View.GONE) {
            mRecyclerView.setVisibility(View.GONE);
        }

        if (mNoResults.getVisibility() != View.GONE) {
            mNoResults.setVisibility(View.GONE);
        }

        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void showNoResults() {
        if (mRecyclerView.getVisibility() != View.GONE) {
            mRecyclerView.setVisibility(View.GONE);
        }

        if (mProgressBar.getVisibility() != View.GONE) {
            mProgressBar.setVisibility(View.GONE);
        }

        if (mNoResults.getVisibility() != View.VISIBLE) {
            mNoResults.setVisibility(View.VISIBLE);
        }
    }

    public void showRecyclerView() {
        if (mNoResults.getVisibility() != View.GONE) {
            mNoResults.setVisibility(View.GONE);
        }

        if (mProgressBar.getVisibility() != View.GONE) {
            mProgressBar.setVisibility(View.GONE);
        }

        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void showSnackbar(@StringRes final int message) {
        Snackbar.make(findViewById(android.R.id.content), getResources().getString(message),
                Snackbar.LENGTH_LONG).show();
    }
}
