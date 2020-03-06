package com.qipei.model;

import java.io.Serializable;

/**
 * Created by Marcello on 2015/6/8.
 */
public class MLMapData implements Serializable{

    public int id;

    public String value;

    public MLMapData() {
    }
    public MLMapData(int id, String value) {
        this.id = id;
        this.value = value;
    }
}
