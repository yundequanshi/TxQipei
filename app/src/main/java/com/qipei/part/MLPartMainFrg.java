package com.qipei.part;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.adapter.FragmentTabAdapter;
import com.txsh.R;
import com.zuomei.auxiliary.MLHomeCarFrg;
import com.zuomei.base.BaseFragment;
import com.zuomei.model.MLHomeCatalogData;

import java.util.ArrayList;
import java.util.List;

/**
 * 配件
 * @author Marcello
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLPartMainFrg extends BaseFragment{


	private Context _context;

	@ViewInject(R.id.part_rg)
	private RadioGroup mGroup;

	private List<Fragment> fragments = new ArrayList<Fragment>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_part, null);
		ViewUtils.inject(this,view);
		_context = inflater.getContext();


		MLHomeCatalogData d = new MLHomeCatalogData();
		d.id = "1";
		MLHomeCarFrg mFragCar = MLHomeCarFrg.instance(d);
		MLPartListFrag mFragList = new MLPartListFrag();
		fragments.add(mFragCar);
		fragments.add(mFragList);
		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(getActivity(), fragments,R.id.part_fl_content, mGroup);
		tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
			@Override
			public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
//					System.out.println("Extra---- " + index+ " checked!!! ");
			}
		});

		//fillContent(R.id.part_rb_tab1);
		return view;
	}
}
