package com.qipei.found;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.model.MLMapData;
import com.txsh.R;
import com.zuomei.base.BaseActivity;

import cn.ml.base.utils.MLStrUtil;

public class MLIncidentEditAty extends BaseActivity {

	@ViewInject(R.id.edit_content)
	private EditText mEtContent;
	@ViewInject(R.id.top_tv_title)
	private TextView mTvTitle;
	
	private String tag = "";
	private MLMapData mData;
	@Override  
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qp_accident_edit);
		ViewUtils.inject(this);
		mData = (MLMapData) getIntentData();
		tag = mData.id+"";
		initEdit();

		}


	private void initEdit() {
		if(MLStrUtil.compare(tag, "1")){
			//产品名称
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});  
			mEtContent.setHint("请输入1-10位的产品名称");
			mTvTitle.setText("产品名称");
		}else if(MLStrUtil.compare(tag, "2")){
			//车辆子车型
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
		/*	mEtContent.setInputType(InputType.TYPE_CLASS_DATETIME);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));*/
			mEtContent.setHint("请输入车辆子车型");
			mTvTitle.setText("车辆子车型");
		}else if(MLStrUtil.compare(tag, "3")){
			//排量
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入排量");
			mTvTitle.setText("排量");
		}else if(MLStrUtil.compare(tag, "4")){
			//原价
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入原价");
			mTvTitle.setText("原价");
		}else if(MLStrUtil.compare(tag, "5")){
			//现价
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入现价");
			mTvTitle.setText("现价");
		}else if(MLStrUtil.compare(tag, "6")){
			//姓名
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
			mEtContent.setHint("请输入姓名");
			mTvTitle.setText("姓名");
		}else if(MLStrUtil.compare(tag, "7")){
			//联系电话
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入联系电话");
			mTvTitle.setText("联系电话");
		}
		
		mEtContent.setText(mData.value);
		
	}
	
	@OnClick(R.id.post_btn_right)
	public void saveOnClick(View view){
		String content = "";
		Intent intent = new Intent();
		
		
		if(MLStrUtil.compare(tag, "1")){
			//车型名称
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmpty(content)){
				showMessage("产品名称不能为空");
				return;
			}
			intent.putExtra("data", content);
			setResult(1, intent);
		}
		if(MLStrUtil.compare(tag, "2")){
			//车辆子车型
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmpty(content)){
				showMessage("车辆子车型不能为空");
				return;
			}
			intent.putExtra("data", content);
			setResult(2, intent);
		}
		if(MLStrUtil.compare(tag, "3")){
			//排量
			content = mEtContent.getText().toString();
			intent.putExtra("data", content);
			if(MLStrUtil.isEmpty(content)){
				showMessage("排量不能为空");
				return;
			}
			setResult(3, intent);
		}
		if(MLStrUtil.compare(tag, "4")){
			//原价
			content = mEtContent.getText().toString();
			intent.putExtra("data", content);
			if(MLStrUtil.isEmpty(content)){
				showMessage("原价不能为空");
				return;
			}
			setResult(4, intent);
		}
		if(MLStrUtil.compare(tag, "5")){
			//现价
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("现价不能为空");
				return;
			}
			
			intent.putExtra("data", content);
			setResult(5, intent);
		}
		if(MLStrUtil.compare(tag, "6")){
			//姓名
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("姓名不能为空");
				return;
			}

			intent.putExtra("data", content);
			setResult(6, intent);
		}

		if(MLStrUtil.compare(tag, "7")){
			//电话
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("联系电话不能为空");
				return;
			}

			intent.putExtra("data", content);
			setResult(7, intent);
		}
	
		finish();
	}
	@OnClick(R.id.post_btn_left)
	public void backOnClick(View view){
		finish();
	}
}
