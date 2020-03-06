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

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.model.MLMyStockData;
import com.zuomei.model.MLMyStockDetail;
import com.zuomei.utils.MLStringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

/**
 * 添加进货信息
 * 
 * @author Marcello
 * 
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLMyStockDetailFrg extends BaseFragment {

	public static MLMyStockDetailFrg INSTANCE = null;

	private static MLMyStockData _data;

	public static MLMyStockDetailFrg instance(Object obj) {
		_data = (MLMyStockData) obj;
		// if(INSTANCE==null){
		INSTANCE = new MLMyStockDetailFrg();
		// }
		return INSTANCE;
	}

	@ViewInject(R.id.add_et_name)
	private EditText _nameEt;
	@ViewInject(R.id.add_et_phone)
	private EditText _phoneEt;

	@ViewInject(R.id.stock_btn_add)
	private Button _okBtn;

	@ViewInject(R.id.add_btn)
	private Button _adddBtn;
	@ViewInject(R.id.add_lv_record)
	private ListView _recordLv;
	private MLMyStockAddAdapter _adapter;
	private List<MLMyStockDetail> _lists;

	@ViewInject(R.id.stock_rl_root)
	private RelativeLayout _root;
	@ViewInject(R.id.add_et_time)
	private Button add_et_time;

	private Context _context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_stock_detail, null);
		ViewUtils.inject(this, view);

		_context = inflater.getContext();

		initView();
		return view;
	}

	

	private void initView() {
		_lists = new ArrayList<MLMyStockDetail>();
		_adapter = new MLMyStockAddAdapter(_context);
		_recordLv.setAdapter(_adapter);
		_adapter.setData(_data.stockDetail);
		_okBtn.setVisibility(View.INVISIBLE);
		_adddBtn.setVisibility(View.GONE);
		_nameEt.setEnabled(true);
		_phoneEt.setEnabled(true);
		_nameEt.setText(_data.companyName);
		_phoneEt.setText(_data.companyPhone);
		
		add_et_time.setText(MLStringUtils.time_year(_data.date.getTime()).toString());
	}

	/**
	 * @description 返回
	 * 
	 * @author marcello
	 */
	@OnClick(R.id.home_top_back)
	public void backOnClick(View view) {
		getActivity().onBackPressed();
	}

	private IEvent<Object> _event;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_event = (IEvent<Object>) activity;
	}
}
