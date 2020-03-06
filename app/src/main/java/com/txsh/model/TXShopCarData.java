package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/7/21.
 */
public class TXShopCarData extends MLBaseResponse {

    @Expose
    public List<TXShopCarDetailData> datas;



    public class TXShopCarDetailData implements Serializable {
        @Expose
        public String id;
        @Expose
        public String productShelveState;
        @Expose
        public String privce;
        @Expose
        public String oldprivce;
        @Expose
        public String sales;
        @Expose
        public String imgPath;
        @Expose
        public String number;

        public String addplus;

        public boolean isChoose;

        public int buyednum;

        @Expose
        public String productName;
        @Expose
        public String productState;
        @Expose
        public String productFreight;
        @Expose
        public String productId;
    }

}
