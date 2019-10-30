package com.huatu.android.ui.activity.feedback;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.http.RxSchedulers;

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