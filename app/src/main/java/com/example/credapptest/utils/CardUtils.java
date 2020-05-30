package com.example.credapptest.utils;

import android.text.TextUtils;

import com.example.credapptest.R;
import com.example.credapptest.interfaces.OnTextChangeListener;
import com.example.credapptest.models.CardType;
import com.example.credapptest.models.FormatedNumberRes;

import java.util.regex.Pattern;

public class CardUtils {

    public static final CardType AMEX = new CardType(new int[]{4, 6, 5}, 15, "^3[47][0-9]{0,13}$", R.mipmap.amex);
    public static final CardType MASTER = new CardType(new int[]{4, 4, 4, 4}, 16, "^(5[1-5][0-9]{0,14}|2(22[1-9][0-9]{0,12}|2[3-9][0-9]{0,13}|[3-6][0-9]{0,14}|7[0-1][0-9]{0,13}|720[0-9]{0,12}))$", R.mipmap.master);
    public static final CardType MAESTRO = new CardType(new int[]{4, 4, 4, 4}, 16, "^(5018|5020|5038|6304|6759|6761|6763)[0-9]{0,15}$", R.mipmap.maestro);
    public static final CardType VISA = new CardType(new int[]{4, 4, 4, 4}, 16, "^4[0-9]{0,12}(?:[0-9]{0,3})?$", R.mipmap.visa);
    public static final CardType JCB = new CardType(new int[]{4, 4, 4, 4}, 16, "^(?:2131|1800|35\\d{0,3})\\d{0,11}$", R.mipmap.jcb);
    public static final CardType DINERS1 = new CardType(new int[]{4, 6, 4}, 14, "^389[0-9]{0,11}$", R.mipmap.diners);
    public static final CardType DINERS2 = new CardType(new int[]{4, 6, 4}, 14, "^3(?:0[0-5]|[68][0-9])[0-9]{0,11}$", R.mipmap.diners);
    public static final CardType DISCOVER = new CardType(new int[]{4, 4, 4, 4}, 16, "^65[4-9][0-9]{0,13}|64[4-9][0-9]{0,13}|6011[0-9]{0,12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{0,10})$\n", R.mipmap.discover);
    public static final CardType COMMON = new CardType(new int[]{4, 4, 4, 4}, 16, "", R.mipmap.placeholder);

    private CardType cardType;

    /**
     * Luhn Formula for validating credit card number
     * Step 1: Saving the last digit and dropping it from the main string
     * Step 2: Reverse the digits
     * Step 3: Multiple odd digits by 2
     * Step 4: Subtract 9 to numbers over 9
     * Step 5: Add all numbers
     * Step 6: Mod 10
     *
     * @param cardNumber
     * @return
     */
    public boolean checkIfCardNumberIsValid(String cardNumber) {
        if (TextUtils.isEmpty(cardNumber)) {
            return false;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(cardNumber);

        int sumOfAllModifiedNumber = 0;

        int count = 1;
        for (int i = builder.length(); i > 0; i--) {
            //Step 3
            if (count % 2 == 0) {
                int value = (Character.getNumericValue(builder.charAt(i - 1)) * 2);
                builder.setCharAt(i - 1, Character.forDigit(value > 9 ? value - 9 : value, 10));
            }
            sumOfAllModifiedNumber += Character.getNumericValue(builder.charAt(i - 1));
            count++;
        }

        return sumOfAllModifiedNumber > 0 && sumOfAllModifiedNumber % 10 == 0 ? true : false;
    }

    /**
     * Return the CardType class based on the input card number
     *
     * @param cardNumber
     * @return
     */
    public void getInputFormat(String cardNumber) {
        if (Pattern.compile(AMEX.regex).matcher(cardNumber).matches()) {
            cardType = AMEX;
        } else if (Pattern.matches(MAESTRO.regex, cardNumber)) {
            cardType = MAESTRO;
        } else if (Pattern.matches(MASTER.regex, cardNumber)) {
            cardType = MASTER;
        } else if (Pattern.matches(VISA.regex, cardNumber)) {
            cardType = VISA;
        } else if (Pattern.matches(JCB.regex, cardNumber)) {
            cardType = JCB;
        } else if (Pattern.matches(DISCOVER.regex, cardNumber)) {
            cardType = DISCOVER;
        } else if (Pattern.matches(DINERS1.regex, cardNumber)
                || Pattern.matches(DINERS2.regex, cardNumber)) {
            cardType = DINERS1;
        } else {
            cardType = COMMON;
        }
    }

    /**
     * 1. Format the card based on their card numbers
     * 2. Generate suffix to be appended with card number
     * 3. Fallback number if the number exceeds
     *
     * @param cardNumber
     * @param onTextChangeListener
     * @return
     */
    public FormatedNumberRes formatCardNumber(String cardNumber, String fallbackNumber, OnTextChangeListener onTextChangeListener) {
        FormatedNumberRes formatedNumber = new FormatedNumberRes();
        String suffix = "";
        StringBuilder builder = new StringBuilder();
        int startIndex = 0;
        int endIndex = 0;
        int length = cardNumber.length();

        if (cardType != null && length > cardType.length) {
            fallbackNumber = fallbackNumber.substring(0, fallbackNumber.length() - 1);
            formatedNumber.setNumber(fallbackNumber);
            formatedNumber.setSuffix("");
            return formatedNumber;
        }

        getInputFormat(cardNumber);

        for (int pattern : cardType.format) {
            endIndex += pattern;
            final boolean isAtEnd = (endIndex >= length);
            if (length > startIndex) {
                builder.append(cardNumber.substring(startIndex, isAtEnd ? length : endIndex));
            }
            if (!isAtEnd) {
                builder.append(" ");
            } else {
                int suffixStart = length > startIndex ? endIndex - length : endIndex - startIndex;
                for (int i = 0; i < suffixStart; i++) {
                    suffix += "X";
                }
                if (startIndex != endIndex) {
                    suffix += " ";
                }
            }
            startIndex += pattern;
        }

        //Handling validation
        if (length == cardType.length && !checkIfCardNumberIsValid(cardNumber)) {
            onTextChangeListener.onValidationError();
        } else {
            //hide the error once user starts editing
            onTextChangeListener.hideValidation();
        }
        //Setting resource image of the card type
        onTextChangeListener.setResource(cardType.resource);

        formatedNumber.setNumber(builder.toString());
        formatedNumber.setSuffix(suffix);
        return formatedNumber;
    }
}
