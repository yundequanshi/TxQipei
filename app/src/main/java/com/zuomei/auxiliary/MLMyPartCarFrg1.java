package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLHomeCatalogResponse;
import com.zuomei.services.MLHomeServices;
import com.zuomei.widget.sortlistview.CharacterParser;
import com.zuomei.widget.sortlistview.ClearEditText;
import com.zuomei.widget.sortlistview.PinyinComparator;
import com.zuomei.widget.sortlistview.SideBar;
import com.zuomei.widget.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.zuomei.widget.sortlistview.SortAdapter;

import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;

/**
 * 选择车辆品牌
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPartCarFrg1 extends BaseFragment{

	public static MLMyPartCarFrg1 INSTANCE =null;
	
	
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	/***汉字转换成拼音的类*/
	private CharacterParser characterParser;
	//private List<SortModel> SourceDateList;
	

	/**根据拼音来排列ListView里面的数据类*/
	private PinyinComparator pinyinComparator;
	public static MLMyPartCarFrg1 instance(){
		
			INSTANCE = new MLMyPartCarFrg1();
		return INSTANCE;
	}
	
	@ViewInject(R.id.home_lv_car)
	private ListView _carLv;
	

	@ViewInject(R.id.home_sidrbar)
	private SideBar _sidrbar;
	
	@ViewInject(R.id.home_tv_dialog)
	private TextView _dialogTv;
	@ViewInject(R.id.car_carview)
	private MLHomeCarView _carView;
	

	
	private Context _context;
	
	private MLHomeCatalogData _catologData ;
	private List<MLHomeCatalogData>_lists;   
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	//if(view ==null){
		view = inflater.inflate(R.layout.incident_car, container,false);
		ViewUtils.inject(this, view);
		initData(HTTP_RESPONSE_CATALOG);
	//}
	
	view.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
	view.setFocusableInTouchMode(true);

	_context = inflater.getContext();
		return view;
	}

private void initData( int position){
	 //获取分类    
	ZMRequestParams catalogParam = new ZMRequestParams();
	catalogParam.addParameter(MLConstants.PARAM_HOME_CATALOG, "1");
    ZMHttpRequestMessage message1 = new ZMHttpRequestMessage(RequestType.HOME_CATALOG, null, catalogParam, _handler, position, MLHomeServices.getInstance());
    loadDataWithMessage(_context, null, message1);
}
	
	private void initView() {
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		
		filledData();
		
		// 根据a-z进行排序源数据
		Collections.sort(_lists, pinyinComparator);
		adapter = new SortAdapter(_context, _lists);
		_carLv.setAdapter(adapter);
		
		_sidrbar.setTextView(_dialogTv);
		
		//设置右侧触摸监听
		_sidrbar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				//该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					_carLv.setSelection(position);
				}
			}
		});
		
		
//		_carLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//				_catologData = (MLHomeCatalogData) parent.getAdapter().getItem(position);
////				//	initData(_catologData.id, HTTP_RESPONSE_CATALOG_CAR);
////				setCarView(_catologData.id);
//				toActivity(getActivity(), MLConstants.HOME_BUSINESS_LIST,_catologData);
//			}
//		});

		_carLv.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					_carView.setVisibility(View.GONE);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//	_carView.setVisibility(View.GONE);
			}
		});
	}
	
	
	private void setCarView(String id){
		_carView.setData(id,_event);
		Animation hyperspaceJumpAnimation=AnimationUtils.loadAnimation(_context,R.anim.slide_in_right);
		_carView.startAnimation(hyperspaceJumpAnimation);
  
	   /* ObjectAnimator anim = ObjectAnimator.ofFloat(_carView, "alpha", 0f, 1f); 
	    anim.setDuration(1000); 
	    anim.start();  */
		_carView.setVisibility(View.VISIBLE);
		
	}
	/**
	  * @description  品牌
	  *
	  * @author marcello
	 */
	@OnItemClick(R.id.home_lv_car)
	public void brankOnItemClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		_catologData= (MLHomeCatalogData) arg0.getAdapter().getItem(arg2);
		EventBus.getDefault().postSticky(_catologData);
		((MLAuxiliaryActivity)_context).finish();

	}

	@OnClick(R.id.top_btn_left)
	public void backOnClick(View view ){
		getActivity().finish();
	}

	/**
	 * 为ListView填充数据
	 * @return
	 */
	private List<MLHomeCatalogData> filledData(){
		
		for(MLHomeCatalogData data : _lists){
			String pinyin = characterParser.getSelling(data.name);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if(sortString.matches("[A-Z]")){
				data.sortLetters = sortString.toUpperCase();
			}else{
				data.sortLetters = "#";
			}
		}
		 List<MLHomeCatalogData> dataa = _lists;
		return _lists;
	}

	 private static final int HTTP_RESPONSE_CATALOG = 1;
	 private static final int HTTP_RESPONSE_CATALOG_CAR = 2;
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
	          //      showMessage(error.errorMessage);
	                return;
	            }
	            switch (msg.what) {
	             //获取分类
	            case  HTTP_RESPONSE_CATALOG:{
	            		MLHomeCatalogResponse ret = (MLHomeCatalogResponse) msg.obj;
	            		if(ret.state.equalsIgnoreCase("1")){
	            		_lists = ret.datas;
	                	initView();
	                	}else{
	                		showMessage("获取分类失败!");
	                	}
	            	break;
	            }
	            //获取车型
	            case HTTP_RESPONSE_CATALOG_CAR:{
	            	MLHomeCatalogResponse ret = (MLHomeCatalogResponse) msg.obj;
            		if(ret.state.equalsIgnoreCase("1")){
            	   		if(ret.datas.size()==0){
        				_event.onEvent(_catologData, MLConstants.HOME_BUSINESS_LIST);
            	   		}else{
            	   		//	setCarView(ret.datas);
            	   		}
                	}else{
                		showMessage("获取车辆失败!");
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
