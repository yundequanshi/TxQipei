package com.zuomei.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.txsh.R;
import com.zuomei.base.BaseFragment;
import com.zuomei.utils.MLToolUtil;
/**
 * 
 * @description   闪屏
 * 
 * 
 * @author  marcello
 */   
public class MLSplashFragment extends BaseFragment{

	public static MLSplashFragment INSTANCE =null;
	public static MLSplashFragment instance(){
		INSTANCE = new MLSplashFragment();
		return INSTANCE;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Context  context = inflater.getContext();
		ImageView view = (ImageView) LayoutInflater.from(context).inflate(R.layout.zm_login_splash, null);
		view.setImageBitmap(MLToolUtil.readBitMap(getActivity(),R.drawable.welcome_default));
		return view;
	}
	
	
}
