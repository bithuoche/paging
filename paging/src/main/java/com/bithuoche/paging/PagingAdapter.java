package com.bithuoche.paging;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class PagingAdapter<T, VH extends RecyclerView.ViewHolder> extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface LoadNextPageCallback {
        void loadNextPage(boolean resumeLastFailed);
    }

    private final int TYPE_LOAD_MORE = -19870812;

    private LoadState loadState;

    private List<T> dataList;

    private final LoadNextPageCallback loadNextPageCallback;

    public PagingAdapter(LoadNextPageCallback loadNextPageCallback) {
        this.loadNextPageCallback = loadNextPageCallback;
    }

    public void setLoadState(LoadState loadState) {
        this.loadState = loadState;
        notifyDataSetChanged();
    }

    public void setPageResult(PageResult<T> pageResult) {
        dataList = pageResult.dataList;
        if (!ObjectUtils.isEmpty(pageResult.oldList) && !ObjectUtils.isEmpty(pageResult.newerList)) {
            notifyItemRangeInserted(pageResult.oldList.size(), pageResult.newerList.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_LOAD_MORE) {
            return doCreateLoadMoreViewHolder(viewGroup, viewType);
        }
        return doCreateViewHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int position) {
        if (getItemViewType(position) == TYPE_LOAD_MORE) {
            doBindLoadMoreViewHolder(vh, position, loadState);
        } else {
            doBindViewHolder((VH) vh, position);
        }
        if (shouldLoadNextPageOnBindPosition(position)) {
            loadNextPageCallback.loadNextPage(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= getItemCount() - 1) {
            return TYPE_LOAD_MORE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return ObjectUtils.isEmpty(dataList) ? 0 : 1 + dataList.size();
    }

    public T getItem(int position) {
        return dataList.get(position);
    }

    protected boolean shouldLoadNextPageOnBindPosition(int position) {
        // TODO: 2018/4/15 when to load next page
        return position > getItemCount() - 5;
    }

    protected abstract VH doCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType);

    protected RecyclerView.ViewHolder doCreateLoadMoreViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new RecyclerView.ViewHolder(inflater.inflate(R.layout.paging_load_more_list_item, viewGroup, false)) {
        };
    }

    protected abstract void doBindViewHolder(@NonNull VH vh, int position);

    protected void doBindLoadMoreViewHolder(@NonNull RecyclerView.ViewHolder vh, int position, LoadState loadState) {
        boolean showLoading = loadState == null || loadState == LoadState.LOADING_NEXT;
        boolean loadError = loadState != null && loadState == LoadState.LOAD_NEXT_FAILED;
        String hint = vh.itemView.getContext()
                .getString(loadError ? R.string.click_retry_network_request : R.string.no_more_content);
        final ViewUtils viewUtils = ViewUtils.create(vh.itemView);
        viewUtils.setVisibility(R.id.paging_no_more_hint, showLoading ? View.INVISIBLE : View.VISIBLE)
                .setVisibility(R.id.paging_progress_bar, showLoading ? View.VISIBLE : View.INVISIBLE)
                .setText(R.id.paging_no_more_hint, hint)
                .setOnClickListener(R.id.paging_no_more_hint, !loadError ?
                        null : new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewUtils.setVisibility(R.id.paging_no_more_hint, View.INVISIBLE)
                                .setVisibility(R.id.paging_progress_bar, View.VISIBLE)
                                .setOnClickListener(R.id.paging_no_more_hint, null);
                        loadNextPageCallback.loadNextPage(true);
                    }
                });
    }

}
