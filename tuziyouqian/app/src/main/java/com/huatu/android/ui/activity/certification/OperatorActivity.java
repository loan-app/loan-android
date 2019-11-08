package com.huatu.android.ui.activity.certification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.bean.BaseBean;
import com.huatu.android.http.RxManager;
import com.huatu.android.http.RxSchedulers;
import com.huatu.android.http.RxSubscriber;
import com.huatu.android.widget.TitleHeaderBar;
import com.lib.core.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperatorActivity extends AppCompatActivity {
  @BindView(R.id.mTitle) TitleHeaderBar mTitle;
  @BindView(R.id.phone_edit) EditText phoneEdit;
  @BindView(R.id.name_edit) EditText nameEdit;
  @BindView(R.id.number_edit) EditText numberEdit;
  @BindView(R.id.server_edit) EditText serverEdit;


  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_operator);
    ButterKnife.bind(this);

    mTitle.setLeftOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }


  @OnClick(R.id.confirm) public void confirm() {
    String phone = phoneEdit.getText().toString();
    String name = nameEdit.getText().toString();
    String number = numberEdit.getText().toString();
    String serverCode = serverEdit.getText().toString();
    if (TextUtils.isEmpty(phone)) {
      ToastUtil.showShort("请输入手机号");
      return;
    }
    if (TextUtils.isEmpty(name)) {
      ToastUtil.showShort("请输入姓名");
      return;
    }
    if (TextUtils.isEmpty(number)) {
      ToastUtil.showShort("请输入身份证号码");
      return;
    }
    if (TextUtils.isEmpty(serverCode)) {
      ToastUtil.showShort("请输入服务密码");
      return;
    }

    App.serverAPI.operatorConfirm(App.getInstance().getToken(), phone, name, number, serverCode).compose(RxSchedulers.<BaseBean>io_main())
        .subscribe(new RxSubscriber<BaseBean>(getApplicationContext(), new RxManager(), true) {
          @Override protected void onSuccess(BaseBean baseBean) {
            ToastUtil.showShort("认证成功");
            finish();
          }

          @Override protected void onFailed(String code, String msg) {
            ToastUtil.showShort(msg);
          }
        });

  }

}
