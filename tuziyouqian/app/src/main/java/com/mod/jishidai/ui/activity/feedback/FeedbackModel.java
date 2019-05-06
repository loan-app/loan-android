package com.mod.jishidai.ui.activity.feedback;

import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.http.RxSchedulers;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/30
 */
public class FeedbackModel implements FeedbackContract.Model {

    @Override
    public Flowable<BaseBean> sendFeedBack(String token, String questionDesc, String questionImg) {
        return App.serverAPI.sendFeedback(token, questionDesc, questionImg).compose(RxSchedulers.<BaseBean>io_main());
    }
}