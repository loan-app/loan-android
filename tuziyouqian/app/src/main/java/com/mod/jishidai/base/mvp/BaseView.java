package com.mod.jishidai.base.mvp;


public interface BaseView {
    /*******内嵌加载*******/
    void showLoading(String title);

    void stopLoading();

    void showToast(String msg);
}