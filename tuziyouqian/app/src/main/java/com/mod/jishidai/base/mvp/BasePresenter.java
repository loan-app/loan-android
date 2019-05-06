package com.mod.jishidai.base.mvp;

import android.content.Context;

import com.mod.jishidai.http.RxManager;


public abstract class BasePresenter<M, V> {
    public Context mContext;
    public M mModel;
    public V mView;
    public RxManager mRxManage = new RxManager();

    public void setVM(V v, M m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();

    }

    public void onStart() {
    }

    public void onDestroy() {
        mRxManage.clear();
        mView = null;
    }
}
