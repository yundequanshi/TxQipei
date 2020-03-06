package com.txsh.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.http.ZMHttpError;
import com.zuomei.model.MLSpecialResonse;

import cn.ml.base.utils.IEvent;

/**
 * 行业资讯
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TXInfoDetailFrg extends BaseFragment {

	public static TXInfoDetailFrg INSTANCE = null;

	public static String mUrl ;
	public static TXInfoDetailFrg instance(Object url) {
	//	if (INSTANCE == null) {
		mUrl = String.valueOf(url);
			INSTANCE = new TXInfoDetailFrg();
	//	}
		return INSTANCE;
	}

	@ViewInject(R.id.web)
	private WebView _webview;

	@ViewInject(R.id.top_title)
	private TextView _titleTv;
	
	private Context _context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tx_info_detail, null);
		ViewUtils.inject(this, view);
		_context = inflater.getContext();

		initView();
		return view;
	}

	private void initView() {
		_titleTv.setText("详情");
		_webview.getSettings().setJavaScriptEnabled(true);
	//	_webview.setInitialScale(25);//为25%，最小缩放等级 

		_webview.getSettings().setBuiltInZoomControls(true);
		_webview.getSettings().setUseWideViewPort(true);
		_webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		_webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				dismissProgressDialog();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				showProgressDialog("加载中...",_context);

			}
		});
		
		/*String 	url = String.format("%s%s", APIConstants.API_DEFAULT_HOST,
				APIConstants.API_LOTTERY_DETAIL);*/
		_webview.loadUrl(mUrl);
		
	/*	ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(
				RequestType.LOTTERY_DETAIL, null, null, _handler,
				HTTP_RESPONSE_SPECIAL_DETAIL, MLMyServices.getInstance());
		loadData(_context, message1);*/
	}

	/**
	 * @description 返回
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view) {
		getActivity().finish();
	}

	private static final int HTTP_RESPONSE_SPECIAL_DETAIL = 0;
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
			case HTTP_RESPONSE_SPECIAL_DETAIL: {
				MLSpecialResonse ret = (MLSpecialResonse) msg.obj;
				if (ret.state.equalsIgnoreCase("1")) {
					_webview.loadDataWithBaseURL(null, ret.datas, "text/html",
							"utf-8", null);

					// showMessage("添加进货成功!");
					// _event.onEvent(null, MLConstants.MY_STOCK);
				} else {
					showMessage("添加进货失败!");
				}
				break;
			}
			default:
				break;
			}
		}
	};

	private IEvent<Object> _event;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
