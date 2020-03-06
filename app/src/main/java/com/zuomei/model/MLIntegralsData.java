package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by Marcello on 2015/6/14.
 */
public class MLIntegralsData {

    @Expose
    public String depotName;
    @Expose
    public String userPhoto;
    @Expose
    public String integalValue;
    @Expose
    public List<MLIntegralList> integrals;

    public class MLIntegralList{
        @Expose
        public String val;
        @Expose
        public String createTime;
        @Expose
        public String source;
    }
}


