package com.zuomei.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txsh.R;


/**
 * RelativeLayout 最下方横线 +  左侧文字
 *
 * @author Marcello
 */
public class MLLineLayout extends RelativeLayout {


    private Context _context;

    public MLLineLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _context = context;
        init();
    }

    private String name;

    public MLLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ml);
        name = a.getString(R.styleable.ml_textValue);
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

    private void init() {
        setWillNotDraw(false);

        float font_size = getResources().getDimension(R.dimen.layout_font_size);
        float font_l = getResources().getDimension(R.dimen.layout_font_l);
        int font_color = getResources().getColor(R.color.common_black);

        View view = LayoutInflater.from(_context).inflate(R.layout.widget_linelayout, null);

        _nameTv = (TextView) view.findViewById(R.id.line_tv_label);
        _nameTv.setText(name);
        /* TextView tv = new TextView(_context);
		 tv.setText(name);
		 tv.setTextColor(font_color);
		 tv.setTextSize(font_size);
		 LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		 params.addRule(RelativeLayout.CENTER_VERTICAL);
		 params.leftMargin = (int) font_l;*/
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(view, params);
        //=============画笔==============================================
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.common_darker_gray));
        // 设置样式-填充
        paint.setStyle(Style.FILL);
        paint.setStrokeWidth(1);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        int w = getWidth();
        int h = getHeight();
        canvas.drawLine(0, h - 3, w, h - 3, paint);
		
	/*	float font_size = getResources().getDimension(R.dimen.layout_font_size); 
		paint.setTextSize(font_size);
		float font_l = getResources().getDimension(R.dimen.layout_font_l); 
		canvas.drawText("文字", font_l, h/2, paint);*/
    }

}
