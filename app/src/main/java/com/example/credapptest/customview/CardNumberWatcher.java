package com.example.credapptest.customview;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;

import com.example.credapptest.interfaces.OnTextChangeListener;
import com.example.credapptest.models.FormatedNumberRes;
import com.example.credapptest.utils.CardUtils;


public class CardNumberWatcher implements TextWatcher {
    private boolean lock = false;
    private int updatedCursor = 0;
    private OnTextChangeListener onTextChangeListener;
    private CardUtils cardUtils;

    public CardNumberWatcher(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
        this.cardUtils = new CardUtils();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!lock) {
            updatedCursor = (start + count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!lock) {
            lock = true;
            setText(s);
            lock = false;
        }
    }

    private void setText(Editable s) {
        FormatedNumberRes formattedText = cardUtils.formatCardNumber(s.toString().replaceAll(" ", ""), s.toString(), onTextChangeListener);
        s.replace(0, s.length(), formattedText.getNumber());
        int i = updatedCursor;
        int formattedTextLength = formattedText.getNumber().length();

        if (updatedCursor >= formattedTextLength) {
            updatedCursor = formattedTextLength;
        }
        if ((updatedCursor > 0) && (formattedText.getNumber().charAt(-1 + updatedCursor) == ' ')) {
            this.updatedCursor += 1;
        }
        if ((updatedCursor > 1) && (formattedText.getNumber().charAt(-1 + updatedCursor) == ' ')) {
            updatedCursor -= 1;
        }
        if (updatedCursor != i) {
            Selection.setSelection(s, updatedCursor);
        }
        if (onTextChangeListener != null) {
            onTextChangeListener.onTextChange(s.toString(), formattedText.getSuffix());
        }
    }
}
