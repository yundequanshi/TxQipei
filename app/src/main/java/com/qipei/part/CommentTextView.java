package com.qipei.part;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by 汉玉 on 2017/6/21.
 */
public class CommentTextView extends TextView {

  public boolean linkHit;//内部链接是否被点击

  public CommentTextView(Context context) {
    super(context);
  }
  public CommentTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }
  public CommentTextView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public boolean performClick() {
    if(linkHit){
      return true;
    }
    return super.performClick();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    linkHit = false;
    return super.onTouchEvent(event);
  }
}
