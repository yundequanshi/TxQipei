package cn.ml.base.widget.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.ml.base.R;

@SuppressLint("NewApi")
public class MLStar extends LinearLayout{

	private Context mContext;
	private   int count_f;

	public MLStar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}

	public MLStar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		   TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.star);   
	        count_f = array.getInt(R.styleable.star_count, 0);
		init();
	}

	public MLStar(Context context) {
		super(context);
		mContext = context;
		init();
	}
	
	private void init(){
		setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for(int i=0;i<count_f;i++){
			ImageView star_f = new ImageView(mContext);
			star_f.setImageResource(R.drawable.star_f);
			addView(star_f, param);
		}
		for(int i=0;i<(5-count_f);i++){
			ImageView star_n = new ImageView(mContext);
			star_n.setImageResource(R.drawable.star_n);
			addView(star_n, param);
		}
	}
	
	public void setStarCount(int count){
		count_f = count;
		removeAllViews();
		init();
	}
		
}
