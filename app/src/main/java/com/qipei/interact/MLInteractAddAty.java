package com.qipei.interact;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLFolderUtils;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.widget.BCScrollGridView;
import com.qipei.interact.selectVideo.SelectActivity;
import com.txsh.utils.LXPhoto;
import com.zuomei.home.MLAccidentPhotoAdapter2;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.photo.MLPhotoUtil;

import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.txsh.market.EventBusModel;
import com.txsh.model.FileUpload;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLAccidentPhotoAdapter;
import com.zuomei.home.MLAccidentPhotoAdapter2.GetRefusePhoto;
import com.zuomei.home.MLCropImageActivity;
import com.zuomei.home.MLMessageAddPop;
import com.zuomei.home.MLMessagePhotoPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMessageServices;
import com.zuomei.utils.MLToolUtil;
import org.greenrobot.eventbus.EventBus;


/**
 * Created by Marcello on 2015/6/11.
 */
public class MLInteractAddAty extends BaseActivity implements GetRefusePhoto {

  private static final String TAG = "MLInteractAddAty";
  private static final boolean D = true;
  private int selectIndex = 0;
  private ArrayList<String> mPhotoList = new ArrayList<String>();
  private MLAccidentPhotoAdapter2 mImagePathAdapter;
  private MLMessageAddPop _pop;
  private MLMessagePhotoPop _photoPop;
  /* 用来标识请求gallery的activity */
  private static final int PHOTO_PICKED_WITH_DATA = 3021;
  // 照相机拍照得到的图片
  private File mCurrentPhotoFile;
  /* 用来标识请求照相功能的activity */
  private static final int CAMERA_WITH_DATA = 3023;
  /* 用来标识请求裁剪图片后的activity */
  private static final int CAMERA_CROP_DATA = 3022;
  private int camIndex = 0;
  @ViewInject(R.id.accident_add_root)
  private LinearLayout _root;

  @ViewInject(R.id.accident_gv_photo)
  private BCScrollGridView _photoGv;
  @ViewInject(R.id.last_hint)
  private TextView last_hint;
  @ViewInject(R.id.yuyinshijian)
  private TextView yuyinshijian;
  @ViewInject(R.id.iv_video)
  private ImageView ivVideo;
  @ViewInject(R.id.publish_video)
  private ImageView publishViewo;

  @ViewInject(R.id.interact_et_content)
  private EditText mEtContent;
  private LinearLayout ll_voice;
  // 录音计时
  private TimeCount timer;
  // 录音相关
  private MediaRecorder recorder;
  public String voiceSaveDir;
  /**
   * 录音保存名称
   **/
  public static String voiceName = ".amr";
  public long name = System.currentTimeMillis();
  // 是否已经录音
  private boolean isVoice;

  private List<String> imageurl = new ArrayList<String>();

  private Gson gson;

  private List<Map<String, String>> imageuploadList = new ArrayList<Map<String, String>>();

  private List<FileUpload> voiceuploadList = new ArrayList<FileUpload>();
  private List<FileUpload> videouploadList = new ArrayList<FileUpload>();
  private int i = 0;

  private ImageView yuyinneirong, shanchuyuyin;
  private String mDataVideo;
  private File mDataVideoImage;
  private boolean hasVocie = false;
  private String videoPic = "";
  private List<PhotoInfo> photoInfos = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.qp_interact_add);
    ViewUtils.inject(this);
    ll_voice = (LinearLayout) findViewById(R.id.ll_voice);
    yuyinneirong = (ImageView) findViewById(R.id.yuyinneirong);
    shanchuyuyin = (ImageView) findViewById(R.id.shanchuyuyin);
    yuyinneirong.setVisibility(View.GONE);
    gson = new Gson();
    // 存在SDCARD的时候，路径设置到SDCARD
    if (Environment.MEDIA_MOUNTED.equals(Environment
        .getExternalStorageState())) {
      voiceSaveDir = Environment.getExternalStorageDirectory().getPath()
          + File.separatorChar + getPackageName()
          + File.separatorChar;
      // 不存在SDCARD的时候
    } else {
      voiceSaveDir = Environment.getDataDirectory().getPath()
          + File.separatorChar + "data" + File.separatorChar
          + getPackageName() + File.separatorChar;
    }
    last_hint.setText(Html.fromHtml(String
        .format("点击添加9张照片，请从<font color=\"#279efa\">%s</font>展示车辆。",
            "正面、侧面、细节")));
    new File(voiceSaveDir).mkdirs();
    MLPhotoUtil.clear();
    mPhotoList.clear();
    mPhotoList.add(String.valueOf(R.mipmap.add_img));
    mImagePathAdapter = new MLAccidentPhotoAdapter2(this, mPhotoList, 400,
        300);
    mImagePathAdapter.setHandler(this);
    _photoGv.setAdapter(mImagePathAdapter);
    View mAvatarView = LayoutInflater.from(this).inflate(
        R.layout.message_choose_avatar, null);
    _pop = new MLMessageAddPop(this, mAvatarView);
    Button albumButton = (Button) mAvatarView
        .findViewById(R.id.choose_album);
    Button camButton = (Button) mAvatarView.findViewById(R.id.choose_cam);
    Button cancelButton = (Button) mAvatarView
        .findViewById(R.id.choose_cancel);

    albumButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        // 从相册中去获取
        try {
          Intent intent = new Intent(Intent.ACTION_PICK, null);
          intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
          startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
          showMessage("没有找到照片");
        }
      }
    });
    camButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        doPickPhotoAction();
      }
    });

    cancelButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        _pop.dismiss();
      }
    });

    ll_voice.setOnTouchListener(new OnTouchListener() {

      @Override
      public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if (mPhotoList.size() > 1) {
          showMessage("已经添加图片不能再发布语音");
          return true;
        }

        if (!BCStringUtil.isEmpty(mDataVideo)) {
          showMessage("已经添加视频不能再发布语音");
          return true;
        }
        switch (event.getAction()) {
          case MotionEvent.ACTION_DOWN:// 开始录音
            Toast.makeText(MLInteractAddAty.this, "开始录音", Toast.LENGTH_SHORT).show();
            // 开始录音
            hasVocie = true;
            startVoice();
            if (timer != null) {
              timer.cancel();
              timer = null;
            }
            // 录音计时 1分钟
            timer = new TimeCount(60000, 1000);
            // 录音计时 一分钟
            timer.start();
            break;
          case MotionEvent.ACTION_UP:// 停止录音
            Toast.makeText(MLInteractAddAty.this, "停止录音", Toast.LENGTH_SHORT).show();
            // 停止录音
            stopVoice();
            if (timer != null) {
              timer.cancel();
              timer.close();
              timer = null;
            }
            break;
          default:
            break;
        }
        return true;
      }
    });

    yuyinneirong.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        openFile();
      }
    });

    shanchuyuyin.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        name = System.currentTimeMillis();
        yuyinneirong.setVisibility(View.GONE);
        showMessage("语音删除成功");
      }
    });
  }

  // 录音超时提示dialog
  void dialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("录音备注不能超过一分钟").setCancelable(false)
        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });
    AlertDialog alert = builder.create();
    alert.show();
  }

  @Override
  public void onPause() {
    // TODO Auto-generated method stub
    super.onPause();
    isAdd = false;
  }

  @OnItemClick(R.id.accident_gv_photo)
  public void photoOnItemClick(AdapterView<?> arg0, View arg1, int position,
      long arg3) {

    if (hasVocie) {
      showMessage("已经添加语音不能再发布图片");
      return;
    }

    if (!BCStringUtil.isEmpty(mDataVideo)) {
      showMessage("已经添加视频不能再发布图片");
      return;
    }

    if (position == 9) {
      showMessage("最多添加9张图片!");
      return;
    }
    selectIndex = position;
    String path = mPhotoList.get(position);
    if (path.equalsIgnoreCase(String.valueOf(R.mipmap.add_img))) {
      LXPhoto.getMorePhoto(this, photoInfos, new OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
          if (!resultList.isEmpty()) {
            mPhotoList.remove(mPhotoList.size() - 1);
            if (reqeustCode == LXPhoto.REQUEST_CODE_CAMERA) {
              photoInfos.addAll(resultList);
            } else {
              photoInfos.clear();
              photoInfos.addAll(resultList);
            }
            imageurl.clear();
            mPhotoList.clear();
            for (PhotoInfo photoInfo : photoInfos) {
              mPhotoList.add(photoInfo.getPhotoPath());
              imageurl.add(photoInfo.getPhotoPath());
            }
            showInAdapter(mPhotoList);
          }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
      });
    } else {
      _photoPop = new MLMessagePhotoPop(this, path);
      _photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0);
    }
  }

  private void showInAdapter(ArrayList<String> mPhotoList) {
    mPhotoList.add(String.valueOf(R.mipmap.add_img));
    mImagePathAdapter.setItems(mPhotoList);
  }

  /**
   * 描述：从照相机获取
   */
  private void doPickPhotoAction() {
    String status = Environment.getExternalStorageState();
    // 判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
    if (status.equals(Environment.MEDIA_MOUNTED)) {
      doTakePhoto();
    } else {
      showMessage("没有可用的存储卡");
    }
  }

  /**
   * 拍照获取图片
   */
  protected void doTakePhoto() {
    try {

      String mFileName = System.currentTimeMillis() + ".jpg";

      mCurrentPhotoFile = new
          File(Environment.getExternalStorageDirectory(),
          mFileName);

//			mCurrentPhotoFile = getAttachFolder();
      // mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
      intent.putExtra(MediaStore.EXTRA_OUTPUT,
          Uri.fromFile(mCurrentPhotoFile));
      startActivityForResult(intent, CAMERA_WITH_DATA);
    } catch (Exception e) {
      showMessage("未找到系统相机程序");
    }
  }

  File _attachDirFile = null;

  public File getAttachFolder() {
    if (_attachDirFile == null) {
      File file = BaseApplication.getInstance().getExternalCacheDir();
      String subPath = String.format("%s/image", file.getAbsolutePath());
      /*
       * File file = getActivity().getFilesDir(); String subPath =
			 * String.format("%s/uploadAttach",file.getAbsolutePath()); File
			 * subFile = new File(subPath); return subFile.getAbsoluteFile();
			 */
      File subFile = new File(subPath);
      return subFile.getAbsoluteFile();
    }
    return _attachDirFile;
  }

  @OnClick(R.id.top_btn_left)
  public void backOnClick(View view) {
    finish();
  }

  @OnClick(R.id.publish_video)
  public void videoOnClick(View view) {
    if (mPhotoList.size() > 1) {
      showMessage("已经添加图片不能再发布视频");
      return;
    }

    if (hasVocie) {
      showMessage("已经添加语音不能再发布视频");
      return;
    }
    String[] item = {"拍摄视频", "选择视频"};
    BCDialogUtil.getDialogItem(getAty(), R.color.bj_head_back_nomall, "提示", item,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            if (which == 0) {
              startAct(MLInteractAddAty.this, MLReVideoAty.class, null, 1001);
            } else {
              startAct(getAty(), SelectActivity.class, null, 1001);
            }
          }
        });
  }

  private boolean isAdd = false;

  @OnClick(R.id.accident_btn_next)
  public void addOnClick(View view) {

    String message = mEtContent.getText().toString();
    if (MLToolUtil.isNull(message) && imageurl.size() == 0) {
      showMessage("请填写消息内容或上传图片");
      // isAdd = false;
      return;
    }
    if (imageurl.size() == 0) {
      uploadVoice();
    }
    showProgressDialog(MLInteractAddAty.this);
    for (i = 0; i < imageurl.size(); i++) {
      RequestParams params = new RequestParams();
      HttpUtils http = new HttpUtils();
      params.addBodyParameter("file", new File(imageurl.get(i)));
      http.send(HttpRequest.HttpMethod.POST,
          APIConstants.API_IMAGE_UPLOAD
              + APIConstants.API_UPLOAD_IMAGES, params,
          new RequestCallBack<String>() {

            @Override
            public void onStart() {
            }

            @Override
            public void onLoading(long total, long current,
                boolean isUploading) {
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
              FileUpload imageUpload = gson.fromJson(
                  responseInfo.result, FileUpload.class);
              Map<String, String> imageMap = new HashMap<String, String>();
              imageMap.put("path", imageUpload.getRes().getData()
                  .get(0).getPath());
              imageuploadList.add(imageMap);
              if (imageuploadList.size() == i) {
                uploadVoice();
              }
              return;
            }

            @Override
            public void onFailure(HttpException error, String msg) {
              uploadVoice();
            }
          });
    }
  }

  public void uploadVoice() {
    RequestParams params = new RequestParams();

    params.addBodyParameter("file", new File(voiceSaveDir + name
        + voiceName));

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, APIConstants.API_IMAGE_UPLOAD
            + APIConstants.API_UPLOAD_VOICE, params,
        new RequestCallBack<String>() {

          @Override
          public void onStart() {
          }

          @Override
          public void onLoading(long total, long current,
              boolean isUploading) {
          }

          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            FileUpload voiceUpload = gson.fromJson(
                responseInfo.result, FileUpload.class);
            voiceuploadList.add(voiceUpload);
            if (BCStringUtil.isEmpty(mDataVideo)) {
              publish();
            } else {
              uploadVideo();
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            if (BCStringUtil.isEmpty(mDataVideo)) {
              publish();
            } else {
              uploadVideo();
            }
          }
        });
  }

  public void uploadVideo() {

    RequestParams params = new RequestParams();
    HttpUtils http = new HttpUtils();
    params.addBodyParameter("file", mDataVideoImage);
    http.send(HttpRequest.HttpMethod.POST,
        APIConstants.API_IMAGE_UPLOAD
            + APIConstants.API_UPLOAD_IMAGES, params,
        new RequestCallBack<String>() {

          @Override
          public void onStart() {
          }

          @Override
          public void onLoading(long total, long current,
              boolean isUploading) {
          }

          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            FileUpload imageUpload = gson.fromJson(
                responseInfo.result, FileUpload.class);
            videoPic = imageUpload.getRes().getData()
                .get(0).getPath();
            uploadVideo2();
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            uploadVideo2();
          }
        });
  }

  private void uploadVideo2() {
    RequestParams params = new RequestParams();
    params.addBodyParameter("file", new File(mDataVideo));

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST, APIConstants.API_IMAGE_UPLOAD
            + APIConstants.API_UPLOAD_VOICE, params,
        new RequestCallBack<String>() {

          @Override
          public void onStart() {
          }

          @Override
          public void onLoading(long total, long current,
              boolean isUploading) {
          }

          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            FileUpload voiceUpload = gson.fromJson(
                responseInfo.result, FileUpload.class);
            videouploadList.add(voiceUpload);
            publish();
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            publish();
          }
        });
  }

  /**
   * @description 发表互动
   * @author marcello
   */
  private void publish() {
    MLLogin user = BaseApplication.getInstance().get_user();
    ZMRequestParams params = new ZMRequestParams();
    gson.toJsonTree(imageuploadList);
    if (imageuploadList.size() > 0) {
      params.addParameter("images", gson.toJsonTree(imageuploadList)
          .toString());
    }
    if (user.isDepot) {
      params.addParameter(MLConstants.PARAM_HOME_DEPORT, user.Id);
    } else {
      params.addParameter(MLConstants.PARAM_HOME_BUSINESSID1, user.Id);
    }
    String content = mEtContent.getText().toString();
    content = content.replaceAll("\r|\n", "");
    params.addParameter(MLConstants.PARAM_MESSAGE_CONTENT, content);
    params.addParameter("cityId", BaseApplication._currentCity);
    params.addParameter("length", yuyinshijian.getText().toString()
        .substring(0, yuyinshijian.getText().toString().length() - 1));

    if (voiceuploadList.size() > 0) {
      params.addParameter("voice", voiceuploadList.get(0).getRes()
          .getData().get(0).getPath());
    }

    //上传视频
    if (!videouploadList.isEmpty()) {
      params.addParameter("video", videouploadList.get(0).getRes()
          .getData().get(0).getPath());
      params.addParameter("videoPic", videoPic);
    }

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.MESSAGE_PUBLISH, null, params, _handler,
        HTTP_RESPONSE_PUBLICH, MLMessageServices.getInstance());
    loadDataWithMessage("正在发布...", message2);

  }

  private static final int HTTP_RESPONSE_PUBLICH = 0;
  private Handler _handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      dismissProgressDialog();
      if (msg == null || msg.obj == null) {
        showMessage(R.string.loading_data_failed);
        return;
      }
      if (msg.obj instanceof ZMHttpError) {
        ZMHttpError error = (ZMHttpError) msg.obj;
        showMessage(error.errorMessage);
        return;
      }
      switch (msg.what) {
        // 发表互动消息
        case HTTP_RESPONSE_PUBLICH: {
          isAdd = false;
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessageSuccess("发表成功!");
            setResult(1);
            EventBus.getDefault().post(new EventBusModel("refuse"));
            finish();
          } else {
            showMessage("发表互动消息失败!");
            imageuploadList.clear();
            voiceuploadList.clear();
          }
          break;
        }
        default:
          break;
      }
    }
  };

  /**
   * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况, 他们启动时是这样的startActivityForResult
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
    _pop.dismiss();
    if (resultCode != android.app.Activity.RESULT_OK) {
      if (resultCode != 1001) {
        return;
      }
    }
    if (requestCode == 1001 && mIntent != null) {
      Bundle info = mIntent.getExtras();
      mDataVideo = (String) info.get("path");
      mDataVideoImage = new File(MLFolderUtils.getDiskFile(this, "video"),
          "thvideo" + System.currentTimeMillis() + ".jpg");
      Bitmap bitmap = null;
      FileOutputStream fos = null;
      try {
        if (!mDataVideoImage.getParentFile().exists()) {
          mDataVideoImage.getParentFile().mkdirs();
        }
        bitmap = ThumbnailUtils.createVideoThumbnail(mDataVideo, 3);
        if (bitmap == null) {
          bitmap = BitmapFactory
              .decodeResource(getResources(), R.drawable.image_error);
        }
        fos = new FileOutputStream(mDataVideoImage);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        ivVideo.setVisibility(View.VISIBLE);
        publishViewo.setVisibility(View.GONE);
        ivVideo.setImageBitmap(bitmap);
        ivVideo.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            Intent intent = new Intent(MLInteractAddAty.this, ShowVideoActivity.class);
            intent.putExtra("localpath", mDataVideo);
            intent.putExtra("secret", "");
            intent.putExtra("remotepath", "");
            startActivity(intent);
          }
        });

      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (fos != null) {
          try {
            fos.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
          fos = null;
        }
        if (bitmap != null) {
          //   bitmap.recycle();
          bitmap = null;
        }

      }
    }
    switch (requestCode) {
      case PHOTO_PICKED_WITH_DATA:
        Uri uri = mIntent.getData();
        String currentFilePath = getPath(uri);
        startPhotoZoom(uri);
        break;
      case CAMERA_WITH_DATA:
        if (D) {
          Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
        }
        String currentFilePath2 = mCurrentPhotoFile.getPath();
        Uri uri2 = Uri.fromFile(mCurrentPhotoFile);
        startPhotoZoom(uri2);
        break;
      case CAMERA_CROP_DATA:

        Bundle extras = mIntent.getExtras();
        String currentFilePath3 = mCurrentPhotoFile.getPath();
        imageurl.add(currentFilePath3);
        mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, currentFilePath3);
        camIndex++;
        AbViewUtil.setAbsListViewHeight(_photoGv, 3, 25);
        break;
    }
  }


  /**
   * 裁剪图片方法实现
   */
  public void startPhotoZoom(Uri uri) {
        /*
         * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
         * yourself_sdk_path/docs/reference/android/content/Intent.html
         * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能,
         * 是直接调本地库的，小马不懂C C++  这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么
         * 制做的了...吼吼
         */
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
    intent.putExtra("crop", "true");
    // aspectX aspectY 是宽高的比例
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);
    // outputX outputY 是裁剪图片宽高
    intent.putExtra("outputX", 600);
    intent.putExtra("outputY", 600);
    //  intent.putExtra("return-data", true);

    String mFileName = System.currentTimeMillis() + ".jpg";

    mCurrentPhotoFile = new
        File(Environment.getExternalStorageDirectory(),
        mFileName);

//		mCurrentPhotoFile = getAttachFolder();
    // mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));

    startActivityForResult(intent, CAMERA_CROP_DATA);
  }

  /**
   * 从相册得到的url转换为SD卡中图片路径
   */
  @SuppressWarnings("deprecation")
  public String getPath(Uri uri) {
    if (AbStrUtil.isEmpty(uri.getAuthority())) {
      return null;
    }
    String[] projection = {MediaStore.Images.Media.DATA};
    Cursor cursor = MLInteractAddAty.this.managedQuery(uri, projection,
        null, null, null);
    int column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    String path = cursor.getString(column_index);
    return path;
  }

  // 开始录音
  @SuppressWarnings("deprecation")
  private void startVoice() {
    // new出MediaRecorder对象
    recorder = new MediaRecorder();
    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    // 设置MediaRecorder的音频源为麦克风
    recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
    // 设置MediaRecorder录制的音频格式
    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    // 设置MediaRecorder录制音频的编码为amr.
    recorder.setOutputFile(voiceSaveDir + name + voiceName);
    // 设置录制好的音频文件保存路径
    recorder.setOnErrorListener(new OnErrorListener() {

      @Override
      public void onError(MediaRecorder arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
        releaseRecorder();
      }
    });

    try {
      // 准备录制
      recorder.prepare();
    } catch (IllegalStateException e1) {
      // TODO Auto-generated catch block
      recorder.reset();
      // 刻录完成一定要释放资源
      recorder.release();
      recorder = null;
      e1.printStackTrace();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      recorder.reset();
      // 刻录完成一定要释放资源
      recorder.release();
      recorder = null;
      e1.printStackTrace();
    }

    try {
      recorder.start();
      isVoice = true;
      yuyinneirong.setVisibility(View.VISIBLE);
    } catch (Throwable e) {
      // TODO: handle exception
      releaseRecorder();
      // e.printStackTrace();
    }
  }

  // 释放录音
  private void releaseRecorder() {
    stopVoice();
  }

  // 停止录音
  private void stopVoice() {
    if (recorder != null) {
      recorder.setOnErrorListener(null);
      recorder.setPreviewDisplay(null);
      try {
        // 停止录音
        recorder.stop();
      } catch (RuntimeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        try {
          Thread.sleep(100);
        } catch (InterruptedException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        try {
          recorder.stop();
        } catch (Exception e1) {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e2) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
        try {
          Thread.sleep(100);
        } catch (InterruptedException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
      recorder.reset();
      // 刻录完成一定要释放资源
      recorder.release();
      recorder = null;
    }
  }

  /*
   * 录音计时
   */
  class TimeCount extends CountDownTimer {

    boolean isDialog;// 是否弹窗提示超时

    public TimeCount(long millisInFuture, long countDownInterval) {
      // 参数依次为总时长,和计时的时间间隔
      super(millisInFuture, countDownInterval);
    }

    @Override
    public void onFinish() {
      // 计时完毕时触发
      dialog();
      if (timer != null) {
        timer.cancel();
        timer = null;
      }
      stopVoice();// 停止录音
    }

    public void close() {
      if (timer != null) {
        timer.cancel();
        timer = null;
      }
    }

    @Override
    public void onTick(long millisUntilFinished) {
      // 计时过程显示

      yuyinshijian.setText(60000 / 1000 - millisUntilFinished / 1000 + "″");
    }
  }

  // 播放录音
  public void openFile() {
    MediaPlayer mp = new MediaPlayer();
    try {
      mp.reset();
      mp.setDataSource(voiceSaveDir + name + voiceName);
      mp.prepareAsync();
      mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
          mp.start();
        }
      });
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalStateException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void onGetRefusePhoto(String number) {
    // TODO Auto-generated method stub
    String msg = number;
    imageurl.remove(Integer.parseInt(msg));
    photoInfos.remove(Integer.parseInt(msg));
  }
}
