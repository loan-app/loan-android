package com.mod.jishidai.ui.activity.main;

import com.lib.core.utils.AppUtils;
import com.lib.core.utils.DisplayUtil;
import com.lib.core.utils.NetWorkUtils;
import com.lib.core.utils.SPUtils;
import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.VersionBean;
import com.mod.jishidai.http.RxSubscriber;

import java.util.HashMap;

/**
 * Created by TMVPHelper on 2018/04/29
 */
public class MainPresenter extends MainContract.Presenter {

   /* @Override
    public void uploadDevice() {
        HashMap<String, String> map = new HashMap<>();
        map.put("token", App.getInstance().getToken());
        map.put("deviceid", SPUtils.getStringData(SPUtils.DEVICEID, ""));
        map.put("netType", NetWorkUtils.getNetworkType());
        map.put("phoneBrand", App.getInstance().getPhoneBrand());
        map.put("phoneModel", App.getInstance().getPhoneModel());
        map.put("phoneSystem", App.getInstance().getOSVersion());
        map.put("phoneResolution", DisplayUtil.getScreenWidth(mContext) + "*" + DisplayUtil.getScreenHeight(mContext));
        map.put("phoneMemory", AppUtils.getRomTotalSize());
        mModel.uploadDevice(map).subscribe(new RxSubscriber<BaseBean>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean baseBean) {

            }

            @Override
            protected void onFailed(String code, String msg) {

            }
        });
    }

    @Override
    public void checkVersion() {
        mModel.checkVersion(App.getInstance().getToken()).subscribe(new RxSubscriber<BaseBean<VersionBean>>(mContext, mRxManage, false) {
            @Override
            protected void onSuccess(BaseBean<VersionBean> bean) {
                if (mView != null) {
                    if (bean != null && bean.data != null) {
                        if (bean.data.versionCode > Integer.valueOf(App.getInstance().getVersionCode())) {
                            mView.showUpdateDialog(bean.data);
                        }

                    }
                }

            }

            @Override
            protected void onFailed(String code, String msg) {

            }
        });

    }*/


}