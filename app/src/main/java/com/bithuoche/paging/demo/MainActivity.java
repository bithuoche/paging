package com.bithuoche.paging.demo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bithuoche.paging.LoadState;
import com.bithuoche.paging.PageResult;
import com.bithuoche.paging.PagingAdapter;
import com.bithuoche.paging.PagingStateHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        final UserPagingViewModel userPagingViewModel = ViewModelProviders.of(this).get(UserPagingViewModel.class);
        final UserAdapter userAdapter = new UserAdapter(new PagingAdapter.LoadNextPageCallback() {
            @Override
            public void loadNextPage(boolean resumeLastFailed) {
                userPagingViewModel.loadNextPage(resumeLastFailed);
            }
        });
        recyclerView.setAdapter(userAdapter);

        final PagingStateHelper pagingStateHelper = new PagingStateHelper(findViewById(R.id.loading),
                (TextView) findViewById(R.id.no_content_hint), recyclerView, new Runnable() {
            @Override
            public void run() {
                userPagingViewModel.initLoad();
            }
        });

        userPagingViewModel.getPageResult()
                .observe(this, new Observer<PageResult<User>>() {
                    @Override
                    public void onChanged(@Nullable PageResult<User> userPageResult) {
                        userAdapter.setPageResult(userPageResult);
                    }
                });

        userPagingViewModel.getLoadState()
                .observe(this, new Observer<LoadState>() {
                    @Override
                    public void onChanged(@Nullable LoadState loadState) {
                        userAdapter.setLoadState(loadState);
                        pagingStateHelper.switchState(loadState);
                    }
                });

        userPagingViewModel.initLoad();
    }
}
