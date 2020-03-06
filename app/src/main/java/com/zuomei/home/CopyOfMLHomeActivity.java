package com.zuomei.home;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.ab.view.sliding.AbBottomTabView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.home.MLHomeFrg;
import com.txsh.R;
import com.zuomei.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;

public class CopyOfMLHomeActivity extends BaseActivity implements IEvent<Object> {

	@ViewInject(R.id.home_tab)
	private AbBottomTabView _bottomTab;
	
	private List<Drawable> tabDrawables = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_main);
		ViewUtils.inject(this);
		
		initTab();
	}

	private void initTab() {
		_bottomTab.getViewPager().setOffscreenPageLimit(5);
		
		Fragment page1 = new MLHomeFrg();
		Fragment page2= MLViolationFrg.instance();
		Fragment page3= MLMessageFrg.instance();
		Fragment page4= MLAccidentFrg.instance();
		Fragment page5= MLMyMainDFrg.instance();
		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page1);
		mFragments.add(page2);
		mFragments.add(page3);
		mFragments.add(page4);
		mFragments.add(page5);
		
		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("首页");
		tabTexts.add("违章");
		tabTexts.add("互动");
		tabTexts.add("事故车");
		tabTexts.add("我的");
		
		
		//设置样式
		_bottomTab.setTabTextColor(Color.parseColor("#bbbbbb"));
		_bottomTab.setTabSelectColor(Color.parseColor("#00aff0"));
		
//		_bottomTab.setTabBackgroundResource(R.drawable.tab_bg2);
		_bottomTab.setTabLayoutBackgroundResource(R.drawable.tablayout_bg2);
	
		//注意图片的顺序
		tabDrawables = new ArrayList<Drawable>();
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_home_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_home_f));
		/*tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_violation_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_violation_f));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_message_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_message_f));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_accident_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_accident_f));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_my_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.bottom_my_f));*/
		
		_bottomTab.getViewPager().setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
			
		});
	//	_bottomTab.addItemViews(tabTexts,mFragments,tabDrawables);
		_bottomTab.setTabPadding(2,8, 2, 2);
	}
	@Override
	public void onEvent(Object source, Object eventArg) {
		//fillContent(source,(Integer)eventArg);
	}
}
