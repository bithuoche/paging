package com.bithuoche.paging.demo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bithuoche.paging.PagingAdapter;

public class UserAdapter extends PagingAdapter<User, UserItemViewHolder> {
    public UserAdapter(LoadNextPageCallback loadNextPageCallback) {
        super(loadNextPageCallback);
    }

    @Override
    protected UserItemViewHolder doCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new UserItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false));
    }

    @Override
    protected void doBindViewHolder(@NonNull UserItemViewHolder userItemViewHolder, int position) {
        userItemViewHolder.bindTo(getItem(position));
    }
}
