package com.zuomei.auxiliary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.model.MLMyStockDetail;
import com.zuomei.utils.MLToolUtil;

import cn.ml.base.utils.IEvent;

public class MLMyStockAddPop extends PopupWindow{
	@ViewInject(R.id.et_part)
	private EditText _partEt;
	
	@ViewInject(R.id.et_count)
	private EditText _countTv;
	@ViewInject(R.id.et_price)
	private EditText _etprice;
	@ViewInject(R.id.bt_zhiliang)
	private Button _zhiliang;

	
	private IEvent<String> _event;
	
	@ViewInject(R.id.rl_root)
	private RelativeLayout _root;
	
	
	@ViewInject(R.id.ly_status)
	private LinearLayout _qualityLL;
	
	private Context _context;
	public MLMyStockAddPop(Context context,IEvent<String> event) {  
		super(context);
		_event = event;
		_context = context;
        final View view = LayoutInflater.from(_context).inflate(R.layout.my_stock_pop, null);
        ViewUtils.inject(this, view);
		 
        
      //设置LTSelectPopupWindow的View  
        this.setContentView(view);  
        //设置LTSelectPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置LTSelectPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);  
        //设置LTSelectPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置LTSelectPopupWindow弹出窗体动画效果  
     //   this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明    
       ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置LTSelectPopupWindow弹出窗体的背景  
       this.setBackgroundDrawable(dw);  
        
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        
        view.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = view.findViewById(R.id.login_ly_status).getTop();  
                int bottom = view.findViewById(R.id.login_ly_status).getBottom();
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height||y>bottom){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
        }
	
	String zhiliang = "";
	@OnClick(R.id.bt_zhiliang)
	public void zhiliangOnClick(View view)
	{
		 final String s[] = {"正品", "副品", "高仿"};

	    AlertDialog builder = new AlertDialog.Builder(_context,AlertDialog.THEME_HOLO_LIGHT)
	            .setItems(s, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						_zhiliang.setText(s[arg1].toString());
						zhiliang=s[arg1].toString();
					}
				})
	            .setTitle("操作").create();
	    builder.show();
	}
	@OnClick(R.id.stock_ok)
	public void okOnClick(View view){
		String name = _partEt.getText().toString();
		String count = _countTv.getText().toString();
		String price = _etprice.getText().toString();
		if(MLToolUtil.isNull(name)){
			showMessage("名称不能为空!");
			return;
		} 
		if(MLToolUtil.isNull(count)) {
			showMessage("数量不能为空!");
			return;
		}
		if(MLToolUtil.isNull(price)) {
			showMessage("价格不能为空!");
			return;
		}
	/*	if(MLToolUtil.isNull(zhiliang)) {
			showMessage("质量不能为空!");
			return;
		}*/

		MLMyStockDetail detail = new MLMyStockDetail();
		detail.goodName = name;
		detail.goodNum = count;
		detail.price=price;
		detail.quality=zhiliang;
		_event.onEvent(detail, "");
		dismiss();
	}
	
	@OnClick(R.id.xinxiguanbi)
	public void xinxiguanbiOnClick(View view){
		   dismiss();  
	}
	

	private void showMessage(String text){
		Toast.makeText(_context, text, Toast.LENGTH_SHORT).show();
	}

}
