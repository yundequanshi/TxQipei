package com.txsh.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.txsh.model.TXSupplyRes;
import com.txsh.utils.GridViewInScrollView;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.utils.MLStrUtil;
import cn.ml.base.utils.MLToolUtil;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * Created by Marcello on 2015/6/3.
 */
public class TXSupplyAdapter extends MLAdapterBase<TXSupplyRes.TXSupplyData> {

	public Handler _handler;
	public Context _context;

	public TXSupplyAdapter(Context context, int viewXml, Handler handler) {
		super(context, viewXml);
		_handler = handler;
		_context = context;
	}

	/*
	 * @ViewInject(R.id.carlist_tv_label) public TextView mcarmol;
	 */


	@ViewInject(R.id.message_tv_name)
	public TextView _nameTv;

	@ViewInject(R.id.message__tv_content)
	public TextView _contentTv;

	@ViewInject(R.id.supply_phone)
	public TextView _phoneTv;
	@ViewInject(R.id.message_tv_type)
	public TextView _typeTv;


	@ViewInject(R.id.interact_tv_time)
	public TextView _timeTv;

	@ViewInject(R.id.message_gv_image)
	public GridViewInScrollView _imageGridView;

	@ViewInject(R.id.message_iv_icon)
	public RoundedImageView _iconIv;


	@Override
	protected void setItemData(View view, final TXSupplyRes.TXSupplyData data,
			final int position) {
		ViewUtils.inject(this, view);
		if (data == null)
			return;


		// 头像
		String iconUrl = "";
		iconUrl = APIConstants.API_IMAGE + "?id=" + data.imageId;
		_nameTv.setText(data.name);




		if (data.content == "") {
			_contentTv.setVisibility(View.GONE);
		} else {
			_contentTv.setVisibility(View.VISIBLE);
		}
		_contentTv.setText(data.content);
		// String time = MLStringUtils.friendly_time(data.createTime);
		_timeTv.setText(data.createTime);


		_iconIv.setTag(iconUrl);
		if (!BaseApplication.IMAGE_CACHE.get(iconUrl, _iconIv)) {
			_iconIv.setImageResource(R.drawable.default_message_header);
		}


		if(MLStrUtil.compare(data.type,"求购")){
			_typeTv.setText("求购");
			_typeTv.setBackgroundColor(Color.parseColor("#d90808"));
		}else{
			_typeTv.setText("供应");
			_typeTv.setBackgroundColor(Color.parseColor("#4dca02"));
		}


		_phoneTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MLToolUtil.call((Activity) _context,data.phone);
			}
		});

		ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (data.images != null) {
			JSONArray jsonArray;
			try {
				jsonArray = new JSONArray(data.images);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = new JSONObject(jsonArray.get(i)
							.toString());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("path",
							APIConstants.API_IMAGE_SHOW
									+ jsonObject.getString("path"));
					list.add(map);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (list.size() == 0) {
			_imageGridView.setVisibility(View.GONE);
		} else {
			_imageGridView.setVisibility(View.VISIBLE);
		}
		ShowAdapter showAdapter = new ShowAdapter(list);
		_imageGridView.setAdapter(showAdapter);

		_iconIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Message m = new Message();
				m.arg1 = mPosition;
				m.what = 3;
				m.obj = data;
				_handler.sendMessage(m);

			}
		});


		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message m = new Message();
				m.arg1 = mPosition;
				m.what = 4;
				m.obj = data;
				_handler.sendMessage(m);
			}
		});

		// _imageIv.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Message m = new Message();
		// m.arg1 = mPosition;
		// m.what = 2;
		// m.obj = mInfo.images;
		// ;
		// _handler.sendMessage(m);
		// }
		// });


		
	}

	private class ShowAdapter extends BaseAdapter {
		private List<Map<String, Object>> mList;

		public ShowAdapter(List<Map<String, Object>> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder mHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(_context).inflate(
						R.layout.tx_interact_image_gridview_imageview, null);
				mHolder = new ViewHolder();
				// mHolder.ll_info = (LinearLayout)
				// convertView.findViewById(R.id.ll_info);
				mHolder.image = (ImageView) convertView
						.findViewById(R.id.tx_interact_imageview);
				// mHolder.imageRleative = (ImageView) convertView
				// .findViewById(R.id.img_relative);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			// if(mlist.get(position).getPic().isEmpty())
			// {
			// mHolder.imageRleative.setBackgroundResource(R.drawable.pic_2x);
			// }else
			// {
			// bitmapUtils.display(mHolder.image,
			// mList.get(position).get("path")
			// .toString());

			mHolder.image.setTag(mList.get(position).get("path").toString());
			if (!BaseApplication.IMAGE_CACHE.get(mList.get(position)
					.get("path").toString(), mHolder.image)) {
				mHolder.image.setImageDrawable(null);
			}

			mHolder.image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					List<String> images = new ArrayList<String>();
					for (int i = 0; i < mList.size(); i++) {
						images.add(mList.get(i).get("path").toString());
					}
					Message m = new Message();
					m.arg1 = position;
					m.what = 2;
					m.obj = images;
					_handler.sendMessage(m);

				}
			});

			
			
			return convertView;
		}
	}

	class ViewHolder {
		ImageView image;

	}


}