package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopYueCheckRes extends MLBaseResponse {

    @Expose
    public TXShopYueCheckData datas;

    public class TXShopYueCheckData implements Serializable{
        @Expose
        public double money;
        @Expose
        public double payCount;
    }


}
