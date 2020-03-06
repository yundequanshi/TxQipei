package com.zuomei.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.txsh.R;
import com.zuomei.base.BasePopupWindow;

public class MLMessagePhotoPop extends BasePopupWindow{

	@ViewInject(R.id.add_iv_photo)
	private ImageView _photoIv;
	
	public MLMessagePhotoPop(Context context) {
		super(context);
	}

	public MLMessagePhotoPop(Activity context,String path) {  
		//使用异步加载图片
		super(context);
		final View view = LayoutInflater.from(context).inflate(R.layout.message_photo, null);
        ViewUtils.inject(this, view);
        
      // Drawable drawable = Drawable.createFromPath(path);
        bitmapUtils.display(_photoIv, path, bigPicDisplayConfig);		
     /*   _photoIv.setTag(path);
		 if (!BaseApplication.IMAGE_CACHE.get(path, _photoIv)) {
			 _photoIv.setImageDrawable(null);
		    }*/
		  
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
//       ColorDrawable dw = new ColorDrawable(0xb0000000);  
       ColorDrawable dw = new ColorDrawable(Color.BLACK);  
        //设置LTSelectPopupWindow弹出窗体的背景  
       this.setBackgroundDrawable(dw);  
        
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        
        view.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                 if(event.getAction()==MotionEvent.ACTION_UP){  
                	 dismiss();  
                 }   
            /*    int height = view.findViewById(R.id.add_iv_photo).getTop();  
                int bottom = view.findViewById(R.id.add_iv_photo).getBottom(); 
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y>height||y<bottom){  
                        dismiss();  
                    }  
                }                */
                return true;  
            }  
        });  
        }
	
/*	public void setButtonOnclickListener(OnClickListener clickListener){
		_delBtn.setOnClickListener(clickListener);
		_blackBtn.setOnClickListener(clickListener);
		_reportBtn.setOnClickListener(clickListener);
		_weiboBtn.setOnClickListener(clickListener);
	}*/
}
