package com.qipei.interact.selectVideo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import cn.ml.base.utils.MLDialogUtils;
import com.txsh.R;
import com.zuomei.base.BaseActivity;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends BaseActivity {

  private ScannerAnsyTask ansyTask = new ScannerAnsyTask();

  Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 100:
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              MLDialogUtils.dismissProgressDialog();
              List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
              videoInfos.addAll(ansyTask.videoInfos);
              GridView gridView = (GridView) findViewById(R.id.select_video);
              SelectVideoGvAdapter selectVideoGvAdapter = new SelectVideoGvAdapter(getAty(),
                  R.layout.item_select_video);
              gridView.setAdapter(selectVideoGvAdapter);
              selectVideoGvAdapter.setData(videoInfos);
            }
          });
          break;

        default:
          break;
      }
    }

  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select);
    showProgressDialog(getAty());
    ansyTask.execute();
  }

  public class ScannerAnsyTask extends AsyncTask<Void, Integer, List<VideoInfo>> {


    public List<VideoInfo> videoInfos = new ArrayList<VideoInfo>();
    private final static String TAG = "视频文件";

    @Override
    protected List<VideoInfo> doInBackground(Void... params) {
      videoInfos = getVideoFile(videoInfos, Environment.getExternalStorageDirectory());
      videoInfos = filterVideo(videoInfos);
      handler.sendEmptyMessage(100);
      return videoInfos;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<VideoInfo> videoInfos) {
      super.onPostExecute(videoInfos);
    }

    /**
     * 获取视频文件
     */
    private List<VideoInfo> getVideoFile(final List<VideoInfo> list, File file) {

      file.listFiles(new FileFilter() {

        @Override
        public boolean accept(File file) {

          String name = file.getName();

          int i = name.indexOf('.');
          if (i != -1) {
            name = name.substring(i);
            if (name.equalsIgnoreCase(".mp4")
//                                || name.equalsIgnoreCase(".3gp")
                || name.equalsIgnoreCase(".wmv")
//                                || name.equalsIgnoreCase(".ts")
//                                || name.equalsIgnoreCase(".rmvb")
//                                || name.equalsIgnoreCase(".mov")
//                                || name.equalsIgnoreCase(".m4v")
                || name.equalsIgnoreCase(".avi")
//                                || name.equalsIgnoreCase(".m3u8")
//                                || name.equalsIgnoreCase(".3gpp")
//                                || name.equalsIgnoreCase(".3gpp2")
//                                || name.equalsIgnoreCase(".mkv")
                || name.equalsIgnoreCase(".flv")
//                                || name.equalsIgnoreCase(".divx")
//                                || name.equalsIgnoreCase(".f4v")
//                                || name.equalsIgnoreCase(".rm")
//                                || name.equalsIgnoreCase(".asf")
//                                || name.equalsIgnoreCase(".ram")
//                                || name.equalsIgnoreCase(".mpg")
//                                || name.equalsIgnoreCase(".v8")
//                                || name.equalsIgnoreCase(".swf")
//                                || name.equalsIgnoreCase(".m2v")
//                                || name.equalsIgnoreCase(".asx")
//                                || name.equalsIgnoreCase(".ra")
//                                || name.equalsIgnoreCase(".ndivx")
//                                || name.equalsIgnoreCase(".xvid")
                ) {
              VideoInfo video = new VideoInfo();
              file.getUsableSpace();
              video.setDisplayName(file.getName());
              video.setPath(file.getAbsolutePath());
              Log.i("tga", "name" + video.getPath());
              list.add(video);
              return true;
            }
            //判断是不是目录
          } else if (file.isDirectory()) {
            getVideoFile(list, file);
          }
          return false;
        }
      });

      return list;
    }

    /**
     * 10M=10485760 b,小于10m的过滤掉 过滤视频文件
     */
    private List<VideoInfo> filterVideo(List<VideoInfo> videoInfos) {
      List<VideoInfo> newVideos = new ArrayList<VideoInfo>();
      for (VideoInfo videoInfo : videoInfos) {
        File f = new File(videoInfo.getPath());
        if (f.exists() && f.isFile() && f.length() > 500) {
          videoInfo.setImgPath(MLPhotoGet.getPath(getAty(), videoInfo.getPath()));
          videoInfo.setLength(f.length());
          newVideos.add(videoInfo);
          Log.i(TAG, "文件大小" + f.length());
        }
      }
      return newVideos;
    }
  }

  public void back(View view) {
    finish();
  }
}
