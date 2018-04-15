package com.bithuoche.paging;

public enum LoadState {
    INIT_LOADING_WITHOUT_CACHE, INIT_LOADING_WITH_CACHE,
    INIT_FAILED_WITHOUT_CACHE, INIT_FAILED_WITH_CACHE,
    INIT_LOAD_SUCCESS,
    LOADING_NEXT, LOAD_NEXT_FAILED, LOAD_NEXT_SUCCESS,
    LOAD_FINISHED_WITH_CONTENT, LOAD_FINISHED_WITHOUT_CONTENT
}
