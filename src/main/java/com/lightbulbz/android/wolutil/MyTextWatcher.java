package com.lightbulbz.android.wolutil;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by kevin on 6/3/15.
 */
class MyTextWatcher implements TextWatcher {
    private final int COLOR_VALID;
    private final int COLOR_INVALID;
    private final EditText editText;
    private final String validPattern;

    public MyTextWatcher(Context context, EditText editText, String validPattern) {
        this.editText = editText;
        COLOR_VALID = context.getResources().getColor(android.R.color.primary_text_dark);
        COLOR_INVALID = Color.RED;
        this.validPattern = validPattern;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (Pattern.matches(validPattern, s)) {
            editText.setTextColor(COLOR_VALID);
        } else {
            editText.setTextColor(COLOR_INVALID);
        }
    }
}
