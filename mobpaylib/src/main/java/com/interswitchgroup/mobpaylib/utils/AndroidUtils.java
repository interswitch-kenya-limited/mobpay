package com.interswitchgroup.mobpaylib.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class AndroidUtils {
  /**
   * @param view View to animate
   * @param toVisibility Visibility at the end of animation
   * @param toAlpha Alpha at the end of animation
   * @param duration Animation duration in ms
   */
  public static void animateView(
      final View view, final int toVisibility, float toAlpha, int duration) {
    boolean show = toVisibility == View.VISIBLE;
    if (show) {
      view.setAlpha(0);
    }
    view.setVisibility(View.VISIBLE);
    view.bringToFront();
    view.animate()
        .setDuration(duration)
        .alpha(show ? toAlpha : 0)
        .setListener(
            new AnimatorListenerAdapter() {
              @Override
              public void onAnimationEnd(Animator animation) {
                view.setVisibility(toVisibility);
              }
            });
  }
}
