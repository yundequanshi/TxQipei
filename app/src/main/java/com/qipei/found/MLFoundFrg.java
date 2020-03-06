package com.qipei.found;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.adapter.MLFoundAdapter;
import com.qipei.model.MLFoundItem;
import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ml.base.utils.MLStrUtil;

/**
 * 发现
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLFoundFrg extends BaseFragment{


	private Context _context;
	@ViewInject(R.id.found_gv)
	private GridView mGrid;

	private MLFoundAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_found, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();


		mAdapter = new MLFoundAdapter(_context,R.layout.item_found);
		mGrid.setAdapter(mAdapter);
		setData();


		mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


				if(MLStrUtil.compare(mItems.get(position).content,"刮刮乐")){
					toActivity(_context, MLConstants.MY_LOTTERY, null);
				}
				/*if(MLStrUtil.compare(mItems.get(position).content,"上传发货单")){
					Intent intent = new Intent(getActivity(),MLInvoiceAty.class);
					startActivity(intent);
				}*/
				if(MLStrUtil.compare(mItems.get(position).content,"事故件")){
					Intent intent = new Intent(getActivity(),MLIncidentAty.class);
					startActivity(intent);
				}
				if(MLStrUtil.compare(mItems.get(position).content,"事故车")){
					Intent intent = new Intent(getActivity(),MLAccidentAty.class);
					startActivity(intent);
				}
				if(MLStrUtil.compare(mItems.get(position).content,"用户量")){
					toActivity(_context, MLConstants.MY_USER, null);
				}

			}
		});
		return view;
	}

	private List<MLFoundItem> mItems;
	private void setData(){
		MLFoundItem item1 = new MLFoundItem(R.drawable.found_item_tg,"活动与团购");
		MLFoundItem item2 = new MLFoundItem(R.drawable.found_item_ggl,"刮刮乐");
		MLFoundItem item3 = new MLFoundItem(R.drawable.found_item_wz,"违章查询");
		MLFoundItem item4 = new MLFoundItem(R.drawable.found_item_pp,"品牌基地");
		MLFoundItem item5 = new MLFoundItem(R.drawable.found_item_fhd,"上传发货单");
		MLFoundItem item6 = new MLFoundItem(R.drawable.found_item_sgj,"事故件");
		MLFoundItem item7 = new MLFoundItem(R.drawable.found_item_yhl,"用户量");
		MLFoundItem item8 = new MLFoundItem(R.drawable.found_item_sgc,"事故车");
		MLFoundItem item9 = new MLFoundItem(0,"");

		mItems = new ArrayList<MLFoundItem>();
		Collections.addAll(mItems,item1,item2,item3,item4,item5,item6,item7,item8,item9);
		mAdapter.setData(mItems);

	}
}
