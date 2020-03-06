package com.txsh.model;

/**
 * Created by Marcello on 2015/7/7.
 */
public class TXEventModel {

    public TXEventModel() {

    }
    public TXEventModel(int type, Object obj) {
        this.type = type;
        this.obj = obj;
    }

    public int type;
    public Object obj;


}
