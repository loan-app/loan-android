package com.mod.tuziyouqian.ui.activity.feedback;

import android.text.TextUtils;

import com.lib.core.utils.CollectionUtils;
import com.mod.tuziyouqian.base.App;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.UploadRequestBean;
import com.mod.tuziyouqian.http.RxSubscriber;

import java.util.List;

/**
 * Created by TMVPHelper on 2018/04/30
 */
public class FeedbackPresenter extends FeedbackContract.Presenter {

    @Override
    public void sendFeedBack( String questionDesc, List<UploadRequestBean> questionImg) {
        StringBuilder imags = new StringBuilder();
        if (!CollectionUtils.isNullOrEmpty(questionImg)) {
            for (UploadRequestBean img : questionImg) {
                if (img.isSuccess)
                    imags.append(img.url).append("|");
            }
            if (!TextUtils.isEmpty(imags))
                imags.deleteCharAt(imags.length() - 1);
        }

        mModel.sendFeedBack(App.getInstance().getToken(), questionDesc, imags.toString()).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean baseBean) {
                if (mView != null)
                    mView.showFeedbackSuccess();

            }

            @Override
            protected void onFailed(String code, String msg) {
                if (mView != null) {
                    mView.stopLoading();
                    mView.showToast(msg);
                }

            }
        });
    }
}