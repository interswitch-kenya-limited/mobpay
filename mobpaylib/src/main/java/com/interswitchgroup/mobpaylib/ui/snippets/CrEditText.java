package com.interswitchgroup.mobpaylib.ui.snippets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import com.interswitchgroup.mobpaylib.R;
import com.interswitchgroup.mobpaylib.model.Card;

public class CrEditText extends AppCompatEditText {

    @Nullable
    private String mSeparator;
    private Gravity mDrawableGravity = Gravity.END;
    private int mCurrentDrawableResId = 0;

    public CrEditText(Context context) {
        super(context);
        init();
    }
    public CrEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        applyAttributes(attrs);
    }

    public CrEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        applyAttributes(attrs);
    }

    private void init() {
        setInputType(InputType.TYPE_CLASS_PHONE);
        setSeparator(Separator.NONE);
        setDrawableGravity(Gravity.END);
    }

    private void applyAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CrEditText,
                0, 0);
        try {
            setSeparator(Separator.values()[a.getInt(R.styleable.CrEditText_separator, Separator.NONE.ordinal())]);
//            setDisabledCardsInternal(a.getInt(R.styleable.EditCredit_disabledCards, 0));
            setDrawableGravity(Gravity.values()[a.getInt(R.styleable.CrEditText_drawableGravity, Gravity.END.ordinal())]);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        String textWithoutSeparator = getTextWithoutSeparator();
        Card.Type type = Card.getType(textWithoutSeparator);
        Integer mDrawableResId = Card.TYPE_DRAWABLE_MAP.get(type);
        if (mDrawableResId != null) {
            mCurrentDrawableResId = mDrawableResId;
        } else {
            mCurrentDrawableResId = R.drawable.creditcard;
        }
        addDrawable();
        addSeparators();
    }

    private void addDrawable() {
        Drawable currentDrawable = ContextCompat.getDrawable(getContext(), mCurrentDrawableResId);
        if (currentDrawable != null && TextUtils.isEmpty(getError())) {
            currentDrawable = resize(currentDrawable);
            if (mDrawableGravity == null) {
                mDrawableGravity = Gravity.END;
            }
            switch (mDrawableGravity) {
                case START:
                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, currentDrawable, null, null, null);
                    break;
                case RIGHT:
                    setCompoundDrawablesWithIntrinsicBounds(null, null, currentDrawable, null);
                    break;
                case LEFT:
                    setCompoundDrawablesWithIntrinsicBounds(currentDrawable, null, null, null);
                    break;
                default:
                    TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(this, null, null, currentDrawable, null);
                    break;
            }
        }
    }

    private void addSeparators() {
        String text = getText().toString();
        if (mSeparator != null) {
            if (text.length() > 4 && !text.matches("(?:[0-9]{4}" + mSeparator + ")+[0-9]{1,4}")) {
                StringBuilder sp = new StringBuilder();
                int caretPosition = getSelectionEnd();
                String[] segments = splitString(text.replaceAll(mSeparator, ""));
                for (String segment : segments) {
                    sp.append(segment).append(mSeparator);
                }
                setText("");
                append(sp.delete(sp.length() - mSeparator.length(), sp.length()).toString());
                if (caretPosition < text.length())
                    setSelection(caretPosition);
            }
        }
    }

    private void removeSeparators() {
        String text = getText().toString();
        text = text.replaceAll(" ", "").replaceAll("-", "");
        setText("");
        append(text);
    }

    private String[] splitString(String s) {
        int arrayLength = (int) Math.ceil(((s.length() / (double) 4)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + 4);
            j += 4;
        }
        result[lastIndex] = s.substring(j);

        return result;
    }

    public String getTextWithoutSeparator() {
        if (mSeparator == null) return getText().toString();
        return getText().toString().replaceAll(mSeparator, "");
    }

    /**
     * Use this method to set the separator style.
     * The default separator is {@link Separator#NONE}.
     *
     * @param separator the style of the separator.
     */
    public void setSeparator(@NonNull Separator separator) {
        switch (separator) {
            case NONE:
                mSeparator = null;
                break;
            case SPACES:
                mSeparator = " ";
                break;
            case DASHES:
                mSeparator = "-";
                break;
        }
        if (mSeparator != null) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(23)});
            setKeyListener(DigitsKeyListener.getInstance("0123456789" + mSeparator));
            addSeparators();
        } else {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(19)});
            setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            removeSeparators();
        }
    }

    /**
     * Use this method to set the location of the card drawable.
     * The default gravity is {@link Gravity#END}.
     *
     * @param gravity the drawable location.
     */
    public void setDrawableGravity(@NonNull Gravity gravity) {
        mDrawableGravity = gravity;
        addDrawable();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean noDrawablesVisible = true;
        for (Drawable drawable : getCompoundDrawables()) {
            if (drawable != null) {
                noDrawablesVisible = false;
                break;
            }
        }
        if (noDrawablesVisible) {
            addDrawable();
        }
    }

    private Drawable resize(Drawable image) {
        int imageIntrinsicHeight = image.getIntrinsicHeight();
//        int measuredHeight = getMeasuredHeight();
        int measuredHeight = 30;
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
//        int height = measuredHeight - (paddingTop + paddingBottom);
        int height = measuredHeight;
        if (height <= 0) {
            return null;
        } else if (imageIntrinsicHeight > height) {
            Bitmap b = ((BitmapDrawable) image).getBitmap();
            float ratio = (float) image.getIntrinsicWidth() / (float) imageIntrinsicHeight;
            Bitmap bitmapResized = Bitmap.createScaledBitmap(b, (int) (height * ratio), height, false);
            return new BitmapDrawable(getResources(), bitmapResized);
        } else {
            return image;
        }
    }

    public enum Separator {
        NONE, SPACES, DASHES
    }

    public enum Gravity {
        START, END, LEFT, RIGHT
    }
}