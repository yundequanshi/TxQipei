package com.zuomei.home;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.model.MLMessageCommentData;

/**
 * 回复
 * @author Marcello
 *
 */
public class MLReplyItemView extends RelativeLayout{

	private Context _context;
	public MLReplyItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
		init();
	}

	public MLReplyItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init();
	}

	public MLReplyItemView(Context context) {
		super(context);
		_context = context;
		init();
	}
	
	/*@ViewInject(R.id.message_tv_reply)
	private TextView _replyTv;*/
	
	@ViewInject(R.id.message_tv_person)
	private TextView _personTv;
	
	private void init(){
		View view = LayoutInflater.from(_context).inflate(R.layout.message_reply_item, null);
		LayoutParams param = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		addView(view,param);
		ViewUtils.inject(this, view);
	}
	
	public void setData(MLMessageCommentData data) {
		//_replyTv.setText(data.contentDetail);
	    //_contentTV.setText(Html.fromHtml("原 因 : <font color=\"#ff0000\">账户已被冻结，请联系客服人员。电话:0531-8987625</font>"));   
		String text = String.format("<font color=\"#2394E4\">%s:</font><font color=\"#888888\">%s</font>", data.userName,data.content);
		_personTv.setText(Html.fromHtml(text));
	}
}
