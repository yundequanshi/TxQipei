package com.zuomei.widget.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.txsh.R;


/**
 * 加载中Dialog
 *
 * @author xm
 */
public class MLLoadingDialog extends AlertDialog {

    private TextView tips_loading_msg;

    private String message = null;

    public MLLoadingDialog(Context context) {
        super(context);
        //message = getContext().getResources().getString("加载中...");
        message = "加载中";
    }

    public MLLoadingDialog(Context context, String message) {
        super(context);
        this.message = message;
        this.setCancelable(false);
    }

    public MLLoadingDialog(Context context, int theme, String message) {
        super(context, theme);
        this.message = message;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.widget_tips_loading);
        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        tips_loading_msg.setText(this.message);
    }

    public void setText(String message) {
        this.message = message;
        tips_loading_msg.setText(this.message);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }

}
