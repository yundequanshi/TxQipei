package com.zuomei.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

public class MLInteractionData extends MLBaseResponse {

  @Expose
  public InteractionData datas;

  public class InteractionData implements Serializable {

    @Expose
    public boolean flag;
  }
}
