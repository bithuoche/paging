package com.bithuoche.paging;

import java.util.List;

public class PageResult<T> {

    public final List<T> dataList;
    public final List<T> oldList;
    public final List<T> newerList;

    public PageResult(List<T> dataList, List<T> oldList, List<T> newerList) {
        this.dataList = dataList;
        this.oldList = oldList;
        this.newerList = newerList;
    }
}
