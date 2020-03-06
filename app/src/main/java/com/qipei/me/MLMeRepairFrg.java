package com.qipei.me;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyInfoData;
import com.zuomei.model.MLMyInfoResponse;
import com.zuomei.services.MLMyServices;

import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * 我的
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMeRepairFrg extends BaseFragment{


	private Context _context;
	private MLMyInfoData _info;

	@ViewInject(R.id.me_iv_icon)
	private RoundedImageView mIvIcon;
	@ViewInject(R.id.me_tv_name)
	private TextView mTvName;
	@ViewInject(R.id.me_tv_phone)
	private TextView mTvPhone;
	@ViewInject(R.id.me_tv_jf)
	private TextView mTvJf;
	@ViewInject(R.id.me_tv_yue)
	private TextView mTvYue;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_me, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();

		initData();
		return view;
	}


	//获取商家的基本信息
	private void initData() {
		MLLogin data = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams catalogParam = new ZMRequestParams();
		catalogParam.addParameter("depotId", data.Id);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.MY_INFO_B, null, catalogParam, _handler, HTTP_RESPONSE_INFO,  MLMyServices.getInstance());
		loadDataWithMessage(_context, null, message1);
	}

	@OnClick({R.id.me_tv_balance,R.id.me_tv_phone1,R.id.me_tv_deal,R.id.me_tv_accident,R.id.me_tv_incident,
			R.id.me_tv_offer,R.id.me_tv_integral})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.me_tv_balance:{
				//余额
				startAct(MLMeRepairFrg.this,MLBalanceAty.class);
				break;
			}
			case R.id.me_tv_integral:{
				//积分
				startAct(MLMeRepairFrg.this,MLIntegralAty.class);
				break;
			}
			case R.id.me_tv_phone1:{
				//通话记录
				toActivity(_context, MLConstants.MY_PHONE_D, null);
				break;
			}
			case R.id.me_tv_accident:{
				//事故车
				toActivity(_context, MLConstants.MY_ACCIDENT,null);
				break;
			}
			case R.id.me_tv_deal:{
				//交易记录
				break;
			}
			case R.id.me_tv_incident:{
				//事故件
				toActivity(_context, MLConstants.MY_LEAVE,null);
				break;
			}
			case R.id.me_tv_offer:{
				//报价管理
				toActivity(_context, MLConstants.MY_ACCIDENT_PART,null);
				break;
			}
		}
	}


	@OnClick(R.id.me_rl_info)
	public void infoOnClick(View view){
		startAct(MLMeRepairFrg.this,MLMePersonAty.class);
	}


	private static final int HTTP_RESPONSE_INFO= 0;
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
				case HTTP_RESPONSE_INFO:{
					MLMyInfoResponse ret = (MLMyInfoResponse) msg.obj;
					if(ret.state.equalsIgnoreCase("1")){
						_info =  ret.datas;

						setHead();
					}else{
						showMessage("获取头像失败!");
					}
					break;
				}
				default:
					break;
			}
		}
	};


	protected void setHead() {
		//头像
		if(_info.userPhoto<=0){
			mIvIcon.setImageResource(R.drawable.default_my_info_head);
		}else{
			String iconUrl = APIConstants.API_IMAGE+"?id="+_info.userPhoto;
			mIvIcon.setTag(iconUrl);
			if (!BaseApplication.IMAGE_CACHE.get(iconUrl, mIvIcon)) {
				mIvIcon.setImageDrawable(null);
			}
		}
		//名字
		mTvName.setText(_info.depotName);
		mTvPhone.setText(_info.userPhone);
		mTvJf.setText("积分："+_info.integral);
		mTvYue.setText("余额："+_info.money);
	}

}
