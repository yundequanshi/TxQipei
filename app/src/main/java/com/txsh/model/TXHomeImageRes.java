package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;

/**
 * Created by Marcello on 2015/6/25.
 */
public class TXHomeImageRes extends MLBaseResponse {

    @Expose
    public TXHomeImageData datas;

    public class TXHomeImageData{
        @Expose
        public String goodbusinessman;
        @Expose
        public String Specialsale;
        @Expose
        public String Integralmall;
    }


}
