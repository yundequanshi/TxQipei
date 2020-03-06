package com.zuomei.utils;

import android.app.Activity;

import com.txsh.R;


/**
 * 动画效果类
 * 
 * @author Administrator
 * 
 */
public class ActivityAnimator {
	/**
	 * 水平伸缩效果
	 * 
	 * @param a
	 */
	public void flipHorizontalAnimation(Activity a) {
		a.overridePendingTransition(R.anim.flip_horizontal_in,
				R.anim.flip_horizontal_out);
	}

	public void flipHorizontalBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.flip_horizontal_in,
				R.anim.flip_horizontal_out);
	}

	/**
	 * 垂直伸缩
	 * 
	 * @param a
	 */
	public void flipVerticalAnimation(Activity a) {
		a.overridePendingTransition(R.anim.flip_vertical_in,
				R.anim.flip_vertical_out);
	}

	public void flipVerticalBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.flip_vertical_in,
				R.anim.flip_vertical_out);
	}

	/**
	 * 渐变动画
	 * 
	 * @param a
	 */
	public void fadeAnimation(Activity a) {
		a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	public void fadeBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	/**
	 * 左上角退出动画
	 * 
	 * @param a
	 */
	public void disappearTopLeftAnimation(Activity a) {
		a.overridePendingTransition(R.anim.disappear_top_left_in,
				R.anim.disappear_top_left_out);
	}

	public void disappearTopLeftBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.appear_top_left_in,
				R.anim.appear_top_left_out);
	}

	/**
	 * 右下角进入动画
	 * 
	 * @param a
	 */
	public void appearBottomRightAnimation(Activity a) {
		a.overridePendingTransition(R.anim.appear_bottom_right_in,
				R.anim.appear_bottom_right_out);
	}

	/**
	 * 右下角退出动画
	 * 
	 * @param a
	 */
	public void appearBottomRightBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.disappear_bottom_right_in,
				R.anim.disappear_bottom_right_out);
	}

	/**
	 * 缩放动画进入
	 * 
	 * @param a
	 */
	public void unzoomAnimation(Activity a) {
		a.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
	}

	/**
	 * 缩放动画退出
	 * 
	 * @param a
	 */
	public void unzoomBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
	}

	/**
	 * 层叠效果-进入
	 * 
	 * @param a
	 */
	public void stackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.open_next, R.anim.close_main);
	}

	/**
	 * 层叠效果-退出
	 * 
	 * @param a
	 */
	public void stackBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.open_main, R.anim.close_next);
	}

	/**
	 * 水平平移效果-右进左退
	 * 
	 * @param a
	 */
	public void slideLeftRightAnimation(Activity a) {
		a.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
	}

	/**
	 * 水平平移效果-左进右出
	 * 
	 * @param a
	 */
	public void slideLeftRightBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	/**
	 * 垂直平移效果1-
	 * 
	 * @param a
	 */
	public void slideTopBottomAnimation(Activity a) {
		a.overridePendingTransition(R.anim.slide_top_in,
				R.anim.slide_bottom_out);
	}

	/**
	 * 垂直平移效果1-
	 * 
	 * @param a
	 */
	public void slideTopBottomBackAnimation(Activity a) {
		a.overridePendingTransition(R.anim.slide_bottom_in,
				R.anim.slide_top_out);
	}

}
