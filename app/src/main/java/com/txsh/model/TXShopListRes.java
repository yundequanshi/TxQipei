package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXShopListRes extends MLBaseResponse {

    @Expose
    public List<TXShopListData> datas;



    public class TXShopListData implements Serializable{
        @Expose
        public String id;
        @Expose
        public String privce;
        @Expose
        public String oldprivce;
        @Expose
        public String sales;
        @Expose
        public String imgPath;
        @Expose
        public String name;
    }
}
