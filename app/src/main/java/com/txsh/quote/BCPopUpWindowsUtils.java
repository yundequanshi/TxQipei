package com.txsh.quote;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.txsh.R;

/**
 * Created by 汉玉 on 2017/3/13.
 */
public class BCPopUpWindowsUtils {

  private static PopupWindow mPop;
  private static BCPopUpWindowsUtils mlPopUpWindowsUtils = null;
  private Activity activity;
  private float bgAlpha;

  public static BCPopUpWindowsUtils getIstnace() {
    if (mlPopUpWindowsUtils == null) {
      mlPopUpWindowsUtils = new BCPopUpWindowsUtils();
    }
    return mlPopUpWindowsUtils;
  }

  public BCPopUpWindowsUtils getPopUpWindows(View contentView, int width,
      int height, final Activity activity, final float bgAlpha, boolean outCancel) {
    int mWidth = LayoutParams.MATCH_PARENT;
    int mHeight = LayoutParams.MATCH_PARENT;
    this.activity = activity;
    this.bgAlpha = bgAlpha;

    if (width != 0) {
      mWidth = dp2px((float) width);
    }
    if (height != 0) {
      mHeight = dp2px((float) height);
    }

    mPop = new PopupWindow(contentView,
        mWidth, mHeight, true);
    mPop.setAnimationStyle(R.style.bj_popp_anim);
    if (outCancel) {
      mPop.setBackgroundDrawable(new ColorDrawable(0));
      mPop.setOutsideTouchable(true);
    }
    mPop.setOnDismissListener(new OnDismissListener() {
      @Override
      public void onDismiss() {
        setBackgroundAlpha(activity, 1);
      }
    });
    return mlPopUpWindowsUtils;
  }

  /**
   * PopUpWindows显示在控件的左边
   */
  public PopupWindow showLeftOfView(View leftOfView) {
    int[] location = new int[2];
    leftOfView.getLocationOnScreen(location);
    mPop.showAtLocation(leftOfView, Gravity.NO_GRAVITY,
        location[0] - mPop.getWidth(), location[1]);
    return mPop;
  }

  /**
   * PopUpWindows显示在控件的右边
   */
  public PopupWindow showRightOfView(View rightOfView) {
    int[] location = new int[2];
    rightOfView.getLocationOnScreen(location);
    mPop.showAtLocation(rightOfView, Gravity.NO_GRAVITY, location[0] + mPop.getWidth(),
        location[1]);
    return mPop;
  }

  /**
   * PopUpWindows显示在控件的上边
   */
  public PopupWindow showTopOfView(View topOfView) {
    int[] location = new int[2];
    topOfView.getLocationOnScreen(location);
    mPop.showAtLocation(topOfView, Gravity.NO_GRAVITY, location[0], location[1] + mPop.getHeight());
    return mPop;
  }

  /**
   * PopUpWindows显示在控件的下边
   */
  public PopupWindow showBottomOfView(View bottomOfView) {
    mPop.showAsDropDown(bottomOfView);
    return mPop;
  }

  /**
   * PopUpWindows显示全局的中心
   */
  public PopupWindow showCenterOfView(View centerOfView) {
    mPop.showAtLocation(centerOfView, Gravity.CENTER, 0, 0);
    setBackgroundAlpha(activity, bgAlpha);
    return mPop;
  }

  /**
   * 设置页面的透明度
   *
   * @param bgAlpha 1表示不透明
   */
  public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
    lp.alpha = bgAlpha;
    if (bgAlpha == 1) {
      activity.getWindow().clearFlags(
          WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
    } else {
      activity.getWindow()
          .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
    }
    activity.getWindow().setAttributes(lp);
  }


  private static int dp2px(float dpVal) {
    float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpVal * scale + 0.5F);
  }
}
