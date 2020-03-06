package com.txsh;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.zuomei.auxiliary.MLMyPartCarFrg;
import com.zuomei.base.BaseActivity;

public class TXPartCarActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_txpart_car);
		 FragmentManager fragmentManager  = getSupportFragmentManager();
		 FragmentTransaction transaction = fragmentManager.beginTransaction();
		MLMyPartCarFrg mLMyPartCarFrg = new MLMyPartCarFrg();
		transaction.add(R.id.active_txpart_car_linear, mLMyPartCarFrg);

		transaction.commit();

	}

}
