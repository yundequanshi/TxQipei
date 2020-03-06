package com.zuomei.home;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.sliding.AbSlidingPlayView.AbOnItemClickListener;
import com.ab.view.sliding.AbSlidingPlayView.AbOnTouchListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.txsh.adapter.TXDetailImageAdapter;
import com.zuomei.auxiliary.MLAuxiliaryActivity;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.APIConstants;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAccidentDetailData;
import com.zuomei.model.MLAccidentDetailResponse;
import com.zuomei.model.MLAccidentInfo;
import com.zuomei.services.MLAccidentServices;
import com.zuomei.utils.MLStringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 事故车详情
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLAccidentDetailFrg extends BaseFragment {

	public static MLAccidentDetailFrg INSTANCE = null;
	public static MLAccidentInfo data;

	public static MLAccidentDetailFrg instance(Object obj) {
		MLAccidentDetailData d = (MLAccidentDetailData) obj;
		data = d.info;
		// if(INSTANCE==null){
		INSTANCE = new MLAccidentDetailFrg();
		// }
		return INSTANCE;
	}

	@ViewInject(R.id.accident_playView)
	private AbSlidingPlayView _playView;

	@ViewInject(R.id.accident_scrollview)
	private ScrollView _scrollview;

	@ViewInject(R.id.root)
	private RelativeLayout _root;

	/** 行驶里程 */
	@ViewInject(R.id.accident_tv_title)
	private TextView _titleTv;

	/** 行驶里程 */
	@ViewInject(R.id.accident_tv_mileage)
	private TextView _mileageTv;

	/** 上牌日期 */
	@ViewInject(R.id.accident_tv_platedata)
	private TextView _platedataTv;

	/** 买时裸车价 */
	@ViewInject(R.id.accident_tv_oldprice)
	private TextView _oldpriceTv;

	/** 车牌所在地 */
	@ViewInject(R.id.accident_tv_address)
	private TextView _addressTv;

	/** 排量 */
	@ViewInject(R.id.accident_tv_displacement)
	private TextView _displacementTv;

	/** 受损部位 */
	@ViewInject(R.id.accident_tv_damaged)
	private TextView _damagedTv;

	/** 发布时间 */
	@ViewInject(R.id.accident_tv_addtime)
	private TextView _addtimeTv;

	/** 价格 */
	@ViewInject(R.id.accident_tv_price)
	private TextView _priceTv;

	/** 车主描述 */
	@ViewInject(R.id.accident_tv_detail)
	private TextView _detailTv;

	/** 车主描述 */
	@ViewInject(R.id.accident_tv_phone)
	private TextView _phoneTv;
	@ViewInject(R.id.horizon_listview)
	private ViewPager horizon_listview;

	private Context _context;
//	HorizontalListViewAdapter horizontalListViewAdapter;
	MLAccidentDetailResponse ret;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.accident_car_detail, null);
		ViewUtils.inject(this, view);
		horizon_listview = (ViewPager) view.findViewById(R.id.horizon_listview);


		_context = inflater.getContext();
		initView();
		return view;
	}

	private void initView() {

		ZMRequestParams params = new ZMRequestParams();
		params.addParameter(MLConstants.PARAM_ACCIDENT_ID, data.id);

		ZMHttpRequestMessage message = new ZMHttpRequestMessage(
				RequestType.AACIDENT_DETAIL, null, params, _handler,
				HTTP_RESPONSE_ACCIDENT_DETIAL, MLAccidentServices.getInstance());
		loadDataWithMessage(_context, null, message);

	}

	private void initTextView(TextView tv, int index) {
		String text = tv.getText().toString();
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.BLACK);
		builder.setSpan(redSpan, index, text.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(builder);
	}

	private void initPlayView(String image) {

		final String imageId[] = image.split(",");
		for (String id : imageId) {
			ImageView iv = getImageView();
			_playView.addView(iv);
			String imgUrl = APIConstants.API_IMAGE + "?id=" + id;
			// bitmapUtils.display(iv, imgUrl, bigPicDisplayConfig);
			BaseApplication.IMAGE_CACHE.get(imgUrl, iv);
		}
		_playView.setOnTouchListener(new AbOnTouchListener() {
			@Override
			public void onTouch(MotionEvent event) {
				_scrollview.requestDisallowInterceptTouchEvent(true);
			}
		});
		_playView.setOnItemClickListener(new AbOnItemClickListener() {

			@Override
			public void onClick(int position) {
				/*
				 * String path =
				 * APIConstants.API_IMAGE+"?id="+imageId[position];
				 * MLMessagePhotoPop _pop = new MLMessagePhotoPop(getActivity(),
				 * path); _pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
				 */
				MLHomeProductPop _pop = new MLHomeProductPop(getActivity(),
						getHeadImageUrl(), position);
				_pop.showAtLocation(_root, Gravity.CENTER, 0, 0);
			}
		});
	}

	public ImageView getImageView() {
		ImageView image = new ImageView(_context);
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// image.setImageResource(id);
		// image.setBackgroundColor(getResources().getColor(R.color.common_red));
		image.setScaleType(ScaleType.CENTER_CROP);
		return image;
	}

	private MLAccidentInfo _data;

	private void review(MLAccidentInfo data) {
		_data = data;
		initPlayView(data.image);
		String time = MLStringUtils.time_year(data.MCtime);
		_titleTv.setText(data.accidentName);
		_mileageTv.setText("行驶里程：    " + data.mileage + "万公里");
		_addtimeTv.setText("发布时间：	 " + time);
		_oldpriceTv
				.setText(Html.fromHtml(String
						.format("<font color=\"#989898\" >%s</font><font color=\"#ff0000\">%s</font><font color=\"#989898\">%s</font>",
								"新车裸车价格：", data.oldPrice, "  万元")));

		_priceTv.setText(Html.fromHtml(String
				.format("<font color=\"#989898\" >%s</font><font color=\"#ff0000\">%s</font><font color=\"#000000\">%s</font>",
						"预售价格：", data.price, "  万元")));
		_addressTv.setText("所在地：        " + data.city);
		_displacementTv.setText("排量：            " + data.displacement + "L");
		_damagedTv.setText("受损部位：    " + data.damaged);
		_platedataTv.setText("上牌日期：    " + data.platedata);
		_detailTv.setText(data.masterContent);
		_phoneTv.setText("预约看车     " + data.masterName + "" + data.masterPhone);

		initTextView(_mileageTv, 5);
		initTextView(_platedataTv, 5);
		initTextView(_addressTv, 5);
		initTextView(_displacementTv, 3);
		initTextView(_damagedTv, 5);
		initTextView(_addtimeTv, 5);

	}

	/**
	 * @description 返回
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view) {
		((MLAuxiliaryActivity) _context).finish();
	}

	@OnClick(R.id.accident_tv_phone)
	public void callOnClick(View view) {
		if (_data == null)
			return;
		Builder builder = new Builder(_context);
		builder.setMessage("拨打" + _data.masterPhone);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ _data.masterPhone));
				startActivity(intent);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();

	}

	private List<String> getHeadImageUrl() {
		List<String> images = new ArrayList<String>();

		final String imageId[] = _data.image.split(",");
		for (String id : imageId) {
			String imgUrl = APIConstants.API_IMAGE + "?id=" + id;
			images.add(imgUrl);
		}

		return images;
	}

	private static final int HTTP_RESPONSE_ACCIDENT_DETIAL = 0;
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
			case HTTP_RESPONSE_ACCIDENT_DETIAL:
				ret = (MLAccidentDetailResponse) msg.obj;
				if (ret.state.equalsIgnoreCase("1") && ret.datas != null) {
					review(ret.datas);
					// _carAdapter.setData(ret.datas);
					// horizontalListViewAdapter= new
					// HorizontalListViewAdapter(_context, ret.datas.image);
					// horizon_listview.setAdapter(horizontalListViewAdapter);
					// horizontalListViewAdapter.notifyDataSetChanged();
					final String imageId[];
					imageId = ret.datas.image.split(",");
					ImageView iv1 ;
					List<View> listViews = new ArrayList<View>();
					for (int i = 0; i < imageId.length; i = i + 3) {
						if(getActivity()==null){
							return;
						}
						View view = LayoutInflater.from(getActivity()).inflate(
								R.layout.tx_detail_image_item, null);
						iv1 = (ImageView) view
								.findViewById(R.id.tx_detail_item_image1);
						ImageView iv2 = (ImageView) view
								.findViewById(R.id.tx_detail_item_image2);
						ImageView iv3 = (ImageView) view
								.findViewById(R.id.tx_detail_item_image3);
						if (i < imageId.length) {
							String imgUrl1 = APIConstants.API_IMAGE + "?id="
									+ imageId[i];
							if (!BaseApplication.IMAGE_CACHE.get(imgUrl1, iv1)) {
								iv1.setImageResource(R.drawable.sgc_photo);
							}
						}
						if (i+1 < imageId.length) {
							String imgUrl2 = APIConstants.API_IMAGE + "?id="
									+ imageId[i + 1];
							if (!BaseApplication.IMAGE_CACHE.get(imgUrl2, iv2)) {
								iv2.setImageResource(R.drawable.sgc_photo);
							}
						}

						if (i+2 < imageId.length) {
							String imgUrl3 = APIConstants.API_IMAGE + "?id="
									+ imageId[i + 2];

							if (!BaseApplication.IMAGE_CACHE.get(imgUrl3, iv3)) {
								iv3.setImageResource(R.drawable.sgc_photo);
							}
						}
						listViews.add(view);
					}
				
					TXDetailImageAdapter adapter = new TXDetailImageAdapter(getActivity(),imageId,listViews);  
					horizon_listview.setAdapter(adapter);  

				} else {
					showMessage("获取事故车详情失败!");
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// BaseApplication.IMAGE_CACHE.setOnImageCallbackListener(BaseApplication.imageCallBack);
	}

	private IEvent<Object> _event;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
