package com.zuomei.widget.sortlistview;

import com.txsh.model.TXShopTypeListRes.TXHomeGoodsTypeImageData;
import com.zuomei.model.MLHomeCityData;
import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinTypeComparator implements Comparator<TXHomeGoodsTypeImageData> {

    public int compare(TXHomeGoodsTypeImageData o1, TXHomeGoodsTypeImageData o2) {
        if (o1.sortLetters.equals("@")
                || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#")
                || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
