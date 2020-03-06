package com.qipei.interact;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import cn.ml.base.utils.MLDialogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseActivity;

/**
 * 播放视频 Created by Marcello on 2015/8/25.
 */
public class MLInteractVideoAty extends BaseActivity {

  @ViewInject(R.id.interact_video_play)
  private VideoView mVideo;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.aty_interact_video);
    ViewUtils.inject(this);
    String url = (String) getIntentData();
    showProgressDialog("视频缓存中...", getAty());
    Uri uri = Uri.parse(url);
    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.MATCH_PARENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    mVideo.setMediaController(new MediaController(this));
    mVideo.setLayoutParams(layoutParams);
    mVideo.setVideoURI(uri);
    mVideo.start();
    mVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
      @Override
      public void onPrepared(MediaPlayer mp) {
        MLDialogUtils.dismissProgressDialog();
      }
    });
  }
}
