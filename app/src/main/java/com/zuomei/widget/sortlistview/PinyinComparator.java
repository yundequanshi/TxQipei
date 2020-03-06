package com.zuomei.widget.sortlistview;

import com.zuomei.model.MLHomeCatalogData;

import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinComparator implements Comparator<MLHomeCatalogData> {

    public int compare(MLHomeCatalogData o1, MLHomeCatalogData o2) {
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
