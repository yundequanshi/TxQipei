package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.model.MLMyRepairData;
import com.zuomei.model.MLMyRepairDetail;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 汽修详情
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyRepairDetailFrg extends BaseFragment{

	public static MLMyRepairDetailFrg INSTANCE =null;

   public static MLMyRepairData _data;
	public static MLMyRepairDetailFrg instance(Object obj){
		_data = (MLMyRepairData) obj;
	//	if(INSTANCE==null){
			INSTANCE = new MLMyRepairDetailFrg();
	//	}
		return INSTANCE;
	}

	@ViewInject(R.id.add_et_number)
	private EditText _numberEt;
	
	@ViewInject(R.id.add_et_phone)
	private EditText _phoneEt;
	
	@ViewInject(R.id.add_et_price)
	private EditText _priceEt;
	
	@ViewInject(R.id.add_lv_record)
	private ListView _recordLv;
	
	@ViewInject(R.id.stock_btn_add)
	private Button _addBtn;
	
	@ViewInject(R.id.add_btn)
	private Button _okBtn;
	
	@ViewInject(R.id.top_title)
	private TextView _titleTv;
	
	@ViewInject(R.id.stock_rl_root)
	private RelativeLayout _root;
	private MLMyRepairAddPop _addPop;
	private MLMyRecordAdapter _adapter;
	private Context _context;
	private List<MLMyRepairDetail> _lists;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_repair_add, null);
		ViewUtils.inject(this,view);
		
		_context = inflater.getContext();
		
		initView();
		return view;
	}
	
	
	private void initView() {
		
		_addBtn.setVisibility(View.GONE);
		_okBtn.setVisibility(View.GONE);
		_titleTv.setText("汽修详情");
		
	
		
		_lists = new ArrayList<MLMyRepairDetail>();
		_adapter = new MLMyRecordAdapter(_context);
		_recordLv.setAdapter(_adapter);
		_adapter.setData(_data.breakdownDetail);
	}
	/**
	  * @description  返回
	  *
	  * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view){
		getActivity().onBackPressed();
	}
	
	
	
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		_numberEt.setText(_data.carNum);
		_phoneEt.setText(_data.phone);
		_priceEt.setText(_data.cost);
		
		_numberEt.setEnabled(false);
		_phoneEt.setEnabled(false);
		_priceEt.setEnabled(false);
	}

	private IEvent<Object> _event;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
