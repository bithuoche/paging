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
    protected void load(final Long key, final int pageSize, final PageLoadCallback<User> pageLoadCallback) {
        gitHubService.getUser(key, pageSize).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    List<User> users = response.body();

                    // Mock reaching end
                    if (key > 30) {
                        users = users.subList(0, pageSize / 2);
                    }
                    // End

                    pageLoadCallback.onPageLoad(users);
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
