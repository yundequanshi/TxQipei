package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopPlayListRes extends MLBaseResponse {

  @Expose
  public List<TXHomeImageData> datas;

  public class TXHomeImageData {

    @Expose
    public String id;

    @Expose
    public String img;
  }
}
