package com.example.credapptest.interfaces;

public interface OnTextChangeListener {

    void onTextChange(String cardNumber, String suffix);

    void onValidationError();

    void hideValidation();

    void setResource(int resourceId);
}
