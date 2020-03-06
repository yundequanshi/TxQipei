package com.zuomei.model;

import com.google.gson.annotations.Expose;
import com.qipei.model.ParseInfoData;
import java.util.List;

public class MLParseResponse extends MLBaseResponse {

  @Expose
  public List<ParseInfoData> datas;

}
