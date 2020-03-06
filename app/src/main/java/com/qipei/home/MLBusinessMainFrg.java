package com.qipei.home;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLLogin;

/**
 * 商家详情
 * @author Marcello
 *
 */
@SuppressLint("ValidFragment")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLBusinessMainFrg extends BaseFragment{

	private MLHomeBusinessData mData;
	private Context _context;

	public MLBusinessMainFrg(MLHomeBusinessData data){
		mData = data;
	}

	private MLLogin _user;

	@ViewInject(R.id.detail_iv_slide)
	private ImageView mIvSlide;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.qp_busines_detail_main, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();

		_user =	((BaseApplication)getActivity().getApplication()).get_user();

	//	initPlayView();
	//	initData();
		return view;
	}

	/*private void initPlayView() {


		mIvSlide.setOnClickListener(new AbSlidingPlayView.AbOnItemClickListener() {
			@Override
			public void onClick(int position) {
				if (getHeadImageUrl().size() == 0) {
					return;
				}
				MLHomeProductPop _pop = new MLHomeProductPop(getActivity(), getHeadImageUrl(), position);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
			}
		});


	}

	private void initData() {
		//	MLLogin user = BaseApplication._user;
		MLLogin user = 	((BaseApplication)getActivity().getApplication()).get_user();

		if(!user.isDepot){

		}else{
			_collectBtn.setVisibility(View.VISIBLE);
			_moneyRl.setVisibility(View.VISIBLE);
			_payRl.setVisibility(View.VISIBLE);
			_payBtn.setVisibility(View.VISIBLE);
		}


		ZMRequestParams catalogParam = new ZMRequestParams();
		//判断是否来自收藏列表页面
		if(_oldBusiness.isCollectParam){
			catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID,_oldBusiness.compayId);
		}else{
			catalogParam.addParameter(MLConstants.PARAM_HOME_BUSINESSID,_oldBusiness.id);
		}
		catalogParam.addParameter(MLConstants.PARAM_MY_DEPTID2,user.Id);
		ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(ZMHttpType.RequestType.HOME_BUSINESS_DETAIL, null, catalogParam, _handler, HTTP_RESPONSE_BUSINESS, MLHomeServices.getInstance());
		loadDataWithMessage(_context, null, message1);
	}*/
}
