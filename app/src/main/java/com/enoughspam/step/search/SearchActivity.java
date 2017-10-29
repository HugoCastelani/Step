package com.enoughspam.step.search;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.enoughspam.step.R;
import com.enoughspam.step.abstracts.AbstractActivity;

public class SearchActivity extends AbstractActivity {
    EditText mEditText;

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
    }

    @Override
    protected void initActions() {

    }

    @Override
    protected void initFragment() {

    }
}
