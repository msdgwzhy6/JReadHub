package com.jeez.guanpj.jreadhub.mvpframe.view.lce;

import com.jeez.guanpj.jreadhub.mvpframe.view.IBaseMvpView;

public interface IBaseMvpLceView<M> extends IBaseMvpView {

    /**
     * 显示加载中页面
     */
    void showLoading(boolean isPullToRefresh);

    /**
     * 显示内容页面
     */
    void showContent();

    /**
     * 显示异常界面
     */
    void showError();

    /**
     * 绑定数据
     */
    void bindData(M data);

    /**
     * 加载数据
     */
    void loadData(boolean isPullToRefresh);
}
