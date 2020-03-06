package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.home.MLPartPricePop;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLAccidentQuotesData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyPartBusinessMagData;
import com.zuomei.model.MLMyPartDetailData;
import com.zuomei.model.MLMyPartDetailResponse;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLPartServices;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 报价管理- 商家报价-零件列表
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyPartOfferPrice extends BaseFragment{

	public static MLMyPartOfferPrice INSTANCE =null;
	
	static MLMyPartBusinessMagData data ;
	public static MLMyPartOfferPrice instance(Object obj){
			INSTANCE = new MLMyPartOfferPrice();
			data =  (MLMyPartBusinessMagData) obj;
		return INSTANCE;
	}
	
	@ViewInject(R.id.root)
	private RelativeLayout root;
	
	@ViewInject(R.id.mListView)
	private ListView mList;
	
	@ViewInject(R.id.et_content)
	private EditText mRemarkEt;
	
	@ViewInject(R.id.et_dt_content)
	private EditText mDtRemarkEt;
	@ViewInject(R.id.tv_label)
	private TextView mlabelTv;
	
	@ViewInject(R.id.tv_clabel)
	private TextView mclabelTv;
	
	@ViewInject(R.id.btn_list)
	private Button mSubBtn;
	
		//汽修厂名称
		@ViewInject(R.id.et_name)
		private EditText mEtDepotName;
		//车型
		@ViewInject(R.id.et_cartype)
		private TextView mCarTV;
		//子车型
		@ViewInject(R.id.et_carchild)
		private EditText mChildEt;
		//车架号
		@ViewInject(R.id.et_chejia)
		private EditText mChejiaEt;
		//年份
		@ViewInject(R.id.et_year)
		private EditText mYearEt;
	
	private MLMyPartOfferPriceAdapter mAdapter;
	private Context _context;
	private 	MLLogin user ;
	
	public List<MLAccidentQuotesData> _datas;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_partoffer_price, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();
		 user = ((BaseApplication)getActivity().getApplication()).get_user();
		// _subdatas= new ArrayList<MLAccidentQuotesData>();
		
		
		 
		 
		mAdapter = new MLMyPartOfferPriceAdapter(_context);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				
				MLPartPricePop menuWindow = new MLPartPricePop(getActivity(),_datas.get(arg2), new IEvent<MLAccidentQuotesData>() {
						@Override
						public void onEvent(Object source, MLAccidentQuotesData eventArg) {
							_datas.set(arg2, eventArg);
							mAdapter.setData(_datas);
							MLToolUtil.setListViewHeightBasedOnChildren(mList);
						}
					});  
				        menuWindow.showAtLocation(root, Gravity.CENTER, 0, 0); 
				
			}
		});
		
		 if(!data.state.contains("报价")){
			 mSubBtn.setVisibility(View.GONE);
			 mList.setOnItemClickListener(null);
			 mRemarkEt.setEnabled(false);
		 }
		initData();
		return view;
	}
	
	
	private void initData(){
		ZMRequestParams params = new ZMRequestParams();
		  JSONObject jo = new JSONObject();
		  try {
			jo.put("id", data.id);
		} catch (JSONException e) {
			return;
		}
			params.addParameter("data",jo.toString());
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BUS_PART_DETAIL, null, params, _handler,HTTP_RESPONSE_LIST, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	@OnClick(R.id.btn_list)
	public void submitOnClick(View view){
		
		ZMRequestParams params = new ZMRequestParams();
	
		MLMyPartDetailData ret = new MLMyPartDetailData();
		ret.id = data.id;
		ret.remark = mRemarkEt.getText().toString();
		ret.accidentQuotes = mAdapter.getList();
		Gson gson = new Gson();
		params.addParameter("data",gson.toJson(ret));
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.BUS_PART_OFFER, null, params, _handler,HTTP_RESPONSE_OFFER, MLPartServices.getInstance());
	    loadData(_context, message2);
	}
	
	
	private static final int HTTP_RESPONSE_LIST= 0;
	private static final int HTTP_RESPONSE_LIST_PAGE= 1;
	private static final int HTTP_RESPONSE_OFFER= 2;
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
            case HTTP_RESPONSE_LIST:{
            	MLMyPartDetailResponse ret = (MLMyPartDetailResponse) msg.obj;
    		if(ret.state.equalsIgnoreCase("1")){
    			_datas = ret.datas.accidentQuotes;
    			mAdapter.setData(ret.datas.accidentQuotes);
    			//汽修厂备注
    			if(MLToolUtil.isNull(ret.datas.remark)){
    				mDtRemarkEt.setVisibility(View.GONE);
    				mlabelTv.setVisibility(View.GONE);
    			}else{
    				mDtRemarkEt.setText(ret.datas.remark);
    			}
    			
    			//商家备注
    		/*	if(MLToolUtil.isNull(ret.datas.cremark)){
    				mRemarkEt.setVisibility(View.GONE);
    				mclabelTv.setVisibility(View.GONE);
    			}else{
    				mRemarkEt.setText(ret.datas.cremark);
    			}*/
    			mRemarkEt.setText(ret.datas.cremark);
    			mEtDepotName.setText(ret.datas.name);
    			mCarTV.setText(ret.datas.type);
    			mChildEt.setText(ret.datas.childType);
    			mChejiaEt.setText(ret.datas.carNum);
    			mYearEt.setText(ret.datas.particularYear);
    			
    			
    			
    			MLToolUtil.setListViewHeightBasedOnChildren(mList);
        	}else{
        		showMessage("获取报价列表失败!");
        	}
            	break;
            }
            
            case HTTP_RESPONSE_OFFER:{
            	MLRegister ret = (MLRegister) msg.obj;
        		if(ret.state.equalsIgnoreCase("1")){
        			showMessageSuccess("提交成功!");
        			((MLAuxiliaryActivity)_context).finish();
            	}else{
            		showMessage("提交失败!");
            	}
            	break;
            }
            default:
                break;
        }
    }
};
	
	/**
	  * @description   返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		((MLAuxiliaryActivity)_context).finish();
	}
	
	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}

}
