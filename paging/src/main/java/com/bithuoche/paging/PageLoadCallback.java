package com.bithuoche.paging;

import java.util.List;

public interface PageLoadCallback<T> {

    void onPageLoad(List<T> dataList);

    void onPageLoadFailed(Throwable throwable);
}
