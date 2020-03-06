package com.txsh.model;

import com.google.gson.annotations.Expose;
import com.zuomei.model.MLBaseResponse;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Marcello on 2015/6/17.
 */
public class TXHomeTTRes extends MLBaseResponse {

    @Expose
    public List<TXHomeTTData> datas;

    public class TXHomeTTData implements Serializable{
        @Expose
        public String id;
        @Expose
        public String title;
    }
}
