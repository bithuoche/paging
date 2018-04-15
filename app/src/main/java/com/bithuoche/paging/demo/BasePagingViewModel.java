package com.bithuoche.paging.demo;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bithuoche.paging.LoadState;
import com.bithuoche.paging.PageLoadCallback;
import com.bithuoche.paging.PageResult;
import com.bithuoche.paging.PagingLoader;

import java.util.List;

public abstract class BasePagingViewModel<T, Key> extends ViewModel {

    private final static int PAGE_SIZE = 20;

    private MutableLiveData<LoadState> loadState = new MutableLiveData<>();
    private MutableLiveData<PageResult<T>> pageResult = new MutableLiveData<>();

    public MutableLiveData<PageResult<T>> getPageResult() {
        return pageResult;
    }

    public LiveData<LoadState> getLoadState() {
        return loadState;
    }

    public void initLoad() {
        pagingLoader.initLoad();
    }

    public void loadNextPage(boolean resumeLastFailed) {
        pagingLoader.loadNextPage(resumeLastFailed);
    }

    protected abstract void load(Key key, int pageSize, PageLoadCallback<T> pageLoadCallback);

    protected abstract Key getNextKey(Key key, List<T> dataList);

    protected abstract Key getInitKey();

    private PagingLoader<T, Key> pagingLoader = new PagingLoader<T, Key>(getInitKey(), PAGE_SIZE) {
        @Override
        protected void load(Key key, int pageSize, PageLoadCallback<T> pageLoadCallback) {
            BasePagingViewModel.this.load(key, pageSize, pageLoadCallback);
        }

        @Override
        protected Key getNextKey(Key key, List<T> dataList) {
            return BasePagingViewModel.this.getNextKey(key, dataList);
        }

        @Override
        protected void publishPageResult(PageResult<T> pageResult) {
            BasePagingViewModel.this.pageResult.postValue(pageResult);
        }

        @Override
        public void publishLoadState(LoadState loadState) {
            super.publishLoadState(loadState);
            BasePagingViewModel.this.loadState.postValue(loadState);
        }

    };

}
