package com.enoughspam.step.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;
import com.enoughspam.step.util.decorator.ListDecorator;

public class SearchActivity extends AbstractActivity {
    EditText mEditText;
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

        mProgressBar = (LinearLayout) findViewById(R.id.search_progress_bar);
        mNoResults = (LinearLayout) findViewById(R.id.search_no_results);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
    }

    @Override
    protected void initActions() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                final String query = editable.toString();

                if (!query.isEmpty()) {
                    mRecyclerView.setAdapter(new SearchAdapter(query));
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoResults.setVisibility(View.GONE);
                }
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(),
                LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        ListDecorator.init(getBaseContext());
        ListDecorator.addAdaptableMargins(mRecyclerView);
    }

    @Override
    protected void initFragment() {

    }
}
