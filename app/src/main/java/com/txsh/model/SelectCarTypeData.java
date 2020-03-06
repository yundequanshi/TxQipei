package com.txsh.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 * Created by lingyun on 2016/9/24.
 */
public class SelectCarTypeData implements Serializable {

    @Expose
    public String id;

    @Expose
    public String name;

    @Expose
    public String logo;

    @Expose
    public String isHot;

    public String sortLetters;
}
