package com.yhp.fieenews.ui.base;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yhp.fieenews.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import mehdi.sakout.dynamicbox.DynamicBox;

public abstract class BaseListFragment<T> extends SupportFragment implements INetworkView<List> {

    private static final int VIEW_TYPE_LAST_ITEM = 1;
    private static final String TAG = "BaseListFragment";

    private BaseListPresenter<T> mPresenter = createPresenter();
    private LinearLayoutManager mManager;
    private SupportActivity mActivity;

    boolean hasMore = true;

    /**
     * 第三方库
     * 用来显示网络请求失败等错误信息或加载动画等
     */
    public DynamicBox mBox;

    @BindView(R.id.fab)
    FloatingActionButton mFAB;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.frame_list_container)
    FrameLayout mFrameContainer;

    private BaseAdapter<T> mAdapter = new BaseAdapter<T>() {
        @Override
        public BaseViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_LAST_ITEM) {
                // 上划加载
                return new LoadingViewHolder(parent, hasMore);
            }
            return provideViewHolder(parent, viewType);
        }

        @Override
        public int getItemCount() {
            // 有数据时，显示LoadingViewHolder，返回itemCount + 1
            return super.getItemCount() == 0 ? super.getItemCount() : super.getItemCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return VIEW_TYPE_LAST_ITEM;
            }
            return super.getItemViewType(position);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (SupportActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);
        ButterKnife.bind(this, view);
        initContent();
        if (mAdapter.getItemCount() == 0) {
            requestData();
        }
        return view;
    }

    private void initContent() {
        mBox = new DynamicBox(mActivity, mFrameContainer);
        mBox.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 加载失败时的按钮点击事件
                mAdapter.clear();
                requestData();
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mManager);

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击按钮返回顶部
                // mManager.smoothScrollToPosition(mRecyclerView, null, 0);
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                requestData();
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 当位于RecyclerView顶部的时候隐藏FAB按钮，其他时候显示FAB按钮
                mFAB.setVisibility(
                        mManager.findFirstVisibleItemPosition() > 0 ? View.VISIBLE : View.GONE);

                // 当位于RecyclerView底部的时候上划加载更多内容
                if (!mSwipeRefreshLayout.isRefreshing() && hasMore()
                        && mManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1) {
                    requestMore();
                }
            }
        });
    }

    @Override
    public void onSuccess(List list) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mBox.hideAll();
        if (list != null && list.size() != 0) {
            mAdapter.addItems(list);
            Log.e(TAG, "*********请求成功");
        }
    }

    @Override
    public void onError(Throwable e) {
        mBox.setOtherExceptionMessage(e.getMessage());
        mBox.showExceptionLayout();
    }

    private void requestData() {
        mBox.showLoadingLayout();
        hasMore = true;
        getPresenter().start();
    }

    private void requestMore() {
        mSwipeRefreshLayout.setRefreshing(true);
        getPresenter().startRequestMore();
    }

    public boolean hasMore() {
        return hasMore;
    }

    public abstract BaseViewHolder<T> provideViewHolder(ViewGroup parent, int viewType);

    public abstract BaseListPresenter<T> createPresenter();

    @Override
    public BaseListPresenter<T> getPresenter() {
        return mPresenter;
    }
}