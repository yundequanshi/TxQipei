package com.zuomei.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.txsh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marcello
 * @description 选项卡
 */
public class MLTabGroup extends RadioGroup {

    Context context;

    public MLTabGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        sharedConstructor(context, attrs);
    }

    public MLTabGroup(Context context, int radioCount, String radioName) {
        super(context);
        this.context = context;
        if (radioCount <= 0) this.radioCount = 1;
        else
            this.radioCount = radioCount;
        if (radioName != null && radioName != "")
            radioTitles = radioName.split(",");

    }

    public MLTabGroup(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    int radioCount = 0;
    String[] radioTitles;

    private void sharedConstructor(Context context, AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.segmented);
        radioCount = a.getInt(R.styleable.segmented_radio_count, 1);
        String radioTitleText = a.getString(R.styleable.segmented_radio_name);
        a.recycle();
        if (radioTitleText != null && radioTitleText != "")
            radioTitles = radioTitleText.split(",");
        InitRadioButton();
    }

    @SuppressLint("ResourceType")
    private RadioButton createRadio(String title) {
        RadioButton rb = new RadioButton(context);
        rb.setText(title);
        rb.setTextSize(14.f);
        rb.setTextColor((ColorStateList) getResources().getColorStateList(R.drawable.segment_button_font));
        rb.setGravity(Gravity.CENTER);
        rb.setPadding(10, 0, 10,10);
        rb.setButtonDrawable(android.R.color.transparent);
        rb.setCompoundDrawables(null, null, null, null);
        return rb;
    }

    List<RadioButton> rbs = new ArrayList<RadioButton>();

    private void InitRadioButton() {
        int nameSize = radioTitles != null ? radioTitles.length : 0;
        for (int i = 0; i < radioCount; i++) {
            String title = "";
            if (nameSize > i) title = radioTitles[i];
            RadioButton rb = createRadio(title);
            if (radioCount == 1)
                rb.setBackgroundResource(R.drawable.tab2_tv_nav);
            else {
                if (i == 0)
                    rb.setBackgroundResource(R.drawable.tab2_tv_nav);
                else if (i == radioCount - 1)
                    rb.setBackgroundResource(R.drawable.tab2_tv_nav);
                else
                    rb.setBackgroundResource(R.drawable.tab2_tv_nav);
            }
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
            lp.setMargins(40, 0, 40, 0);
            this.addView(rb, lp);
            rbs.add(rb);
        }
        InitRadioButtonClick(0);
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (_radioCheckCB != null) {
                    RadioButton tmpRB = null;
                    int checkedIndex = 0;
                    for (int i = 0; i < rbs.size(); i++) {
                        if (rbs.get(i).getId() == checkedId) {
                            tmpRB = rbs.get(i);
                            checkedIndex = i;
                            break;
                        }
                    }
                    if (tmpRB != null)
                        _radioCheckCB.radioChecked(tmpRB, checkedIndex);
                }
            }
        });
    }

    /**
     * 初始化被点中的radiobutton
     *
     * @param index radioButton的索引，从0开始
     */
    public void InitRadioButtonClick(int index) {
        if (index >= radioCount) return;
        RadioButton rb = rbs.get(index);
        rb.setChecked(true);
    }

    public interface IRadioCheckedListener {
        void radioChecked(RadioButton rb, int index);
    }

    private IRadioCheckedListener _radioCheckCB;

    public void setRadioCheckedCallBack(IRadioCheckedListener listener) {
        _radioCheckCB = listener;
    }

}