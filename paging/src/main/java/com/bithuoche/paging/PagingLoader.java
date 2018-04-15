package com.bithuoche.paging;

import java.util.ArrayList;
import java.util.List;

public abstract class PagingLoader<T, Key> {
    private LoadState loadState;
    private final Key initKey;
    private final int pageSize;
    private Key key;
    private List<T> dataList;

    protected PagingLoader(Key initKey, int pageSize) {
        this.initKey = initKey;
        this.pageSize = pageSize;
    }

    public void publishLoadState(LoadState loadState) {
        this.loadState = loadState;
    }

    public void initLoad() {
        if (loadState == LoadState.INIT_LOADING_WITH_CACHE
                || loadState == LoadState.INIT_LOADING_WITHOUT_CACHE) {
            return;
        }
        if (ObjectUtils.isEmpty(dataList)) {
            publishLoadState(LoadState.INIT_LOADING_WITHOUT_CACHE);
        } else {
            publishLoadState(LoadState.INIT_LOADING_WITH_CACHE);
        }
        final Key keepKey = key;
        key = initKey;
        load(key, pageSize, new PageLoadCallback<T>() {
            @Override
            public void onPageLoad(List<T> dataList) {
                key = getNextKey(key, dataList);
                setDataList(dataList, null, dataList);
                if (dataList.size() < pageSize) {
                    publishLoadState(ObjectUtils.isEmpty(PagingLoader.this.dataList) ?
                            LoadState.LOAD_FINISHED_WITHOUT_CONTENT : LoadState.LOAD_FINISHED_WITH_CONTENT);
                } else {
                    publishLoadState(LoadState.INIT_LOAD_SUCCESS);
                }
            }

            @Override
            public void onPageLoadFailed(Throwable throwable) {
                key = keepKey;
                if (ObjectUtils.isEmpty(dataList)) {
                    publishLoadState(LoadState.INIT_FAILED_WITHOUT_CACHE);
                } else {
                    publishLoadState(LoadState.INIT_FAILED_WITH_CACHE);
                }
            }
        });
    }

    protected abstract void load(Key key, int pageSize, PageLoadCallback<T> pageLoadCallback);

    protected abstract Key getNextKey(Key key, List<T> dataList);

    protected abstract void publishPageResult(PageResult<T> pageResult);

    public void loadNextPage(boolean resumeLastFailed) {
        final boolean loadIfFailedLastTime =
                resumeLastFailed && loadState == LoadState.LOAD_NEXT_FAILED;
        if (!loadIfFailedLastTime
                && loadState != LoadState.INIT_LOAD_SUCCESS
                && loadState != LoadState.LOAD_NEXT_SUCCESS) {
            return;
        }
        publishLoadState(LoadState.LOADING_NEXT);
        load(key, pageSize, new PageLoadCallback<T>() {
            @Override
            public void onPageLoad(List<T> dataList) {
                key = getNextKey(key, dataList);
                if (PagingLoader.this.dataList == null) {
                    setDataList(dataList, null, dataList);
                } else {
                    List<T> newDataList = new ArrayList<>();
                    newDataList.addAll(PagingLoader.this.dataList);
                    newDataList.addAll(dataList);
                    setDataList(newDataList, PagingLoader.this.dataList, dataList);
                }
                if (dataList.size() < pageSize) {
                    publishLoadState(ObjectUtils.isEmpty(PagingLoader.this.dataList) ?
                            LoadState.LOAD_FINISHED_WITHOUT_CONTENT : LoadState.LOAD_FINISHED_WITH_CONTENT);
                } else {
                    publishLoadState(LoadState.LOAD_NEXT_SUCCESS);
                }
            }

            @Override
            public void onPageLoadFailed(Throwable throwable) {
                publishLoadState(LoadState.LOAD_NEXT_FAILED);
            }
        });
    }

    private void setDataList(List<T> dataList, List<T> oldList, List<T> newerList) {
        this.dataList = dataList;
        publishPageResult(new PageResult<>(dataList, oldList, newerList));
    }
}
