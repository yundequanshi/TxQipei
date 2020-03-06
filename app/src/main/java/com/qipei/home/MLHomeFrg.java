package com.qipei.home;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyContactData;
import com.zuomei.model.MLMyContactResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLMyServices;

/**
 * 首页界面
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLHomeFrg extends BaseFragment{

	@ViewInject(R.id.home_tv_phone)
	private TextView mTvPhone;
	private Context _context;
	@ViewInject(R.id.home_et_search)
	private EditText mEtSearch;

	@ViewInject(R.id.home_tv_sign)
	private ImageView mIvSign;

	private MLLogin _user;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_tab1, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		_user = ((BaseApplication)getActivity().getApplication()).get_user();

		initPhone();
		return view;
	}

	private void initPhone() {
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter(MLConstants.PARAM_MY_CITYID, BaseApplication._currentCity);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_CONTACT, null, catalogParam, _handler, HTTP_RESPONSE_CONTACT,  MLMyServices.getInstance());
		loadDataWithMessage(_context, null, message1);
	}

	//搜索
	@OnClick(R.id.home_tv_search)
	public void searchOnClick(View view){
		toActivity(_context, MLConstants.HOME_SEARCH, mEtSearch.getText().toString());
	}
	//积分
	@OnClick(R.id.tab1_rr_integral)
	public void integralOnClick(View view){
		toActivity(_context,MLConstants.MY_LOTTERY_DETAIL,mEtSearch.getText().toString());
	}

	//签到
	@OnClick(R.id.home_tv_sign)
	public void signOnClick(View view){
		ZMRequestParams params = new ZMRequestParams();
		params.addParameter(MLConstants.PARAM_LOGIN_DEPORTID,_user.Id);
		ZMHttpRequestMessage message = new ZMHttpRequestMessage(ZMHttpType.RequestType.SIGN, null, params, _handler, HTTP_RESPONSE_SIGN, MLMyServices.getInstance());
		loadDataWithMessage(_context, null, message);
	}


	private void review(MLMyContactData datas) {

		mTvPhone.setText(String.format("今日拨打:%s次 总计拨打:%s次",datas.todayTime,datas.allTime));
	}

	private MLMyContactData _data;
	private static final int HTTP_RESPONSE_CONTACT= 0;
	private static final int HTTP_RESPONSE_SIGN = 1;
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
				case HTTP_RESPONSE_CONTACT: {
					MLMyContactResponse ret = (MLMyContactResponse) msg.obj;
					if (ret.state.equalsIgnoreCase("1")) {
						_data = ret.datas;
						review(ret.datas);
					} else {
						showMessageError("获取信息失败!");
					}
					break;
				}
				case HTTP_RESPONSE_SIGN:{
					MLRegister ret = (MLRegister) msg.obj;
					if(ret.datas){
						showMessageSuccess("签到成功!");
					//	BaseApplication.aCache.put("SIGN", getTimeDay());
					//	mIvSign.setImageResource();
						mIvSign.setEnabled(false);
					}else{
						showMessageError("今日已签到!");
					}
					break;
				}

			}
		}
	};
}
