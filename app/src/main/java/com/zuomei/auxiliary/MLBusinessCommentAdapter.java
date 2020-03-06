package com.zuomei.auxiliary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.AdapterBase;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLCommentData;
import com.zuomei.utils.MLStringUtils;

public class MLBusinessCommentAdapter extends AdapterBase<MLCommentData>{

	private Context _context;
	
	public MLBusinessCommentAdapter(Context _context) {
		super();
		this._context = _context;
	}

	@Override
	protected View getExView(int position, View view, ViewGroup parent) {
		
		MLMyBillItmView item = null;
		if(view ==null){
			item = new MLMyBillItmView(_context);
			view = item;
		}else{
			item = (MLMyBillItmView) view;
		}
		MLCommentData data = (MLCommentData) getItem(position);
		item.setData(data);
		return item;
	}
	
	

	class MLMyBillItmView extends BaseLayout{

		public MLMyBillItmView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public MLMyBillItmView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public MLMyBillItmView(Context context) {
			super(context);
			init();
		}
		@ViewInject(R.id.comment_name)
		private TextView _nameTv;
		
		@ViewInject(R.id.comment_time)
		private TextView _timeTv;
		
		@ViewInject(R.id.comment_content)
		private TextView _contentTv;
		
		@ViewInject(R.id.comment_iv)
		private ImageView _headIv;
		
		@ViewInject(R.id.home_business_call)
		private ImageView _commentIv;
		
		private void init(){
			View view = LayoutInflater.from(_context).inflate(R.layout.home_comment_item, null);
			addView(view);
			ViewUtils.inject(this, view);
		}
		
		public void setData(MLCommentData data){
			String time =MLStringUtils.time_second(data.mtime);
			_nameTv.setText(data.user.depotName);
			_timeTv.setText(time);
			_contentTv.setText("评论 : "+data.content);
			
			if(data.assessmentLevels.equalsIgnoreCase("-1")){
				_commentIv.setImageResource(R.drawable.comment_bad);
			}else{
				_commentIv.setImageResource(R.drawable.comment_good);
			}
			
			
			
			String iconUrl = APIConstants.API_IMAGE+"?id="+data.user.userPhoto;
			BaseApplication.IMAGE_CACHE.get(iconUrl, _headIv);
		}
	}
}

