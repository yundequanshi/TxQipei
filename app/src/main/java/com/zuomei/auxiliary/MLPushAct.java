package com.zuomei.auxiliary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;

import cn.jpush.android.api.JPushInterface;

public class MLPushAct extends Activity {

	@ViewInject(R.id.tv_name)
	TextView _titleTv;
	
	@ViewInject(R.id.tv_content)
	TextView _content;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_main);
        ViewUtils.inject(this);
        
        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
	       String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
	        
	        
	        _titleTv.setText(title+"");
	        _content.setText(content+"");
        }
    }

	@OnClick(R.id.top_back)
	public void backOnClick(View view){
		finish();
	}
}
