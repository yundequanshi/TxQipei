package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/7/10.
 */
public class TXSupplyRes  extends MLBaseResponse implements Serializable {

    @Expose
    public List<TXSupplyData> datas;
    public class TXSupplyData{
        @Expose
        public String id;
        @Expose
        public String content;
        @Expose
        public String createTime;
        @Expose
        public String phone;
        @Expose
        public String imageId;
        @Expose
        public String name;
        @Expose
        public String userId;
        @Expose
        public String images;
        @Expose
        public String type;
        @Expose
        public String userType;

    }
}
