package com.bithuoche.paging.demo;

import com.bithuoche.paging.PageLoadCallback;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPagingViewModel extends BasePagingViewModel<User, Long> {

    GitHubService gitHubService = GitHubApi.createGitHubService();

    @Override
    protected void load(Long key, int pageSize, final PageLoadCallback<User> pageLoadCallback) {
        gitHubService.getUser(key, pageSize).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    pageLoadCallback.onPageLoad(response.body());
                } else {
                    pageLoadCallback.onPageLoadFailed(new IOException());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                pageLoadCallback.onPageLoadFailed(t);
            }
        });
    }

    @Override
    protected Long getNextKey(Long aLong, List<User> dataList) {
        return aLong + dataList.size();
    }

    @Override
    protected Long getInitKey() {
        return 0L;
    }
}
