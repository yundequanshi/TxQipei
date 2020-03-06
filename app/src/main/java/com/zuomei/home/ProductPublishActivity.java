package com.zuomei.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import cn.finalteam.galleryfinal.GalleryFinal.OnHanlderResultCallback;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import com.baichang.android.utils.BCDialogUtil;
import com.baichang.android.utils.BCStringUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.FileUpload;
import com.txsh.quote.Flag;
import com.txsh.utils.LXPhoto;
import com.zuomei.base.BaseActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.MLEventBusModel;
import com.zuomei.constants.APIConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;
import com.zuomei.utils.ZMJsonParser;
import java.io.File;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class ProductPublishActivity extends BaseActivity {

  @ViewInject(R.id.iv_photo)
  ImageView ivPhoto;
  @ViewInject(R.id.et_name)
  EditText etName;

  private String imagePath = "";
  private MLLogin login = new MLLogin();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_publish);
    ViewUtils.inject(this);
    login = BaseApplication.getInstance().get_user();
  }

  public void addPhoto(View view) {
    LXPhoto.getGudingPhoto(getAty(), new OnHanlderResultCallback() {
      @Override
      public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
        if (!resultList.isEmpty()) {
          imagePath = resultList.get(0).getPhotoPath();
          Glide.with(getAty())
              .load(imagePath)
              .error(R.drawable.sc_photo)
              .into(ivPhoto);
        }
      }

      @Override
      public void onHanlderFailure(int requestCode, String errorMsg) {

      }
    });
  }

  public void back(View view) {
    finish();
  }

  public void publishPhoto(View view) {
    String name = etName.getText().toString();
    if (BCStringUtil.isEmpty(name)) {
      showMessage("名称不能为空");
      return;
    }

    if (BCStringUtil.isEmpty(imagePath)) {
      showMessage("图片不能为空");
      return;
    }
    BCDialogUtil.showProgressDialog(getAty(), "正在发布");
    uploadImage(name, imagePath);
  }

  /**
   * 上传照片
   */
  private void uploadImage(final String name, String photoPath) {
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
              addCompanyProducts(name, imageUpload.getRes().getData()
                  .get(0).getPath());
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            showMessage("图片上传失败!");
          }
        });
  }

  public void addCompanyProducts(String name, String picture) {
    ZMRequestParams catalogParam = new ZMRequestParams();
    catalogParam.addParameter("companyId", login.Id);
    catalogParam.addParameter("name", name);
    catalogParam.addParameter("picture", picture);
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.ADD_COMPANY_PRODUCTS,
        null, catalogParam, _handler, HTTP_RESPONSE_PRODUCT_MANGER_ADD,
        MLHomeServices.getInstance());
    loadDataWithMessage(getAty(), null, message1);
  }

  private static final int HTTP_RESPONSE_PRODUCT_MANGER_ADD = 1;
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
        case HTTP_RESPONSE_PRODUCT_MANGER_ADD: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.state.equalsIgnoreCase("1")) {
            BCDialogUtil.dismissProgressDialog();
            showMessage("发布成功");
            EventBus.getDefault().post(new MLEventBusModel(Flag.EVENT_PUBLISH_PRODUCT_MANGER));
            finish();
          } else {
            showMessage("发布失败!");
          }
          break;
        }
        default:
          break;
      }
    }
  };
}
