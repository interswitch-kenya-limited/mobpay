package com.interswitchgroup.mobpaylib.ui.snippets;


import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToolTip extends AppCompatTextView {

    private static final int ESTIMATED_TOAST_HEIGHT_DIPS = 48;

    public ToolTip(Context context) {
        super(context);
        init();
    }

    public ToolTip(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToolTip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                  You should set the android:contentDescription attribute in this view's XML layout file.
                 */
                String contentDescription = getContentDescription().toString();
                if (!TextUtils.isEmpty(contentDescription)) {

                    final int[] screenPos = new int[2]; // origin is device display
                    final Rect displayFrame = new Rect(); // includes decorations (e.g. status bar)
                    view.getLocationOnScreen(screenPos);
                    view.getWindowVisibleDisplayFrame(displayFrame);

                    final Context context = view.getContext();
                    final int viewWidth = view.getWidth();
                    final int viewHeight = view.getHeight();
                    final int viewCenterX = screenPos[0] + viewWidth / 2;
                    final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                    final int estimatedToastHeight = (int) (ESTIMATED_TOAST_HEIGHT_DIPS
                            * context.getResources().getDisplayMetrics().density);

                    Toast toolTipToast = Toast.makeText(context, contentDescription, Toast.LENGTH_LONG);
                    boolean showBelow = screenPos[1] < estimatedToastHeight;
                    if (showBelow) {
                        // Show below
                        // Offsets are after decorations (e.g. status bar) are factored in
                        toolTipToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                                viewCenterX - screenWidth / 2,
                                screenPos[1] - displayFrame.top + viewHeight);
                    } else {
                        // Show above
                        // Offsets are after decorations (e.g. status bar) are factored in
                        // NOTE: We can't use Gravity.BOTTOM because when the keyboard is up
                        // its height isn't factored in.
                        toolTipToast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL,
                                viewCenterX - screenWidth / 2,
                                screenPos[1] - displayFrame.top - estimatedToastHeight);
                    }
                    toolTipToast.show();
                }
            }
        });
    }

}
