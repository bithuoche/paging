package com.bithuoche.paging;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class PagingStateHelper {

    private final View loadingView;
    private final TextView noContentTipsTextView;
    private final View contentView;
    private final Runnable initLoadCallback;

    public PagingStateHelper(View loadingView, TextView noContentTipsTextView, View contentView, Runnable initLoadCallback) {
        this.loadingView = loadingView;
        this.noContentTipsTextView = noContentTipsTextView;
        this.contentView = contentView;
        this.initLoadCallback = initLoadCallback;
    }

    public void switchState(LoadState loadState) {
        if (loadState == null) {
            return;
        }
        switch (loadState) {
            case LOAD_FINISHED_WITHOUT_CONTENT:
                contentView.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                noContentTipsTextView.setVisibility(View.VISIBLE);
                noContentTipsTextView.setText(R.string.no_more_content);
                noContentTipsTextView.setOnClickListener(null);
                break;
            case INIT_FAILED_WITHOUT_CACHE:
                loadingView.setVisibility(View.GONE);
                noContentTipsTextView.setText(R.string.click_retry_network_request);
                noContentTipsTextView.setVisibility(View.VISIBLE);
                noContentTipsTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initLoadCallback.run();
                        noContentTipsTextView.setVisibility(View.GONE);
                        loadingView.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case INIT_LOADING_WITHOUT_CACHE:
                loadingView.setVisibility(View.VISIBLE);
                noContentTipsTextView.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
                break;
            case INIT_FAILED_WITH_CACHE:
                Toast.makeText(loadingView.getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
            case INIT_LOADING_WITH_CACHE:
            default:
                contentView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                noContentTipsTextView.setVisibility(View.GONE);
        }
    }
}
