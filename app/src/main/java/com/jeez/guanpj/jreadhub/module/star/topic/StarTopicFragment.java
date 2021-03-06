package com.jeez.guanpj.jreadhub.module.star.topic;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jeez.guanpj.jreadhub.R;
import com.jeez.guanpj.jreadhub.bean.TopicDetailBean;
import com.jeez.guanpj.jreadhub.event.SearchByKeywordEvent;
import com.jeez.guanpj.jreadhub.module.adpter.TopicDetailListAdapterWithThirdLib;
import com.jeez.guanpj.jreadhub.mvpframe.rx.RxBus;
import com.jeez.guanpj.jreadhub.mvpframe.view.lce.animator.EmptyEffect;
import com.jeez.guanpj.jreadhub.mvpframe.view.lce.fragment.AbsBaseMvpLceFragment;
import com.jeez.guanpj.jreadhub.util.Constants;
import com.jeez.guanpj.jreadhub.util.ResourceUtil;
import com.jeez.guanpj.jreadhub.widget.custom.CustomLoadMoreView;

import java.util.List;

import butterknife.BindView;

public class StarTopicFragment extends AbsBaseMvpLceFragment<List<TopicDetailBean>, StarTopicPresenter>
        implements StarTopicContract.View<List<TopicDetailBean>>, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private TopicDetailListAdapterWithThirdLib mAdapter;
    private String mKeyword;

    public static StarTopicFragment newInstance() {
        Bundle args = new Bundle();
        StarTopicFragment fragment = new StarTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static StarTopicFragment newInstance(String keyWord) {
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_WORD, keyWord);
        StarTopicFragment fragment = new StarTopicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    public void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new TopicDetailListAdapterWithThirdLib();
        mAdapter.isFirstOnly(false);
        mAdapter.setNotDoAnimationCount(3);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(),
                ResourceUtil.getResource(getActivity(), R.attr.readhubTheme)));
    }

    @Override
    public void initDataAndEvent() {
        mAdapter.setOnLoadMoreListener(() -> doLoadMore(), mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!TextUtils.isEmpty(getArguments().getString(Constants.BUNDLE_KEY_WORD))) {
            mKeyword = getArguments().getString(Constants.BUNDLE_KEY_WORD);
            mPresenter.addSubscribe(RxBus.getInstance().toFlowable(SearchByKeywordEvent.class)
                    .subscribe(searchByKeywordEvent -> {
                        mKeyword = searchByKeywordEvent.getKeyword();
                        mPresenter.getDataWithKeyword(mKeyword);
                    }));
        }
        setLceSwitchEffect(EmptyEffect.getInstance());
        loadData(false);
    }

    @Override
    public void loadData(boolean isPullToRefresh) {
        if (TextUtils.isEmpty(mKeyword)) {
            mPresenter.doRefresh(isPullToRefresh);
        } else {
            mPresenter.getDataWithKeyword(mKeyword);
        }
    }

    @Override
    public void onRefresh() {
        mRefreshLayout.setRefreshing(true);
        mAdapter.setEnableLoadMore(false);
        loadData(true);
    }

    public void doLoadMore() {
        mPresenter.doLoadMore(mAdapter.getItem(mAdapter.getItemCount() - 2).getOrder());
    }

    @Override
    public void bindData(List<TopicDetailBean> data, boolean isPullToRefresh) {
        if (isPullToRefresh) {
            mRefreshLayout.setRefreshing(false);
            mAdapter.setNewData(data);
            mRecyclerView.scrollToPosition(0);
            mAdapter.setEnableLoadMore(true);
        } else {
            if (null != data && data.size() > 0) {
                mAdapter.addData(data);
                mAdapter.setEnableLoadMore(true);
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onFabClick() {
        mRecyclerView.scrollToPosition(0);
    }
}
