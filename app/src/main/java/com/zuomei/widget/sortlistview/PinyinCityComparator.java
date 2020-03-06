package com.zuomei.widget.sortlistview;

import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLHomeCityData;
import java.util.Comparator;

/**
 * @author xiaanming
 */
public class PinyinCityComparator implements Comparator<MLHomeCityData> {

    public int compare(MLHomeCityData o1, MLHomeCityData o2) {
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
