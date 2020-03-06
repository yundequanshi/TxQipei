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

public class MLAccidentEditAty extends BaseActivity {

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
			//昵称
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});  
			mEtContent.setHint("请输入1-10位的车型名称");
			mTvTitle.setText("车型名称");
		}else if(MLStrUtil.compare(tag, "2")){
			//行驶里程
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_DATETIME);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入行驶里程数");
			mTvTitle.setText("行驶里程");
		}else if(MLStrUtil.compare(tag, "3")){
			//受损部位
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
			mEtContent.setHint("请输入受损部位");
			mTvTitle.setText("受损部位");
		}else if(MLStrUtil.compare(tag, "4")){
			//买时裸车价
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入买时裸车价");
			mTvTitle.setText("买时裸车价");
		}else if(MLStrUtil.compare(tag, "5")){
			//排量
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入排量");
			mTvTitle.setText("排量");
		}else if(MLStrUtil.compare(tag, "6")){
			//预售价格
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
			mEtContent.setInputType(InputType.TYPE_CLASS_NUMBER);
			mEtContent.setKeyListener(new DigitsKeyListener(false, true));
			mEtContent.setHint("请输入预售价格");
			mTvTitle.setText("预售价格");
		}else if(MLStrUtil.compare(tag, "7")){
			//姓名
			mEtContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
			mEtContent.setHint("请输入姓名");
			mTvTitle.setText("姓名");
		}else if(MLStrUtil.compare(tag, "8")){
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
				showMessage("车型名称不能为空");
				return;
			}
			intent.putExtra("data", content);
			setResult(1, intent);
		}
		if(MLStrUtil.compare(tag, "2")){
			//行驶里程
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmpty(content)){
				showMessage("行驶里程不能为空");
				return;
			}
			intent.putExtra("data", content);
			setResult(2, intent);
		}
		if(MLStrUtil.compare(tag, "3")){
			//受损部位
			content = mEtContent.getText().toString();
			intent.putExtra("data", content);
			if(MLStrUtil.isEmpty(content)){
				showMessage("受损部位不能为空");
				return;
			}
			setResult(3, intent);
		}
		if(MLStrUtil.compare(tag, "4")){
			//买时裸车价
			content = mEtContent.getText().toString();
			intent.putExtra("data", content);
			if(MLStrUtil.isEmpty(content)){
				showMessage("买时裸车价不能为空");
				return;
			}
			setResult(4, intent);
		}
		if(MLStrUtil.compare(tag, "5")){
			//排量
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("排量不能为空");
				return;
			}
			
			intent.putExtra("data", content);
			setResult(5, intent);
		}
		if(MLStrUtil.compare(tag, "6")){
			//预售价格
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("预售价格不能为空");
				return;
			}

			intent.putExtra("data", content);
			setResult(6, intent);
		}
		if(MLStrUtil.compare(tag, "7")){
			//姓名
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("姓名不能为空");
				return;
			}

			intent.putExtra("data", content);
			setResult(7, intent);
		}
		if(MLStrUtil.compare(tag, "8")){
			//电话
			content = mEtContent.getText().toString();
			if(MLStrUtil.isEmail(content)){
				showMessage("联系电话不能为空");
				return;
			}

			intent.putExtra("data", content);
			setResult(8, intent);
		}
	
		finish();
	}
	@OnClick(R.id.post_btn_left)
	public void backOnClick(View view){
		finish();
	}
}
