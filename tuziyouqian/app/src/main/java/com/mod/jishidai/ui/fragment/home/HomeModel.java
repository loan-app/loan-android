package com.mod.jishidai.ui.fragment.home;

import com.mod.jishidai.base.App;
import com.mod.jishidai.bean.BalanceBean;
import com.mod.jishidai.bean.BaseBean;
import com.mod.jishidai.bean.LaunchBean;
import com.mod.jishidai.bean.OrderBean;
import com.mod.jishidai.http.RxSchedulers;

import io.reactivex.Flowable;

/**
 * Created by TMVPHelper on 2018/04/30
 */
public class HomeModel implements HomeContract.Model {

   /* @Override
    public Flowable<BaseBean<LaunchBean>> getLaunchData() {
        return App.serverAPI.getLaunchData(App.getInstance().getToken()).compose(RxSchedulers.<BaseBean<LaunchBean>>io_main());
    }*/

   /* @Override
    public Flowable<BaseBean<BalanceBean>> getBalance(String token) {
        return App.serverAPI.getBalance(token).compose(RxSchedulers.<BaseBean<BalanceBean>>io_main());
    }
*/
    @Override
    public Flowable<BaseBean<OrderBean>> getLoanData(String token) {
        return App.serverAPI.getLoanData(token).compose(RxSchedulers.<BaseBean<OrderBean>>io_main());
    }

    @Override
    public Flowable<BaseBean<OrderBean>> getOrderStatus(String token) {
        return App.serverAPI.getOrderStatus(token).compose(RxSchedulers.<BaseBean<OrderBean>>io_main());
    }
}