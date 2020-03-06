package com.txsh.home;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.exception.ZMParserException;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDialData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;
import java.util.Calendar;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.photo.MLPhotoFragUtil;
import cn.ml.base.utils.photo.MLPicData;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Marcello on 2015/6/12.
 */
public class TXInvoiceAddFrag extends BaseFragment {

  private Context _context;

  @ViewInject(R.id.add_iv)
  private ImageView _addIv;

  @ViewInject(R.id.et_submit)
  private EditText mEtPrice;

  @ViewInject(R.id.tv_name)
  private TextView _nameTv;

  @ViewInject(R.id.tv_time)
  private TextView _timeTv;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.qp_invoice_add, null);
    ViewUtils.inject(this, view);
    _context = inflater.getContext();

    return view;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }


  @OnClick({R.id.add_tv_photograph, R.id.add_tv_album})
  public void camera(View view) {

    if (view.getId() == R.id.add_tv_album) {
      //相册
      MLPhotoFragUtil.choose(TXInvoiceAddFrag.this, 0);
    } else {
      //拍照
      MLPhotoFragUtil.choose(TXInvoiceAddFrag.this, 1);
    }
  }

  @OnClick(R.id.tv_name)
  public void businessOnClick(View view) {
    toActivity(_context, MLConstants.MY_BILL2_BUSINESS, null);
  }

  @OnClick(R.id.tv_time)
  public void timeOnClick(View view) {
    Calendar c = Calendar.getInstance();
    new DatePickerDialog(
        _context, DatePickerDialog.THEME_HOLO_LIGHT,
        new DatePickerDialog.OnDateSetListener() {
          public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {

            String _month = "";
            String _dayOfMonth = "";

            if ((month + 1) < 10) {
              _month = "0" + (month + 1);
            } else {
              _month = String.valueOf(month + 1);
            }

            if (dayOfMonth < 10) {
              _dayOfMonth = "0" + dayOfMonth;
            } else {
              _dayOfMonth = dayOfMonth + "";
            }

            String text = String.format("%s-%s-%s", year, _month, _dayOfMonth);

            _timeTv.setText(text);
          }
        },
        c.get(Calendar.YEAR), // 传入年份
        c.get(Calendar.MONTH), // 传入月份
        c.get(Calendar.DAY_OF_MONTH) // 传入天数
    ).show();
  }

  public void okOnClick() {

    if (_nameTv.getText().toString().equalsIgnoreCase("请选择发货的商家")) {
      showMessageWarning("商家名称不能为空!");
      return;
    }

    String price = mEtPrice.getText().toString();
    if (MLStrUtil.isEmpty(price)) {
      showMessageWarning("请填写金额!");
      return;
    }

    if (MLStrUtil.isEmpty(MLPhotoFragUtil.getPhotoPath())) {
      showMessageWarning("请上传发货单!");
      return;
    }

     /*   if(_timeTv.getText().toString().equalsIgnoreCase("请选择发货时间")){
            showMessageWarning("发货时间不能为空!");
            return;
        }*/

    RequestParams params = new RequestParams();
    params.addBodyParameter("file", new File(MLPhotoFragUtil.getPhotoPath()));

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST,
        APIConstants.API_IMAGE_UPLOAD_OLD,
        params,
        new RequestCallBack<String>() {
          @Override
          public void onStart() {
            showProgressDialog("正在上传...", _context);
          }

          @Override
          public void onLoading(long total, long current, boolean isUploading) {
          }

          @Override
          public void onSuccess(ResponseInfo<String> responseInfo) {
            try {
              MLMessagePublishResponse ret = ZMJsonParser
                  .fromJsonString(MLMessagePublishResponse.class, responseInfo.result);
              request(ret.datas);
            } catch (ZMParserException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }

          @Override
          public void onFailure(HttpException error, String msg) {
            dismissProgressDialog();
            showMessage("图片上传失败!");
          }
        });


  }

  private void request(String datas) {
    if (_data == null) {
      return;
    }
    MLLogin user = ((BaseApplication) getActivity().getApplication()).get_user();
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("depotUser.id", user.Id);
    params.addParameter("company.id", _data.userId + "");
    params.addParameter("sendTime", _timeTv.getText().toString() + "");
    params.addParameter("images", datas);
    params.addParameter("money", mEtPrice.getText().toString());

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(
        ZMHttpType.RequestType.MY_PHONE_BUISNESS, null, params, _handler, HTTP_RESPONSE_UPDATE,
        MLMyServices.getInstance());
    loadData(_context, message2);
  }

  private static final int HTTP_RESPONSE_UPDATE = 0;
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
        case HTTP_RESPONSE_UPDATE: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.datas) {
            showMessageSuccess("亲，上传审核中请稍后");
            review();
            //    _event.onEvent(null, "update");
          } else {
            showMessageError("上传失败!");
          }
          break;
        }

        default:
          break;
      }
    }
  };

  private void review() {
    _addIv.setImageResource(R.drawable.fahuodansuoluetu);
    _nameTv.setText("");
    _timeTv.setText("");
  }


  @Override

  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (MLPhotoFragUtil.IsCancel() && requestCode != 100) {
      showMessage("操作已取消!");
      return;
    }
    Bitmap b = MLPhotoFragUtil.gePhotoBitmap();
    if (requestCode == 100 && data != null) {
//相册选择返回
      MLPhotoFragUtil.photoZoom(data.getData());
    } else if (requestCode == 101) {
//拍照返回 调用裁剪
      MLPhotoFragUtil.photoZoom(null);
    } else if (requestCode == 102 && resultCode != 0) {
//裁剪返回
      MLPicData pic = new MLPicData();
      pic.mBitMap = MLPhotoFragUtil.gePhotoBitmap();
      pic.path = MLPhotoFragUtil.getPhotoPath();

      _addIv.setImageBitmap(pic.mBitMap);
    }
  }


  private MLDialData _data;

  @Subscribe
  public void setBusiness(MLDialData data) {
    if (data == null) {
      return;
    }
    _data = data;
    _nameTv.setText(data.userName + "");


  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
