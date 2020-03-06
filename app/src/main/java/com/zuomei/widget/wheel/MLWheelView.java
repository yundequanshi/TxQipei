package com.zuomei.widget.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txsh.R;
import com.zuomei.base.BaseLayout;

import java.util.ArrayList;
import java.util.Calendar;

import cn.ml.base.utils.IEvent;

public class MLWheelView extends BaseLayout {

    private Context _context;

    public MLWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        _context = context;
        init();
    }

    public MLWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _context = context;
        init();
    }

    private IEvent _event;
    private OnClickListener _onClickListener;

    public MLWheelView(Context context, IEvent<String> event) {
        super(context);
        _event = event;
        _context = context;
        init();
    }

    ArrayList<TextInfo> mMonths = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mYears = new ArrayList<TextInfo>();
    ArrayList<TextInfo> mDates = new ArrayList<TextInfo>();

    //TextView mSelDateTxt = null;
    WheelView mDateWheel = null;
    WheelView mMonthWheel = null;
    WheelView mYearWheel = null;

    int mCurDate = 0;
    int mCurMonth = 0;
    int mCurYear = 0;

    private TextView _okBtn;

    private void init() {
        View view = LayoutInflater.from(_context).inflate(R.layout.widget_wheel_date, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        addView(view, params);

        //  mSelDateTxt = (TextView) findViewById(R.id.sel_date);

        mDateWheel = (WheelView) findViewById(R.id.wheel_date);
        mMonthWheel = (WheelView) findViewById(R.id.wheel_month);
        mYearWheel = (WheelView) findViewById(R.id.wheel_year);

        mDateWheel.setOnEndFlingListener(mListener);
        mMonthWheel.setOnEndFlingListener(mListener);
        mYearWheel.setOnEndFlingListener(mListener);

        mDateWheel.setSoundEffectsEnabled(true);
        mMonthWheel.setSoundEffectsEnabled(true);
        mYearWheel.setSoundEffectsEnabled(true);

        mDateWheel.setAdapter(new WheelTextAdapter(_context));
        mMonthWheel.setAdapter(new WheelTextAdapter(_context));
        mYearWheel.setAdapter(new WheelTextAdapter(_context));

        prepareData();

        _okBtn = (TextView) view.findViewById(R.id.login_city_ok);
        _okBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                _event.onEvent(null, formatDate());

            }
        });
        //    mSelDateTxt.setText(formatDate());


    }

    private TosGallery.OnEndFlingListener mListener = new TosGallery.OnEndFlingListener() {
        @Override
        public void onEndFling(TosGallery v) {
            int pos = v.getSelectedItemPosition();

            if (v == mDateWheel) {
                TextInfo info = mDates.get(pos);
                setDate(info.mIndex);
            } else if (v == mMonthWheel) {
                TextInfo info = mMonths.get(pos);
                setMonth(info.mIndex);
            } else if (v == mYearWheel) {
                TextInfo info = mYears.get(pos);
                setYear(info.mIndex);
            }

            //  mSelDateTxt.setText(formatDate());
        }
    };

    private String formatDate() {
        return String.format("%d-%02d-%02d", mCurYear, mCurMonth + 1, mCurDate);
    }

    private void setDate(int date) {
        if (date != mCurDate) {
            mCurDate = date;
        }
    }

    private void setYear(int year) {
        if (year != mCurYear) {
            mCurYear = year;
        }
    }

    private void setMonth(int month) {
        if (month != mCurMonth) {
            mCurMonth = month;

            Calendar calendar = Calendar.getInstance();
            int date = calendar.get(Calendar.DATE);
            prepareDayData(mCurYear, month, date);
        }
    }

    private static final int[] DAYS_PER_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    private static final String[] MONTH_NAME = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    private boolean isLeapYear(int year) {
        return ((0 == year % 4) && (0 != year % 100) || (0 == year % 400));
    }

    private void prepareData() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        int startYear = 1980;
        int endYear = 2038;

        mCurDate = day;
        mCurMonth = month;
        mCurYear = year;

        for (int i = 0; i < MONTH_NAME.length; ++i) {
            mMonths.add(new TextInfo(i, MONTH_NAME[i], (i == month)));
        }

        for (int i = startYear; i <= endYear; ++i) {
            mYears.add(new TextInfo(i, String.valueOf(i), (i == year)));
        }

        ((WheelTextAdapter) mMonthWheel.getAdapter()).setData(mMonths);
        ((WheelTextAdapter) mYearWheel.getAdapter()).setData(mYears);

        prepareDayData(year, month, day);

        mMonthWheel.setSelection(month);
        mYearWheel.setSelection(year - startYear);
        mDateWheel.setSelection(day - 1);
    }

    private void prepareDayData(int year, int month, int curDate) {
        mDates.clear();

        int days = DAYS_PER_MONTH[month];

        // The February.
        if (1 == month) {
            days = isLeapYear(year) ? 29 : 28;
        }

        for (int i = 1; i <= days; ++i) {
            mDates.add(new TextInfo(i, String.valueOf(i), (i == curDate)));
        }

        ((WheelTextAdapter) mDateWheel.getAdapter()).setData(mDates);
    }

    protected class TextInfo {
        public TextInfo(int index, String text, boolean isSelected) {
            mIndex = index;
            mText = text;
            mIsSelected = isSelected;

            if (isSelected) {
                mColor = Color.BLUE;
            }
        }

        public int mIndex;
        public String mText;
        public boolean mIsSelected = false;
        public int mColor = Color.BLACK;
    }

    protected class WheelTextAdapter extends BaseAdapter {
        ArrayList<TextInfo> mData = null;
        int mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
        int mHeight = 50;
        Context mContext = null;

        public WheelTextAdapter(Context context) {
            mContext = context;
            mHeight = (int) Utils.pixelToDp(context, mHeight);
        }

        public void setData(ArrayList<TextInfo> data) {
            mData = data;
            this.notifyDataSetChanged();
        }

        public void setItemSize(int width, int height) {
            mWidth = width;
            mHeight = (int) Utils.pixelToDp(mContext, height);
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if (null == convertView) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new TosGallery.LayoutParams(mWidth, mHeight));
                textView = (TextView) convertView;
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                textView.setTextColor(Color.BLACK);
            }

            if (null == textView) {
                textView = (TextView) convertView;
            }

            TextInfo info = mData.get(position);
            textView.setText(info.mText);
            textView.setTextColor(info.mColor);

            return convertView;
        }
    }
}
