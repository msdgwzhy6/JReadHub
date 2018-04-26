package com.jeez.guanpj.jreadhub.ui.developer;

import com.jeez.guanpj.jreadhub.bean.DataListBean;
import com.jeez.guanpj.jreadhub.bean.NewsBean;
import com.jeez.guanpj.jreadhub.core.DataManager;
import com.jeez.guanpj.jreadhub.mvpframe.presenter.BasePresenter;
import com.jeez.guanpj.jreadhub.mvpframe.rx.RxSchedulers;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class DeveloperPresenter extends BasePresenter<DeveloperContract.View> implements DeveloperContract.Presenter {
    private DataManager mDataManager;
    private static final int PAGE_SIZE = 20;

    @Inject
    DeveloperPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void doRefresh() {
        mRxManager.add(mDataManager.getNewsList(NewsBean.TYPE_TECHNEWS, null, PAGE_SIZE)
                .compose(RxSchedulers.io2Main())
                .doOnSubscribe(disposable -> getView().showLoading(true))
                .subscribeWith(new DisposableObserver<DataListBean<NewsBean>>() {
                    @Override
                    public void onNext(DataListBean<NewsBean> newsBeanDataListBean) {
                        getView().onRequestEnd(newsBeanDataListBean, true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onRequestError(true);
                    }

                    @Override
                    public void onComplete() {
                        getView().showContent();
                    }
                }));
    }

    @Override
    public void doLoadMore(Long lastCursor) {
        mRxManager.add(mDataManager.getNewsList(NewsBean.TYPE_TECHNEWS, lastCursor, PAGE_SIZE)
                .compose(RxSchedulers.io2Main())
                .doOnSubscribe(disposable -> getView().showLoading(false))
                .subscribeWith(new DisposableObserver<DataListBean<NewsBean>>() {
                    @Override
                    public void onNext(DataListBean<NewsBean> newsBeanDataListBean) {
                        getView().onRequestEnd(newsBeanDataListBean, false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onRequestError(false);
                    }

                    @Override
                    public void onComplete() {
                        getView().showContent();
                    }
                }));
    }
}
