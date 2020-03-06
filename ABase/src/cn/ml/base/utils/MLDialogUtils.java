package cn.ml.base.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.ml.base.R;

public class MLDialogUtils {
	
	protected static ProgressDialog progressDialog = null;
	/**
	 * 显示加载 ProgressDialog
	 * @param context
	 * @param message
	 */
	public static void showProgressDialog(Context context, String message) {
		if(context==null)return;
		if (progressDialog == null) {
			 progressDialog = new ProgressDialog(context,
					ProgressDialog.THEME_HOLO_LIGHT);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			progressDialog.setMessage(message);
		}
		if (MLStrUtil.isEmpty(message))
			progressDialog.setMessage(MLToolUtil
					.getResourceString(R.string.loading_message));
		else
			progressDialog.setMessage(message);


		try {
			progressDialog.show();
		} catch (Exception e) {
		}

	}
	
	/**
	 * dismiss ProgressDialog
	 */
	public static void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}


	/**
	 * 返回AlertDialog
	 * @param mContext
	 * @return
	 */
	public static AlertDialog.Builder getAlertDialog(Context mContext){
		return new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_LIGHT);
	}


	public static void setDialogTitleColor(Dialog builder,int color){
		try {
			Context context = builder.getContext();
			int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
			View divider = builder.findViewById(divierId);
			divider.setBackgroundColor(context.getResources().getColor(color));

			int alertTitleId = context.getResources().getIdentifier("alertTitle", "id", "android");
			TextView alertTitle = (TextView) builder.findViewById(alertTitleId);
			alertTitle.setTextColor(context.getResources().getColor(color));
		}catch (Exception e){

		}

	}

	public static void setPickerDividerColor(DatePicker datePicker,int color){
		// Divider changing:

		// 获取 mSpinners
		LinearLayout llFirst       = (LinearLayout) datePicker.getChildAt(0);

		// 获取 NumberPicker
		LinearLayout mSpinners      = (LinearLayout) llFirst.getChildAt(0);
		for (int i = 0; i < mSpinners.getChildCount(); i++) {
			NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

			Field[] pickerFields = NumberPicker.class.getDeclaredFields();
			for (Field pf : pickerFields) {
				if (pf.getName().equals("mSelectionDivider")) {
					pf.setAccessible(true);
					try {
						pf.set(picker, new ColorDrawable(datePicker.getContext().getResources().getColor(color)));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (Resources.NotFoundException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}


	public static void choiceTime(Context context,String date,DatePickerDialog.OnDateSetListener dateSetListener){
		Calendar c = Calendar.getInstance();
		if(!MLStrUtil.isEmpty(date)){
			c.setTime(strToDate("yyyy-MM-dd", date));
		}
		DatePickerDialog dialog = new DatePickerDialog(
				context,DatePickerDialog.THEME_HOLO_LIGHT,
				dateSetListener,
				c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		);
		dialog.show();

	setDialogTitleColor(dialog, R.color.cm_app_color);
	setPickerDividerColor(dialog.getDatePicker(),R.color.cm_app_color);
	}



	public static Date strToDate(String style, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

}
