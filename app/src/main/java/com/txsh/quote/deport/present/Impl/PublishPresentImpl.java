package com.txsh.quote.deport.present.Impl;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.baichang.android.widget.BCNoScrollListView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.txsh.R;
import com.txsh.model.FileUpload;
import com.txsh.quote.BCPopUpWindowsUtils;
import com.txsh.quote.Flag;
import com.txsh.quote.IBaseInteraction.BaseListener;
import com.txsh.quote.deport.PublishActivity;
import com.txsh.quote.deport.adapter.PeijianAdapter;
import com.txsh.quote.deport.entity.DeportInfoData;
import com.txsh.quote.deport.entity.PartsData;
import com.txsh.quote.deport.model.Impl.PublishInteractionImpl;
import com.txsh.quote.deport.model.PublishInteraction;
import com.txsh.quote.deport.present.PublishPresent;
import com.txsh.quote.deport.view.PublishView;
import com.txsh.utils.LXPhoto;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLAppDiskCache;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.APIConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.TXCarTypeResponse;
import com.zuomei.model.TXHomeCatalogResponse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.MLToolUtil;
import com.zuomei.utils.ZMJsonParser;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by 汉玉 on 2017/3/14.
 */
public class PublishPresentImpl implements PublishPresent {

  private PublishView mView;
  private PublishInteraction mInteraction;

  private PeijianAdapter mAdapter;
  private List<PartsData> peiJianDatas = new ArrayList<>();
  private String number = "0";
  private String deportId = "";
  private MLLogin user = new MLLogin();
  private TextView tvType = null;
  private String typeName = "";
  private String typeId = "";
  private String imagePath = "";

  public PublishPresentImpl(PublishView publishView) {
    this.mView = publishView;
    this.mInteraction = new PublishInteractionImpl();
    user = MLAppDiskCache.getLoginUser();
    deportId = user.Id;
  }

  @Override
  public void attachListView(BCNoScrollListView listView, Activity activity) {
    mAdapter = new PeijianAdapter(activity, R.layout.bj_item_peijian);
    listView.setAdapter(mAdapter);
  }

  @Override
  public void showPopupWindow(View view, final Activity activity) {
    if (!BCStringUtil.isEmpty(imagePath)) {
      mView.showMsg("上传配件单照片后不能再手动填写配件");
      return;
    }
    View contentView = LayoutInflater.from(activity).inflate(R.layout.bj_pop_add_pj, null);
    final PopupWindow mpop = BCPopUpWindowsUtils
        .getIstnace()
        .getPopUpWindows(contentView, 0, 0, activity, 0.3f, false)
        .showCenterOfView(view);
    ImageView ivDismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
    TextView tvAdd = (TextView) contentView.findViewById(R.id.tv_add);
    final EditText etName = (EditText) contentView.findViewById(R.id.et_pj_name);
    final EditText etNum = (EditText) contentView.findViewById(R.id.et_pj_num);
    final String[] types = new String[]{"原厂", "副厂", "拆车", "品牌"};
    tvType = (TextView) contentView.findViewById(R.id.tv_pj_type);
    tvType.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        BCDialogUtil.getDialogItem(activity, "", types, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            typeId = which + "";
            typeName = types[which];
            if (tvType != null) {
              tvType.setText(typeName);
            }
          }
        });
      }
    });
    ivDismiss.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mpop.dismiss();
      }
    });
    tvAdd.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        String name = etName.getText().toString();
        String num = etNum.getText().toString();
        if (BCStringUtil.isEmpty(name)) {
          mView.showMsg("名字不能为空");
          return;
        }

        if (BCStringUtil.isEmpty(num)) {
          mView.showMsg("数量不能为空");
          return;
        }

        if (BCStringUtil.isEmpty(typeName)) {
          mView.showMsg("分类不能为空");
          return;
        }

        peiJianDatas = mAdapter.getList();
        peiJianDatas.add(new PartsData(name, num, typeName, typeId));
        mAdapter.setData(peiJianDatas);
        mpop.dismiss();
      }
    });
  }

  @Override
  public void addOfferSheet(String typeId, String typeChildId, String typeName, String childType,
      String years, String displacement, String carJia, String logisticsName,
      final Activity activity) {
    if (BCStringUtil.isEmpty(typeName)) {
      mView.showMsg("车型不能为空");
      return;
    }

//    if (BCStringUtil.isEmpty(childType)) {
//      mView.showMsg("子车型不能为空");
//      return;
//    }
//
//    if (BCStringUtil.isEmpty(years)) {
//      mView.showMsg("年款不能为空");
//      return;
//    }
//
//    if (BCStringUtil.isEmpty(displacement)) {
//      mView.showMsg("排量不能为空");
//      return;
//    }

//    if (BCStringUtil.isEmpty(logisticsName)) {
//      mView.showMsg("物流不能为空");
//      return;
//    }

    List<PartsData> peiJianDatas = mAdapter.getList();

    if (peiJianDatas.isEmpty() && BCStringUtil.isEmpty(imagePath)) {
      mView.showMsg("配件和图片请添加一个");
      return;
    }
    DeportInfoData deportInfoData = new DeportInfoData();
    deportInfoData.headPic = user.headPic;
    deportInfoData.hxUser = user.hxUser;
    deportInfoData.hxPwd = user.hxPwd;
    deportInfoData.id = user.Id;
    deportInfoData.name = user.name;
    deportInfoData.phone = user.phone;
    String depotInfo = (new Gson()).toJson(deportInfoData).toString();
    number = peiJianDatas.size() + "";
    String parts = (new Gson()).toJson(peiJianDatas).toString();
    Map<String, String> map = new HashMap<>();
    map.put("typeId", typeId);
    map.put("typeName", typeName);
    map.put("childType", childType);
    map.put("years", years);
    map.put("displacement", displacement);
    map.put("logisticsName", logisticsName);
    map.put("depotId", deportId);
    map.put("depotInfo", depotInfo);
    map.put("vin", carJia);
    map.put("parts", parts);
    map.put("number", number);
    map.put("cityId", BaseApplication._currentCity+"");
    if (BCStringUtil.isEmpty(imagePath)) {
      publishQuoted(map, activity);
    } else {
      BCDialogUtil.showProgressDialog(activity, "正在上传图片");
      uploadImage(map, imagePath, activity);
    }
  }

  @Override
  public void selectYear(Activity activity, final TextView etYears) {
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    if (year < 2017) {
      year = 2017;
    }
    int size = (year + 1) - 2000;
    final String[] years = new String[size + 1];
    for (int i = 0; i <= size; i++) {
      years[i] = ((2000 + size) - i) + "";
    }
    BCDialogUtil.getDialogItem(activity, "选择年份", years, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        etYears.setText(years[which]);
      }
    });
  }

  @Override
  public void selectPhoto(final Activity activity, final ImageView quoted,
      final ImageView ivQuoted) {
    List<PartsData> peiJianDatas = mAdapter.getList();
    if (!peiJianDatas.isEmpty()) {
      mView.showMsg("手动填写配件后不能再上传配件单照片");
      return;
    }
    LXPhoto.getZiyouPhoto(activity, new OnHanlderResultCallback() {
      @Override
      public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if (!resultList.isEmpty()) {
          imagePath = resultList.get(0).getPhotoPath();
          Glide.with(activity)
              .load(imagePath)
              .error(R.drawable.image_error)
              .into(quoted);
          ivQuoted.setVisibility(View.VISIBLE);
        }
      }

      @Override
      public void onHanlderFailure(int requestCode, String errorMsg) {

      }
    });
  }

  /**
   * 上传照片
   */
  private void uploadImage(final Map<String, String> map, String photoPath,
      final Activity activity) {
    RequestParams params = new RequestParams();
    params.addBodyParameter("file", new File(photoPath));
    HttpUtils http = new HttpUtils();
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
            try {
              Gson gson = new Gson();
              FileUpload imageUpload = gson.fromJson(
                  responseInfo.result, FileUpload.class);
              BCDialogUtil.dismissProgressDialog();
              map.put("isPicture", "0");
              map.put("picture", imageUpload.getRes().getData()
                  .get(0).getPath());
              publishQuoted(map, activity);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            mView.showMsg("图片上传失败!");
          }
        });
  }

  /**
   * 发布报价
   */
  private void publishQuoted(Map<String, String> map, final Activity activity) {
    mInteraction.addOfferSheet(map, activity, new BaseListener<Boolean>() {
      @Override
      public void success(Boolean aBoolean) {
        if (aBoolean) {
          mView.showMsg("报价发布成功");
          EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_QUOTED_LIST_REFRESH));
          activity.finish();
        }
      }

      @Override
      public void error(String error) {

      }
    });
  }

  @Override
  public void delete(PublishActivity activity, ImageView ivQuoted, ImageView ivQuotedDelete) {
    imagePath = "";
    ivQuoted.setImageResource(R.drawable.image_error);
    ivQuotedDelete.setVisibility(View.GONE);
  }

  @Override
  public void selectChildType(String typeId, Activity activity) {
    if (BCStringUtil.isEmpty(typeId)) {
      mView.showMsg("请先选择车型");
      return;
    }
    initTwoLevelData(typeId);
  }

  private void initTwoLevelData(String superId) {
    //获取分类
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("superId", superId);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.GET_SECOND_COMPANYTYPE2,
        null, catalogParam, _handler, HTTP_RESPONSE_CATALOG_CAR_TWO_LEVEL,
        MLHomeServices.getInstance());
    loadDataWithMessage(null, message1);
  }

  private static final int HTTP_RESPONSE_CATALOG_CAR_TWO_LEVEL = 1;
  private Handler _handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      if (msg == null || msg.obj == null) {
        return;
      }
      if (msg.obj instanceof ZMHttpError) {
        ZMHttpError error = (ZMHttpError) msg.obj;
        return;
      }
      switch (msg.what) {
        //获取二级车型
        case HTTP_RESPONSE_CATALOG_CAR_TWO_LEVEL: {
          TXCarTypeResponse ret = (TXCarTypeResponse) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            if (ret.datas.isEmpty()) {
              mView.showMsg("此车型暂无子车型");
            } else {
              final List<MLHomeCatalogData> datas = ret.datas;
              String[] types = new String[datas.size()];
              for (int i = 0; i < datas.size(); i++) {
                types[i] = datas.get(i).name;
              }
              BCDialogUtil.getDialogItem(mView.getNowAty(), "选择子车型", types,
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      MLHomeCatalogData data = datas.get(which);
                      mView.setChildType(data.name, data.id);
                    }
                  });
            }
          } else {
            mView.showMsg("获取二级失败!");
          }
          break;
        }
        default:
          break;
      }
    }
  };

  protected void loadDataWithMessage(String message, ZMHttpRequestMessage httpMessage) {
    new PostMessageTask(message).execute(httpMessage);
  }


  private class PostMessageTask extends AsyncTask<ZMHttpRequestMessage, Void, Object> {

    ZMHttpRequestMessage _hm;
    String message;

    public PostMessageTask() {
      this.message = null;
    }

    public PostMessageTask(String message) {
      this.message = message;
    }

    @Override
    protected void onPreExecute() {
      if (this.message == null) {
        return;
      }
    }

    @Override
    protected Object doInBackground(ZMHttpRequestMessage... params) {

      _hm = params[0];
      Object obj = null;
      ZMHttpError error = new ZMHttpError();
      try {
        obj = _hm.getWebService().httpPost(params[0]);
      } catch (Exception e) {
        error.errorMessage = (e == null || MLToolUtil.isNull(e.getMessage())) ?
            MLToolUtil.getResourceString(R.string.unknown_error) : e.getMessage();
      }
      if (!MLToolUtil.isNull(error.errorMessage)) {
        obj = error;
      }

      return obj;
    }

    @Override
    protected void onPostExecute(Object obj) {
      Message message = new Message();
      message.what = _hm.getHandlerMessageID();
      message.obj = obj;
      _hm.getHandler().sendMessage(message);
    }
  }

  @Override
  public void onDestroy() {
    mView = null;
  }

  @Override
  public void onStart() {

  }
}
