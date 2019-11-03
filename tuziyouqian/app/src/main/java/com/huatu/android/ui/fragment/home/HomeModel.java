package com.huatu.android.ui.fragment.home;

import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.bean.CerBean;
import com.huatu.android.bean.OrderBean;
import com.huatu.android.http.RxSchedulers;

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

  @Override
  public Flowable<BaseBean<CerBean>> getCerState(String token) {
    return App.serverAPI.getCerState(token).compose(RxSchedulers.<BaseBean<CerBean>>io_main());
  }
}