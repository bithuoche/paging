package com.bithuoche.paging.demo;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class UserItemViewHolder extends RecyclerView.ViewHolder {

    public UserItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bindTo(User user) {
        TextView textView = itemView.findViewById(android.R.id.text1);
        textView.setText(user.firstName + "  " + user.userId);
    }

}
