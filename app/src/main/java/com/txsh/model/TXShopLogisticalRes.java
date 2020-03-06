package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/6/17.
 */
public class TXShopLogisticalRes extends MLBaseResponse {

    @Expose
    public TXShopLogisticalData datas;

    public class TXShopLogisticalData implements Serializable{
        @Expose
        public String logisOrderId;
        @Expose
        public String logisName;
        @Expose
        public String isLogis;
        @Expose
        public String mobile;
        @Expose
        public String endMobile;

    }



}
