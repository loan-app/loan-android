package com.xinyan.facecheck;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinyan.bioassay.sdk.R;
import com.xinyan.facecheck.lib.bean.XYResultInfo;
import com.xinyan.facecheck.lib.util.IdCardUtils;
import com.xinyan.facecheck.lib.util.IdNameUtils;
import com.xinyan.facecheck.lib.util.RegexUtils;

public class FaceCheckIDActivity extends AppCompatActivity {
  private EditText userName;
  private EditText userId;
  private TextView next;
  private boolean canEdit = true;
  private ImageView nameClear;
  private ImageView idcCardClear;
  private BroadcastReceiver receiver;
  public static String ACTION_CLOSE = "com.xinyan.closeActivty";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_face_check_id);

    initView();
    initDate();
    setListener();
  }


  private void initView() {
    userName = (EditText) findViewById(R.id.userName);
    userId = (EditText) findViewById(R.id.userId);
    next = (TextView) findViewById(R.id.nextState);
    nameClear = (ImageView) findViewById(R.id.name_clear);
    idcCardClear = (ImageView) findViewById(R.id.idcard_clear);
    canEdit = getIntent().getBooleanExtra("canEdit", true);

    findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        handleData();
      }
    });
  }

  private void initDate() {
    LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
    IntentFilter filter = new IntentFilter();
    filter.addAction(ACTION_CLOSE);

    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_CLOSE)) {
          if (!isFinishing()) {
            FaceCheckIDActivity.this.finish();
          }
        }
      }
    };
    broadcastManager.registerReceiver(receiver, filter);

    if (canEdit) {
      userName.addTextChangedListener(textWatcher);
      userId.addTextChangedListener(textWatcher);
      userName.setOnFocusChangeListener(onFocusChangeListener);
      userId.setOnFocusChangeListener(onFocusChangeListener);
      userId.setEnabled(true);
      userName.setEnabled(true);
    } else {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      userId.setEnabled(false);
      userName.setEnabled(false);
    }
    String idNo = getIntent().getStringExtra(KV.KEY_ID);
    String idName = getIntent().getStringExtra(KV.KEY_NAME);
    if (!TextUtils.isEmpty(idNo)) {
      userId.setText(idNo);
      if (idNo.length() <= 18) {
        userId.setSelection(idNo.length());
      } else {
        userId.setSelection(18);
      }
    }
    if (!TextUtils.isEmpty(idName)) {
      userName.setText(idName);
      if (idName.length() <= 32) {
        userName.setSelection(idName.length());
      } else {
        userName.setSelection(32);
      }
    }
  }

  private void setListener() {
    next.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startCheck();
      }
    });
    nameClear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userName.setText("");
      }
    });
    idcCardClear.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userId.setText("");
      }
    });
  }

  private void startCheck() {

    if (loginCheck(userName.getText().toString(), userId.getText().toString())) {

      Intent intent = new Intent(FaceCheckIDActivity.this, FaceCheckStart.class);
      intent.putExtra("canEdit", canEdit);
      intent.putExtra(KV.KEY_NAME, userName.getText().toString());
      intent.putExtra(KV.KEY_ID, userId.getText().toString());
      intent.putExtra(KV.KEY_TRANSID, getIntent().getStringExtra(KV.KEY_TRANSID));
      startActivity(intent);
      finish();
    }
  }

  /**
   * 录入校验
   */
  private boolean loginCheck(String idName, String idNo) {
    if (TextUtils.isEmpty(idName)) {
      ToastUtil.showToast(FaceCheckIDActivity.this, "请输入姓名");
      return false;
    }
    if (!IdNameUtils.isValidatedName(idName)) {
      ToastUtil.showToast(FaceCheckIDActivity.this, "输入的姓名格式有误");
      return false;
    }

    if (TextUtils.isEmpty(idNo)) {
      ToastUtil.showToast(FaceCheckIDActivity.this, "请输入身份证号码");
      return false;
    }
    if (!RegexUtils.isIDCard15(idNo)
            || !IdCardUtils.isValidatedAllIdcard(idNo)) {
      ToastUtil.showToast(FaceCheckIDActivity.this, "输入的身份证号码格式有误");
      return false;
    }
    return true;
  }

  private TextWatcherAdapter textWatcher = new TextWatcherAdapter();


  private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
      if (v == userName) {
        if (!hasFocus) {
          nameClear.setVisibility(View.INVISIBLE);
        } else {
          if (!TextUtils.isEmpty(userName.getText().toString())) {
            nameClear.setVisibility(View.VISIBLE);
          }
        }
      } else if (v == userId) {
        if (!hasFocus) {
          idcCardClear.setVisibility(View.INVISIBLE);
        } else {
          if (!TextUtils.isEmpty(userId.getText().toString())) {
            idcCardClear.setVisibility(View.VISIBLE);
          }
        }
      }
    }
  };

  // 是否启动登录按钮，字符监听者
  private class TextWatcherAdapter implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
      String username = userName.getText().toString();
      String password = userId.getText().toString();
      if (!TextUtils.isEmpty(username) && userName.hasFocus()) {
        nameClear.setVisibility(View.VISIBLE);
      } else if (!TextUtils.isEmpty(password) && userId.hasFocus()) {
        idcCardClear.setVisibility(View.VISIBLE);
      } else {
        nameClear.setVisibility(View.INVISIBLE);
        idcCardClear.setVisibility(View.INVISIBLE);
      }
    }
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      handleData();
    }
    return super.onKeyDown(keyCode, event);
  }

  private void handleData() {
    XYResultInfo faceCheckInfo = new XYResultInfo();
    faceCheckInfo.setTransId(XinYanFaceSDK.getInstance().getTransId());
    faceCheckInfo.setErrorCode("C0000");
    faceCheckInfo.setSuccess(false);
    faceCheckInfo.setFee("N");
    faceCheckInfo.setScore("0");
    faceCheckInfo.setErrorMsg("用户取消");
    faceCheckInfo.setIdNo(userId.getText().toString());
    faceCheckInfo.setIdName(userName.getText().toString());

    XinYanFaceSDK.getInstance().faceCheckReturnData(faceCheckInfo);
    this.finish();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
  }
}
