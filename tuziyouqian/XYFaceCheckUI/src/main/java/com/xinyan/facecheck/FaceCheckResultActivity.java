package com.xinyan.facecheck;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinyan.bioassay.sdk.R;
import com.xinyan.facecheck.lib.bean.XYResultInfo;
import com.xinyan.facecheck.lib.util.StringUtils;
import com.xinyan.facecheck.util.UiUtil;

public class FaceCheckResultActivity extends AppCompatActivity {

    private static final String TAG = "FaceCheckResultActivity";

    private TextView tv_score;
    private ProgressBar progressBar;
    private ImageView tv_progress_bottom;
    private TextView resultString;
    private TextView restartCheck;
    private ImageView resultStringImg;
    private String score = "0";
    private String desc = "";
    private XYResultInfo returnData = null;
    private ImageView resultImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_check_result);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FaceCheckResultActivity.this.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        tv_score = findViewById(R.id.tv_score);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        tv_progress_bottom = (ImageView) findViewById(R.id.tv_progress_bottom);
        resultImg = (ImageView) findViewById(R.id.resultimg);
        resultStringImg = (ImageView) findViewById(R.id.resultStringimg);
        resultString = (TextView) findViewById(R.id.resultString);
        restartCheck = (TextView) findViewById(R.id.restartCheck);
        restartCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnData != null) {
                    XinYanFaceSDK.getInstance().faceCheckReturnData(returnData);
                }
                FaceCheckResultActivity.this.finish();
            }
        });

        tv_score.post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    private void initData() {
        returnData = (XYResultInfo) getIntent().getSerializableExtra(KV.KEY_DATA);

        if (returnData != null) {
            score = returnData.getScore();

            String code = returnData.getErrorCode();
            if ("S3002".equals(code)) {
                desc = "操作频繁，请稍后重试";
            } else {
                desc = returnData.getErrorMsg();
            }

            byte[] img = returnData.getImg();
            if (img != null && img.length > 0) {
                resultImg.setImageBitmap(BitmapFactory.decodeByteArray(img, 0, img.length));
            }
            if (returnData.isSuccess()) {
                handleSuccess();
            } else {
                handleError();
            }
        } else {
            handleError();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (returnData != null) {
            XinYanFaceSDK.getInstance().faceCheckReturnData(returnData);
        }
    }

    private void handleError() {
        restartCheck.setVisibility(View.VISIBLE);
        restartCheck.setText("退出重试");
        resultString.setText(desc);
        if (score != null) {
            setProgress(score);
        } else {
            setProgress("0");
        }
        resultString.setTextColor(getResources().getColor(R.color.color_f06885));
        resultStringImg.setImageResource(R.drawable.fail);
    }

    private void handleSuccess() {
        restartCheck.setVisibility(View.VISIBLE);
        restartCheck.setText("检测完成");
        if (score != null) {
            setProgress(score);
        } else {
            setProgress("0");
        }
        resultString.setTextColor(getResources().getColor(R.color.color_861e21));
        resultStringImg.setImageResource(R.drawable.success);
        resultString.setText(desc);
    }

    private void setProgress(String scores) {
        tv_score.setVisibility(View.VISIBLE);
        tv_progress_bottom.setVisibility(View.VISIBLE);
        restartCheck.setVisibility(View.VISIBLE);
        double d = Double.parseDouble(scores);
        int score = (int) (Math.round(d));
        // 设置进度条
        progressBar.setProgress(score);
        tv_score.setText(score + "%");
        // --设置文字位置
        if (score > 0) {
            int leftTvScore =
                    progressBar.getMeasuredWidth() * score / 100 - tv_score.getMeasuredWidth() / 2
                            + UiUtil.dip2px(this, 8);
            LinearLayout.LayoutParams paramsTvScore = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsTvScore.setMargins(leftTvScore, 0, 0, 0);
            tv_score.setLayoutParams(paramsTvScore);
            // --设置图标位置
            int leftBottom =
                    progressBar.getMeasuredWidth() * score / 100 - tv_progress_bottom.getMeasuredWidth() / 2
                            + UiUtil.dip2px(this, 8);
            LinearLayout.LayoutParams paramsBottom = new LinearLayout.LayoutParams(30, 30);
            paramsBottom.setMargins(leftBottom, UiUtil.dip2px(FaceCheckResultActivity.this, 5), 0, 0);
            tv_progress_bottom.setLayoutParams(paramsBottom);
        }
    }
}
