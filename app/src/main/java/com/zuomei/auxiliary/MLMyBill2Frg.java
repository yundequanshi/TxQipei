package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
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
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDialData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessagePublishResponse;
import com.zuomei.model.MLMyPacketData;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;
import com.zuomei.utils.MLPhotoUtil;
import com.zuomei.utils.ZMJsonParser;

import java.io.File;
import java.util.Calendar;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 汽修厂-我的账单
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyBill2Frg extends BaseFragment {

  public static MLMyBill2Frg INSTANCE = null;

  public static MLMyBill2Frg instance() {
    //	if(INSTANCE==null){
    INSTANCE = new MLMyBill2Frg();
    //	}
    return INSTANCE;
  }

  @ViewInject(R.id.add_phone)
  private ImageView _addIv;

  @ViewInject(R.id.tv_time)
  private TextView _timeTv;

  @ViewInject(R.id.tv_name)
  private TextView _nameTv;

  private Context _context;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.my_bill2_add, null);
    ViewUtils.inject(this, view);

    _context = inflater.getContext();

    initView();
    //	initData();
    return view;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }


  private void initData() {
    /*MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		  
		  
			if(user.isDepot){
				params.addParameter("depotId",user.Id);
			}else{
				params.addParameter("companyId",user.Id);
			}
			
		
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PACKET_INFO, null, params, _handler,HTTP_RESPONSE_MYPACK, MLMyServices.getInstance());
	       loadData(_context, message2);*/
  }

  private void initView() {

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

  @OnClick(R.id.add_phone)
  public void addOnClick(View view) {
		/* UIActionSheetDialog dialog = new UIActionSheetDialog(_context);
			dialog.addSheetItem("拍照", Color.BLACK);
			dialog.addSheetItem("相册选择", Color.BLACK);
			dialog.setOnActionSheetClickListener(new OnActionSheetClickListener() {
				@Override
				public void onClick(int index) {
					//选择了第index项
					MLPhotoUtil.choose(MLMyBill2Frg.this, index);
				}
			});
			dialog.show();*/
  }

  @OnClick(R.id.tv_name)
  public void businessOnClick(View view) {
    toActivity(_context, MLConstants.MY_BILL2_BUSINESS, null);
  }


  @OnClick(R.id.btn_list)
  public void listOnClick(View view) {
    _event.onEvent(null, MLConstants.MY_BILL2_BUSINESS_LIST);
  }


  @OnClick(R.id.btn_submit)
  public void okOnClick(View view) {

    if (_nameTv.getText().toString().equalsIgnoreCase("请选择发货的商家")) {
      showMessageWarning("商家名称不能为空!");
      return;
    }

    if (_timeTv.getText().toString().equalsIgnoreCase("请选择发货时间")) {
      showMessageWarning("发货时间不能为空!");
      return;
    }

    RequestParams params = new RequestParams();
    params.addBodyParameter("file", new File(MLPhotoUtil.getPhotoPath()));

    HttpUtils http = new HttpUtils();
    http.send(HttpRequest.HttpMethod.POST,
        APIConstants.API_IMAGE_UPLOAD,
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
    MLLogin user = ((BaseApplication) getActivity().getApplication()).get_user();
    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("depotUser.id", user.Id);
    params.addParameter("company.id", _data.userId + "");
    params.addParameter("sendTime", _timeTv.getText().toString() + "");
    params.addParameter("images", datas);

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PHONE_BUISNESS, null,
        params, _handler, HTTP_RESPONSE_UPDATE, MLMyServices.getInstance());
    loadData(_context, message2);
  }


  @OnClick(R.id.top_back)
  public void backOnClick(View view) {
    ((MLAuxiliaryActivity) _context).finish();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub

    MLPhotoUtil.photoZoom();
    Bitmap b = BitmapFactory.decodeFile(MLPhotoUtil.getPhotoPath());
    if (b != null) {
      _addIv.setImageBitmap(b);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
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
            showMessageSuccess("上传成功!");
            _event.onEvent(null, MLConstants.MY_BILL2_BUSINESS_LIST);
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


  protected void review(MLMyPacketData data) {

  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }
}
