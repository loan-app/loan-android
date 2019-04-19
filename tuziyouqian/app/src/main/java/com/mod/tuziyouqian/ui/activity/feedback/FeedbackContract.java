package com.mod.tuziyouqian.ui.activity.feedback;

import com.mod.tuziyouqian.base.mvp.BaseModel;
import com.mod.tuziyouqian.base.mvp.BasePresenter;
import com.mod.tuziyouqian.base.mvp.BaseView;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.UploadRequestBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author 周竹
 * @file FeedbackContract
 * @brief
 * @date 2018/4/30 下午3:36
 * Copyright (c) 2017
 * All rights reserved.
 */
public interface FeedbackContract {

    interface View extends BaseView {
        void showFeedbackSuccess();

    }

    interface Model extends BaseModel {
        Flowable<BaseBean> sendFeedBack(String token, String questionDesc, String questionImg);

    }

    abstract static class Presenter extends BasePresenter<Model, View> {
        public abstract void sendFeedBack( String questionDesc, List<UploadRequestBean> questionImg);
    }
}