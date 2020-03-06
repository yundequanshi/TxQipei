package cn.ml.base.widget.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ml.base.R;
/**
 *   RelativeLayout 最下方横线 +  左侧文字
 * @author Marcello
 *
 */
public class MLLineLayout extends RelativeLayout{

	
	private Context _context;
	public MLLineLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		_context = context;
		init();
	}

	private String name ;
	private int icon ;
	private boolean arrow ;
	public MLLineLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Linelayout); 
		name = a.getString(R.styleable.Linelayout_text);
		icon = a.getResourceId(R.styleable.Linelayout_mll_icon, 0);
		arrow = a.getBoolean(R.styleable.Linelayout_arrows, false);
		
		_context = context;
		init();
	}

	public MLLineLayout(Context context) {
		super(context);
		_context = context;
		init();
	}
	private Paint paint;
	private TextView _nameTv;
	private ImageView _iconIv;
	private ImageView _arrowsIv;
	private void init() {
		 setWillNotDraw(false);
		 
		 /*float font_size = getResources().getDimension(R.dimen.layout_font_size); 
		 float font_l = getResources().getDimension(R.dimen.tv_size_body2); 
		 int font_color = getResources().getColor(R.color.tv_black1);*/
		 View view = LayoutInflater.from(_context).inflate(R.layout.widget_linelayout, null);
		 
		 _nameTv = (TextView) view.findViewById(R.id.line_tv_label);
		 _iconIv = (ImageView) view.findViewById(R.id.line_iv_icon);
		 _arrowsIv = (ImageView) view.findViewById(R.id.line_iv_arrows);
		 //显示左侧icon
		 if(icon!=0){
			 _iconIv.setVisibility(View.VISIBLE);
			 _iconIv.setImageResource(icon);
		 }
		 
		 //显示右侧箭头
		 if(arrow){
			 _arrowsIv.setVisibility(View.VISIBLE);
		 }
		 
		 
		 _nameTv.setText(name);
		/* TextView tv = new TextView(_context);
		 tv.setText(name);
		 tv.setTextColor(font_color);
		 tv.setTextSize(font_size);
		 LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 params.addRule(RelativeLayout.CENTER_VERTICAL);
		 params.leftMargin = (int) font_l;*/
		 LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		 addView(view,params);
		 //=============画笔==============================================
		 paint = new Paint();   
		  paint.setColor(getResources().getColor(R.color.tv_black3));   
          // 设置样式-填充   
          paint.setStyle(Style.FILL);   
          paint.setStrokeWidth(1);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		int w = getWidth();
		int h = getHeight();
		canvas.drawLine(0, h-3, w,h-3, paint);
		
	/*	float font_size = getResources().getDimension(R.dimen.layout_font_size); 
		paint.setTextSize(font_size);
		float font_l = getResources().getDimension(R.dimen.layout_font_l); 
		canvas.drawText("文字", font_l, h/2, paint);*/
	}
	
}
