package com.zuomei.home;

/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.txsh.R;
import com.txsh.market.EventBusModel;
import org.greenrobot.eventbus.EventBus;

// TODO: Auto-generated Javadoc
/**
 * 适配器 网络URL的图片.
 */
public class MLAccidentPhotoAdapter1 extends BaseAdapter {

	/** The m context. */
	private Context mContext;

	/** The m image paths. */
	private List<String> mImagePaths = null;

	/** The m width. */
	private int mWidth;

	/** The m height. */
	private int mHeight;

	// 图片下载器
	private AbImageDownloader mAbImageDownloader = null;

	/**
	 * Instantiates a new ab image show adapter.
	 * 
	 * @param context
	 *            the context
	 * @param imagePaths
	 *            the image paths
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public MLAccidentPhotoAdapter1(Context context, List<String> imagePaths,
			int width, int height) {
		mContext = context;
		this.mImagePaths = imagePaths;
		this.mWidth = width;
		this.mHeight = height;
		// 图片下载器
		mAbImageDownloader = new AbImageDownloader(mContext);
		mAbImageDownloader.setWidth(AbViewUtil.scale(context, this.mWidth));
		mAbImageDownloader.setHeight(AbViewUtil.scale(context, this.mHeight));
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		mAbImageDownloader.setNoImage(R.drawable.image_no);
	}

	/**
	 * 描述：获取数量.
	 * 
	 * @return the count
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return mImagePaths.size();
	}

	/**
	 * 描述：获取索引位置的路径.
	 * 
	 * @param position
	 *            the position
	 * @return the item
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return mImagePaths.get(position);
	}

	/**
	 * 描述：获取位置.
	 * 
	 * @param position
	 *            the position
	 * @return the item id
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 描述：显示View.
	 * 
	 * @param position
	 *            the position
	 * @param convertView
	 *            the convert view
	 * @param parent
	 *            the parent
	 * @return the view
	 * @see android.widget.Adapter#getView(int, View,
	 *      ViewGroup)
	 */
	@SuppressWarnings({ "unused", "deprecation" })
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			// LinearLayout mLinearLayout = new LinearLayout(mContext);
			// LinearLayout mLinearLayout1 = new LinearLayout(mContext);
			// RelativeLayout mRelativeLayout = new RelativeLayout(mContext);
			// ImageView mImageView1 = new ImageView(mContext);
			// mImageView1.setScaleType(ScaleType.FIT_CENTER);
			// ImageView mImageView2 = new ImageView(mContext);
			// mImageView2.setScaleType(ScaleType.FIT_CENTER);
			// ImageView mImageView3 = new ImageView(mContext);
			//
			// mImageView3.setScaleType(ScaleType.FIT_XY);
			//
			// holder.mImageView1 = mImageView1;
			// holder.mImageView2 = mImageView2;
			// holder.mImageView3 = mImageView3;
			// holder.mImageView1.setPadding(10, 5, 0, 0);
			// holder.mImageView2.setPadding(10, 5, 0, 0);
			// holder.mImageView3.setPadding(0, 5, 5, 0);
			// LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
			// ViewGroup.LayoutParams.MATCH_PARENT,
			// ViewGroup.LayoutParams.MATCH_PARENT);
			// lp1.gravity = Gravity.CENTER_HORIZONTAL |
			// Gravity.CENTER_VERTICAL;
			//
			// // LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(
			// // ViewGroup.LayoutParams.WRAP_CONTENT,
			// // ViewGroup.LayoutParams.WRAP_CONTENT);
			// // lp3.gravity = Gravity.RIGHT;
			// //
			// RelativeLayout.LayoutParams lp2 = new
			// RelativeLayout.LayoutParams(
			// mWidth, mHeight);
			// lp2.addRule(RelativeLayout.CENTER_HORIZONTAL,
			// RelativeLayout.TRUE);
			// lp2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
			//
			// RelativeLayout.LayoutParams lp3 = new
			// RelativeLayout.LayoutParams(
			// ViewGroup.LayoutParams.MATCH_PARENT,
			// ViewGroup.LayoutParams.WRAP_CONTENT);
			// lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
			// RelativeLayout.TRUE);
			// lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP,
			// RelativeLayout.TRUE);
			//
			//
			// mRelativeLayout.addView(mImageView1, lp2);
			// mRelativeLayout.addView(mImageView2, lp2);
			// mRelativeLayout.addView(mImageView3,lp3);
			//
			// mLinearLayout.addView(mRelativeLayout, lp1);
			//
			//
			// convertView = mLinearLayout;
			
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.photoadapter, null);
			holder.mImageView1 = (ImageView) convertView.findViewById(R.id.iamgeview1);
			holder.mImageView3 = (ImageView) convertView.findViewById(R.id.iamgeview2);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.mImageView1.setImageBitmap(null);
//		holder.mImageView2.setBackgroundDrawable(null);
		
		String imagePath = mImagePaths.get(position);

		if (!AbStrUtil.isEmpty(imagePath)) {
			// 从缓存中获取图片，很重要否则会导致页面闪动
			Bitmap bitmap = null;
			// Bitmap bitmap = AbImageCache.getBitmapFromCache(imagePath);
			// 缓存中没有则从网络和SD卡获取
			if (bitmap == null) {
				holder.mImageView1.setImageResource(R.drawable.image_loading);
				if (imagePath.indexOf("http://") != -1) {
					// 图片的下载
					mAbImageDownloader.setType(AbImageUtil.ORIGINALIMG);
					mAbImageDownloader.display(holder.mImageView1, imagePath);

				} else if (imagePath.indexOf("/") == -1) {
					// 索引图片
					try {
						int res = Integer.parseInt(imagePath);
						holder.mImageView1.setImageDrawable(mContext
								.getResources().getDrawable(res));
					} catch (Exception e) {
						holder.mImageView1
								.setImageResource(R.drawable.image_error);
					}
				} else {
					Bitmap mBitmap = AbFileUtil.getBitmapFromSD(new File(
							imagePath), AbImageUtil.SCALEIMG, mWidth, mHeight);
					if (mBitmap != null) {
						holder.mImageView1.setImageBitmap(mBitmap);
					} else {
						// 无图片时显示
						holder.mImageView1
								.setImageResource(R.drawable.image_no);
					}
				}
			} else {
				// 直接显示
				holder.mImageView1.setImageBitmap(bitmap);
			}
		} else {
			// 无图片时显示
			holder.mImageView1.setImageResource(R.drawable.image_no);
		}
		holder.mImageView1.setAdjustViewBounds(true);
		
		if (mImagePaths.get(position).toString().equals("2130837615")) {
			holder.mImageView3.setVisibility(View.GONE);
		}
		else{
			holder.mImageView3.setVisibility(View.VISIBLE);
		}
		holder.mImageView3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Toast.makeText(mContext, "aaa", 1500).show();\
				EventBus.getDefault().post(new EventBusModel(String.valueOf(position)));  
				mImagePaths.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}

	/**
	 * 增加并改变视图.
	 * 
	 * @param position
	 *            the position
	 * @param imagePaths
	 *            the image paths
	 */
	public void addItem(int position, String imagePaths) {
		mImagePaths.add(position, imagePaths);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 * 
	 * @param imagePaths
	 *            the image paths
	 */
	public void addItems(List<String> imagePaths) {
		mImagePaths.addAll(imagePaths);
		notifyDataSetChanged();
	}

	/**
	 * 增加多条并改变视图.
	 */
	public void clearItems() {
		mImagePaths.clear();
		notifyDataSetChanged();
	}

	/**
	 * View元素.
	 */
	public static class ViewHolder {

		/** The m image view1. */
		public ImageView mImageView1;

		/** The m image view2. */
//		public ImageView mImageView2;

		public ImageView mImageView3;
	}

	public List<String> getImagePaths() {
		return mImagePaths;
	}

}
