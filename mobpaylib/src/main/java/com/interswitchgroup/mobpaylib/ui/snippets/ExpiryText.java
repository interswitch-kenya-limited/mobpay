package com.interswitchgroup.mobpaylib.ui.snippets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.model.Card;

import org.apache.commons.lang3.ArrayUtils;

public class ExpiryText extends AppCompatEditText {

    @Nullable
    private static String SEPARATOR = "/";
    private String month;
    private String year;

    public ExpiryText(Context context) {
        super(context);
    }

    public ExpiryText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpiryText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

        String textString = text.toString();
        if (textString.length() > 0) {
            String newText = sanitize(textString);
            if (!textString.equalsIgnoreCase(newText)) {
                int caretPosition = getSelectionEnd();
                setText(newText);
                if (caretPosition < newText.length())
                    setSelection(caretPosition);
                return;
            }
            if (textString.length() < 5) {
                setBackgroundResource(R.drawable.probably_valid_textbox);
            } else if (textString.length() == 5) {
                if (Card.isExpiryValid(textString)) {
                    setBackgroundResource(R.drawable.valid_textbox);
                } else {
                    setBackgroundResource(R.drawable.error_textbox);
                }
            }
        } else {
            setBackgroundResource(R.drawable.edit_text_basic);
        }
    }


    private static String sanitize(String text) {
        String[] expiryParts = text.replaceAll("[^\\d]", "").split("(?<=\\G.{2})");
        for (String expiryPart : expiryParts) {
            if (expiryPart.isEmpty()) {
                expiryParts = ArrayUtils.removeElement(expiryParts, expiryPart);
            }
        }
        String expiryMonth = "";
        String expiryYear = "";
        if (expiryParts.length > 0) {
            expiryMonth = expiryParts[0];
            int expMonthInt = Integer.parseInt(expiryMonth);
            if (expMonthInt == 0) {
                expiryMonth = String.valueOf(expMonthInt);
            } else if ((expiryMonth.length() < 2) && (expMonthInt > 1)) {
                expiryMonth = "0" + expMonthInt;
            } else if (expMonthInt > 12) {
                expiryMonth = "0" + expMonthInt;
            }

            String[] newParts;
            String originalExpiryYear = "";
            if (expiryParts.length > 1) {
                originalExpiryYear = expiryParts[1];
            }
            newParts = (expiryMonth + originalExpiryYear).split("(?<=\\G.{2})");
            expiryMonth = newParts[0];
            if (newParts.length > 1) {
                expiryYear = newParts[1];
            }
        }
        return expiryMonth + SEPARATOR + expiryYear;
    }

    public static String getTextWithoutSeparator(String text) {
        return text.replaceAll(SEPARATOR, "");
    }
}