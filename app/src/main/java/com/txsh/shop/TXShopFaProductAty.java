package com.txsh.shop;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;

import android.widget.TextView;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.baichang.android.utils.BCStringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.txsh.services.MLShopServices;
import com.txsh.utils.LXPhoto;
import com.txsh.utils.MLHttpImage;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.MLConstants;
import com.zuomei.home.MLAccidentPhotoAdapter;
import com.zuomei.home.MLMessageAddPop;
import com.zuomei.home.MLMessagePhotoPop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.photo.MLPhotoUtil;

/**
 * Created by Administrator on 2015/7/24.
 */
public class TXShopFaProductAty extends BaseActivity implements
    MLAccidentPhotoAdapter.GetRefusePhoto {

  //上面的图片
//    @ViewInject(R.id.inter_iv_one)
//    private ImageView mIvone;
//    @ViewInject(R.id.inter_iv_two)
//    private ImageView mIvtwo;
//    @ViewInject(R.id.inter_iv_three)
//    private ImageView mIvthree;
//    @ViewInject(R.id.inter_iv_four)
//    private ImageView mIvfour;
//    @ViewInject(R.id.inter_iv_five)
//    private ImageView mIvfive;
//    @ViewInject(R.id.inter_iv_six)
//    private ImageView mIvsix;


  //    //下面的图片
//    @ViewInject(R.id.product_iv_one)
//    private ImageView productmIvone;
//    @ViewInject(R.id.product_iv_two)
//    private ImageView productmIvtwo;
//    @ViewInject(R.id.product_iv_three)
//    private ImageView productmIvthree;
//    @ViewInject(R.id.product_iv_four)
//    private ImageView productmIvfour;
//    @ViewInject(R.id.product_iv_five)
//    private ImageView productmIvfive;
//    @ViewInject(R.id.product_iv_six)
//    private ImageView productmIvsix;
  /// 编辑框
  @ViewInject(R.id.shoppro_ed_name)
  private EditText productname;
  @ViewInject(R.id.shoppro_ed_price)
  private EditText productprice;
  @ViewInject(R.id.shoppro_ed_oldprice)
  private EditText productoldprice;
  @ViewInject(R.id.shoppro_ed_count)
  private EditText productcount;
  @ViewInject(R.id.shoppro_ed_freight)
  private EditText productfreight;
  @ViewInject(R.id.shoppro_ed_mobile)
  private EditText productmobile;
  @ViewInject(R.id.shoppro_ed_content)
  private EditText productContent;


  private String one, two, three, four, five, six;
  private String pone, ptwo, pthree, pfour, pfive, psix;
  private int mCurrent;
  private String companyId = "";
  private String content = "";
  List<Map<String, String>> mList;
  List<Map<String, String>> mList2;

  ///第二次
  @ViewInject(R.id.accident_add_root)
  private LinearLayout _root;

  @ViewInject(R.id.fabu_gv_photo1)
  private GridView _photoGv;

  @ViewInject(R.id.fabu_gv_photo2)
  private GridView _photoGv2;
  @ViewInject(R.id.shoppro_ed_fenlei)
  private TextView tvFenlei;

  private ArrayList<String> mPhotoList = new ArrayList<String>();
  private ArrayList<String> mPhotoList2 = new ArrayList<String>();


  private List<String> imageurl = new ArrayList<String>();
  private List<String> imageurl2 = new ArrayList<String>();


  private MLAccidentPhotoAdapter mImagePathAdapter;
  private MLAccidentPhotoAdapter mImagePathAdapter2;

  /* 用来标识请求gallery的activity */
  private static final int PHOTO_PICKED_WITH_DATA = 3021;
  // 照相机拍照得到的图片
  private File mCurrentPhotoFile;
  /* 用来标识请求照相功能的activity */
  private static final int CAMERA_WITH_DATA = 3023;
  /* 用来标识请求裁剪图片后的activity */
  private static final int CAMERA_CROP_DATA = 3022;


  /* 用来标识请求gallery的activity */
  private static final int PHOTO_PICKED_WITH_DATA2 = 30210;
  /* 用来标识请求照相功能的activity */
  private static final int CAMERA_WITH_DATA2 = 30230;
  /* 用来标识请求裁剪图片后的activity */
  private static final int CAMERA_CROP_DATA2 = 30220;

  public String type = "1";
  public String goodsTypeId = "";


  private static final boolean D = true;
  private MLMessageAddPop _pop;
  private MLMessagePhotoPop _photoPop;
  private int camIndex = 0;
  private int selectIndex = 0;
  private static final String TAG = "TXShopFAProductAty";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.tx_shop_fapro_aty);
    ViewUtils.inject(this);
    EventBus.getDefault().register(this);
    mList = new ArrayList<Map<String, String>>(0);
    mList2 = new ArrayList<Map<String, String>>(0);

    String username = BaseApplication.aCache.getAsString(MLConstants.PARAM_REGISTER_USERNAME);
    productmobile.setText(username);

    ////第二次的图片
    View mAvatarView = LayoutInflater.from(this).inflate(
        R.layout.message_choose_avatar, null);
    _pop = new MLMessageAddPop(this, mAvatarView);
    Button albumButton = (Button) mAvatarView
        .findViewById(R.id.choose_album);
    Button camButton = (Button) mAvatarView.findViewById(R.id.choose_cam);
    Button cancelButton = (Button) mAvatarView
        .findViewById(R.id.choose_cancel);

    albumButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 从相册中去获取
        try {
          Intent intent = new Intent(Intent.ACTION_PICK, null);
          intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
          if (MLStrUtil.compare(type, "1")) {
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
          } else {
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA2);
          }
        } catch (ActivityNotFoundException e) {
          showMessage("没有找到照片");
        }
      }
    });
    camButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        doPickPhotoAction();
      }
    });

    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        _pop.dismiss();
      }
    });

    MLPhotoUtil.clear();
    mPhotoList.clear();
    mPhotoList.add(String.valueOf(R.drawable.fabiaotupian));
    mImagePathAdapter = new MLAccidentPhotoAdapter(this, mPhotoList, 400,
        300);
    mImagePathAdapter.setHandler(this);
    _photoGv.setAdapter(mImagePathAdapter);

    mPhotoList2.clear();
    mPhotoList2.add(String.valueOf(R.drawable.fabiaotupian));
    mImagePathAdapter2 = new MLAccidentPhotoAdapter(this, mPhotoList2, 400,
        300);
    mImagePathAdapter2.setHandler(this);
    _photoGv2.setAdapter(mImagePathAdapter2);


  }

  @OnItemClick(R.id.fabu_gv_photo1)
  public void photoOnItemClick(AdapterView<?> arg0, View arg1, int position,
      long arg3) {
    type = "1";
    if (position == 6) {
      showMessage("最多添加6张图片!");
      return;
    }
    selectIndex = position;
    String path = mPhotoList.get(position);
    if (path.equalsIgnoreCase(String.valueOf(R.drawable.fabiaotupian))) {
      LXPhoto.getGudingPhoto(this, new OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
          if (!resultList.isEmpty()) {
            String currentFilePath3 = resultList.get(0).getPhotoPath();
            imageurl.add(currentFilePath3);
            mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, currentFilePath3);
            //          camIndex++;
            AbViewUtil.setAbsListViewHeight(_photoGv, 3, 25);
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

  @OnItemClick(R.id.fabu_gv_photo2)
  public void photo2OnItemClick(AdapterView<?> arg0, View arg1, int position,
      long arg3) {
    type = "2";
    if (position == 6) {
      showMessage("最多添加6张图片!");
      return;
    }
    selectIndex = position;
    String path = mPhotoList2.get(position);
    if (path.equalsIgnoreCase(String.valueOf(R.drawable.fabiaotupian))) {
//      _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
      LXPhoto.getGudingPhoto(this, new OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
          if (!resultList.isEmpty()) {
            String currentFilePath3 = resultList.get(0).getPhotoPath();
            imageurl2.add(currentFilePath3);
            mImagePathAdapter2.addItem(mImagePathAdapter2.getCount() - 1, currentFilePath3);
            //          camIndex++;
            AbViewUtil.setAbsListViewHeight(_photoGv2, 3, 25);
          }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
      });
    } else {
      //    choise();
      _photoPop = new MLMessagePhotoPop(this, path);
      _photoPop.showAtLocation(_root, Gravity.CENTER, 0, 0);
    }
  }

  @OnClick(R.id.shoppro_ed_fenlei)
  private void fenleiOnClick(View view) {
    startAct(getAty(), TXShopTypeActivity.class, "1");
  }

  @Subscribe
  public void event(MLEventBusModel model) {
    if (model.type == MLConstants.SELECT_TYPE) {
      TXHomeGoodsTypeImageData data = (TXHomeGoodsTypeImageData) model.obj[0];
      tvFenlei.setText(data.name);
      goodsTypeId = data.id;
    }
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
      if (MLStrUtil.compare(type, "1")) {
        startActivityForResult(intent, CAMERA_WITH_DATA);
      } else {
        startActivityForResult(intent, CAMERA_WITH_DATA2);
      }

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


  @OnClick(R.id.accident_btn_next)
  public void faBu(View view) {
    if (MLStrUtil.isEmpty(productname.getText().toString())) {
      showMessage("商品名称不能为空");
      return;
    }
    if (MLStrUtil.isEmpty(productprice.getText().toString().trim())) {
      showMessage("商品价格不能为空");
      return;
    }
      /*  if (MLStrUtil.isEmpty(productoldprice.getText().toString().trim())) {
            showMessage("商品原价不能为空");
            return;
        }*/
    if (MLStrUtil.isEmpty(productcount.getText().toString().trim())) {
      showMessage("商品库存不能为空");
      return;
    }
    if (MLStrUtil.isEmpty(productfreight.getText().toString().trim())) {
      showMessage("商品运费不能为空");
      return;
    }
    if (!MLStrUtil.isMobileNo(productmobile.getText().toString())) {
      showMessage("请填写正确的手机号");
      return;
    }

    if (imageurl.size() == 0) {
      showMessage("请上传商品轮播图");
      return;
    }
    if (imageurl2.size() == 0) {
      showMessage("请上传商品详情");
      return;
    }

    if (BCStringUtil.isEmpty(goodsTypeId)) {
      showMessage("请选择商品分类");
      return;
    }

    upload();
    //   uploadcattent();
  }

  public int listpos = 0;
  public int listpostoal = 0;

  public int listpos2 = 0;
  public int listpostoal2 = 0;

  private void upload() {
    listpostoal = imageurl.size();
    for (int i = 0; i < imageurl.size(); i++) {
      updateIcon(imageurl.get(i), "1");
    }
  }

  private void uploadcattent() {
    listpostoal2 = imageurl2.size();
    for (int i = 0; i < imageurl2.size(); i++) {
      updateIcon(imageurl2.get(i), "01");
    }
  }


  public void choise() {
    String s[] = {"拍照", "相册"};

    AlertDialog builder = MLDialogUtils.getAlertDialog(TXShopFaProductAty.this)
        .setItems(s, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            MLPhotoUtil.choose(TXShopFaProductAty.this, (which + 1));
          }
        })
        .setTitle("操作").create();
    //  builder.setCancelable(false);
    builder.show();
    MLDialogUtils.setDialogTitleColor(builder, getResources().getColor(R.color.common_orange));
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    _pop.dismiss();
    if (resultCode != android.app.Activity.RESULT_OK) {
      return;
    }
    switch (requestCode) {
      case PHOTO_PICKED_WITH_DATA:
        Uri uri = data.getData();
        String currentFilePath = getPath(uri);
        startPhotoZoom(uri);
        break;
      case CAMERA_WITH_DATA:
        if (D) {
          Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getAbsolutePath());
        }
        String currentFilePath2 = mCurrentPhotoFile.getAbsolutePath();
        Uri uri2 = Uri.fromFile(mCurrentPhotoFile);
        startPhotoZoom(uri2);

        break;
      case CAMERA_CROP_DATA:

        Bundle extras = data.getExtras();
        String currentFilePath3 = mCurrentPhotoFile.getAbsolutePath();

//			String path = mIntent.getStringExtra("PATH");
//			if (D)
//				Log.d(TAG, "裁剪后得到的图片的路径是 = " + path);
        imageurl.add(currentFilePath3);
        mImagePathAdapter.addItem(mImagePathAdapter.getCount() - 1, currentFilePath3);
        //          camIndex++;
        AbViewUtil.setAbsListViewHeight(_photoGv, 3, 25);
        break;

      case PHOTO_PICKED_WITH_DATA2:
        Uri uritwo = data.getData();
        String currentFilePathtwo = getPath(uritwo);
        startPhotoZoom(uritwo);
        break;
      case CAMERA_WITH_DATA2:
        if (D) {
          Log.d(TAG, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getAbsolutePath());
        }
        String currentFilePath2two = mCurrentPhotoFile.getAbsolutePath();
        Uri uri2two = Uri.fromFile(mCurrentPhotoFile);
        startPhotoZoom(uri2two);

        break;
      case CAMERA_CROP_DATA2:

        Bundle extrastwo = data.getExtras();
        String currentFilePath3two = mCurrentPhotoFile.getAbsolutePath();

//			String path = mIntent.getStringExtra("PATH");
//			if (D)
//				Log.d(TAG, "裁剪后得到的图片的路径是 = " + path);
        imageurl2.add(currentFilePath3two);
        mImagePathAdapter2.addItem(mImagePathAdapter2.getCount() - 1, currentFilePath3two);
        //          camIndex++;
        AbViewUtil.setAbsListViewHeight(_photoGv2, 3, 25);
        break;


    }


  }

  ////////////////////////////////////////////////////////////////

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
    if (MLStrUtil.compare(type, "1")) {
      startActivityForResult(intent, CAMERA_CROP_DATA);
    } else {
      startActivityForResult(intent, CAMERA_CROP_DATA2);
    }

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
    Cursor cursor = TXShopFaProductAty.this.managedQuery(uri, projection,
        null, null, null);
    int column_index = cursor
        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    String path = cursor.getString(column_index);
    return path;
  }
////////////////////////////////////////////////////////////////

  private void showChat() {
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
        .create();
    MLLogin user = ((BaseApplication) this.getApplication()).get_user();

    String content = productContent.getText().toString();

    ZMRequestParams param = new ZMRequestParams();
    param.addParameter("companyId", BaseApplication.getInstance().get_user().Id);
    param.addParameter("name", productname.getText().toString());
    param.addParameter("images", gson.toJson(mList));
    param.addParameter("content", gson.toJson(mList2));
    param.addParameter("price", productprice.getText().toString().trim());
    if (!MLStrUtil.isEmpty(productoldprice.getText().toString().trim())) {
      param.addParameter("oldPrice", productoldprice.getText().toString().trim());
    }

    if (!MLStrUtil.isEmpty(content)) {
      param.addParameter("msg", content);
    }

    param.addParameter("count", productcount.getText().toString().trim());
    param.addParameter("freight", productfreight.getText().toString().trim());
    param.addParameter("mobile", productmobile.getText().toString().trim());
    param.addParameter("goodsTypeId", goodsTypeId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.SHOPPRODUCTFABU,
        null, param,
        _handler, SHOPPRODUCTFABURETURN, MLShopServices.getInstance());
    loadDataWithMessage(this, "正在发表", message1);
  }

  private static final int SHOPPRODUCTFABURETURN = 1;

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
        case SHOPPRODUCTFABURETURN: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            showMessage("发表成功");
            finish();

          } else {
            showMessage("发表失败");
          }
          break;
        }
      }
    }
  };


  private void updateIcon(String photoPath, final String type) {
    MLHttpImage.updateIcon(TXShopFaProductAty.this, photoPath, new IEvent<List>() {
      @Override
      public void onEvent(Object source, List eventArg) {
        if (eventArg == null || eventArg.size() == 0) {
          dismissProgressDialog();
          showMessage("上传失败");
        } else {
          String path = "";
          try {
            JSONObject jsonObject = new JSONObject((String) eventArg.get(0));
            path = jsonObject.getString("path");
          } catch (JSONException e) {
            e.printStackTrace();
          }
          if (MLStrUtil.compare(type, "1")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("path", path);
            mList.add(map);
            listpos = listpos + 1;
            if (listpos == listpostoal) {
              uploadcattent();
            }
          }
          if (MLStrUtil.compare(type, "01")) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("path", path);
            mList2.add(map);
            listpos2 = listpos2 + 1;
            if (listpos2 == listpostoal2) {
              showChat();
            }
          }
          dismissProgressDialog();

        }
      }
    });
  }


  @Override
  public void onGetRefusePhoto(String number) {
    String msg = number;
    imageurl.remove(Integer.parseInt(msg));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  @OnClick(R.id.home_top_back)
  private void backOnClick(View view) {
    finish();
  }
}
