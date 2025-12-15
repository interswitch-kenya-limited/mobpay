package com.interswitchgroup.mobpaylib.ui.snippets;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.model.Card;
import org.apache.commons.lang3.ArrayUtils;

public class ExpiryText extends AppCompatEditText {

  @Nullable private static String SEPARATOR = "/";
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

  private static String sanitize(String text) {
    if (!text.contains(SEPARATOR)) {
      text = text.replaceAll("(.{2})", "$1" + SEPARATOR);
    }
    String[] expiryParts = text.split(SEPARATOR);
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
    return (expiryMonth.length() > 1 ? expiryMonth + SEPARATOR : expiryMonth) + expiryYear;
  }

  @Override
  protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {

    String textString = text.toString();
    if (textString.length() > 0) {
      String newText = sanitize(textString);
      if (!textString.equalsIgnoreCase(newText)) {
        // Text was modified by sanitizer, we need to set the new text and move the cursor to the
        // expected position
        int previousCaretPosition = getSelectionEnd();
        setText(newText);
        int newLength = newText.length();
        int oldLength = textString.length();
        int caretMoves = newLength - oldLength;
        int sign = lengthAfter - lengthBefore;
        int newCaretPosition = previousCaretPosition;
        if (sign > 0) { // Checks if deleting or inserting new chars
          newCaretPosition = previousCaretPosition + caretMoves;
        }
        setSelection(newCaretPosition);
        return;
      }
      if (textString.length() < 5) {
        setBackgroundResource(R.drawable.textbox_probably_valid);
      } else if (textString.length() == 5) {
        String[] expiryParts = textString.replaceAll("[^\\d]", "").split("(?<=\\G.{2})");
        String apiExpiry = expiryParts[1] + expiryParts[0];
        if (Card.isExpiryValid(apiExpiry)) {
          setBackgroundResource(R.drawable.textbox_valid);
        } else {
          setBackgroundResource(R.drawable.textbox_invalid);
        }
      }
    } else {
      setBackgroundResource(R.drawable.textbox_neutral);
    }
  }

  public static String getTextWithoutSeparator(String text) {
    return text.replaceAll(SEPARATOR, "");
  }
}
