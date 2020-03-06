package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/6/17.
 */
public class TXShopSumRes extends MLBaseResponse {

    @Expose
    public TXShopSumData datas;

    public class TXShopSumData implements Serializable{
        @Expose
        public String daiShou;
        @Expose
        public String daiFu;
        @Expose
        public String yiShou;
    }



}
