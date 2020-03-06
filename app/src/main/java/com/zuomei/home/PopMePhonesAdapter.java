package com.zuomei.home;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.txsh.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 汉玉 on 2017/4/6.
 */
public class PopMePhonesAdapter extends BaseAdapter {

  private List<String> dataList = new ArrayList<String>();
  private HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
  private Context context;

  public PopMePhonesAdapter() {
  }

  public PopMePhonesAdapter(Context context, List<String> dataList) {
    this.dataList = dataList;
    this.context = context;
  }

  @Override
  public int getCount() {
    return dataList.size();
  }

  @Override
  public Object getItem(int position) {
    return dataList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  public List<String> getList() {
    List<String> list = new ArrayList<>();
    list.addAll(dataList);
    Iterator iter = hashMap.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry entry = (Map.Entry) iter.next();
      int key = (int) entry.getKey();
      list.set(key, (String) entry.getValue());
    }
    return list;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    String str = dataList.get(position);
    convertView = LayoutInflater.from(context).inflate(R.layout.item_me_phones, null);
    final EditText editText = (EditText) convertView.findViewById(R.id.et_phone);
    editText.setText(str);
    //为editText设置TextChangedListener，每次改变的值设置到hashMap
    //我们要拿到里面的值根据position拿值
    editText.addTextChangedListener(new TextWatcher() {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void beforeTextChanged(CharSequence s, int start,
          int count, int after) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        //将editText中改变的值设置的HashMap中
        hashMap.put(position, s.toString());
      }
    });

    //如果hashMap不为空，就设置的editText
    if (hashMap.get(position) != null) {
      editText.setText(hashMap.get(position));
    }

    return convertView;
  }

}

