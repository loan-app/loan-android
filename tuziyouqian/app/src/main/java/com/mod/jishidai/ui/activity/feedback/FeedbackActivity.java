package com.mod.jishidai.ui.activity.feedback;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyco.animation.ZoomEnter.ZoomInEnter;
import com.flyco.animation.ZoomExit.ZoomInExit;
import com.gyf.barlibrary.OnKeyboardListener;
import com.lib.core.utils.DisplayUtil;
import com.lib.core.utils.NetWorkUtils;
import com.lib.core.utils.OnItemClickListener;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mod.jishidai.R;
import com.mod.jishidai.base.App;
import com.mod.jishidai.base.BaseActivity;
import com.mod.jishidai.bean.UploadBean;
import com.mod.jishidai.bean.UploadRequestBean;
import com.mod.jishidai.utils.UploadManage;
import com.mod.jishidai.widget.TitleHeaderBar;
import com.mod.jishidai.widget.dialog.NormalTitleAlert;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file FeedbackActivity
 * @brief
 * @date 2018/4/30 上午11:26
 * Copyright (c) 2017
 * All rights reserved.
 */
public class FeedbackActivity extends BaseActivity<FeedbackPresenter, FeedbackModel> implements FeedbackContract.View {
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.etFeedback)
    EditText etFeedback;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.mLinear)
    LinearLayout mLinear;
    @BindView(R.id.mPhotoView)
    RecyclerView mPhotoView;
    @BindView(R.id.btnAffirm)
    Button btnAffirm;
    @BindView(R.id.mScrollView)
    ScrollView mScrollView;
    private PhotoAdapter photoAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    List<UploadRequestBean> mUploadRequests;//保存上传成功后图片地址
    Handler handler = new Handler();

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    public void initView() {
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    tvLimit.setText(toDBC(s.toString()).length() + "/200");
                } else {
                    tvLimit.setText("0/200");
                }

            }
        });
        mImmersionBar.setOnKeyboardListener(new OnKeyboardListener() {
            @Override
            public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                if (isPopup) {
                    int[] mLocation = new int[2];
                    mLinear.getLocationOnScreen(mLocation);
                    int mLocationY = mLocation[1] + mLinear.getHeight();//输入框布局底部在屏幕上的Y坐标
                    int keyBoardY = DisplayUtil.getScreenHeight(FeedbackActivity.this) - keyboardHeight;//键盘弹起时候顶部Y坐标
                    int scrollSpace = 0;
                    if (mLocationY > keyBoardY) {
                        scrollSpace = mLocationY - keyBoardY;
                    } else {
                        scrollSpace = -(DisplayUtil.getScreenHeight(FeedbackActivity.this) - keyboardHeight - mLocationY);

                    }
                    scrollToBottom(scrollSpace);
                } else {
                    scrollToBottom(0);
                }

            }
        });
        photoAdapter = new PhotoAdapter(this, selectList);
        mPhotoView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        mPhotoView.setAdapter(photoAdapter);
        photoAdapter.setOnItemClickListener(new OnItemClickListener<LocalMedia>() {
            @Override
            public void onItemClick(View v, int position, LocalMedia media) {
                if (media == null) {
                    checkPermissions();
                } else {
                    if (selectList.size() > 0) {
                        PictureSelector.create(FeedbackActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                    }
                }

            }
        });
    }

    private void scrollToBottom(final int y) {

        mScrollView.postDelayed(new Runnable() {

            @Override
            public void run() {
                mScrollView.smoothScrollBy(0, y);
            }
        }, 100);

    }

    @OnClick({R.id.btnAffirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnAffirm:
                commit();
                break;
        }
    }

    private void commit() {
        if (!NetWorkUtils.isNetConnected(this)) {
            ToastUtil.showShort(App.getResText(R.string.no_net));
            return;
        }
        if (TextUtils.isEmpty(etFeedback.getText())) {
            ToastUtil.showShort("请填写问题描述");
            return;
        }
        startProgressDialog();
        if (!selectList.isEmpty()) {
            Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
            String[] paths = new String[selectList.size()];
            for (int i = 0; i < selectList.size(); i++) {
                paths[i] = selectList.get(i).getPath();
            }
            Tiny.getInstance().source(paths).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
                @Override
                public void callback(boolean isSuccess, String[] outfile, Throwable t) {
                    if (isSuccess && outfile != null && outfile.length > 0) {
                        upload(outfile);
                    } else {
                        stopProgressDialog();
                        ToastUtil.showShort("图片压缩失败");
                    }
                }
            });
        } else {
            mPresenter.sendFeedBack(etFeedback.getText().toString().trim(), null);

        }
    }

    private void upload(String[] outfile) {
        mUploadRequests = new ArrayList<>();
        for (int i = 0; i < outfile.length; i++) {
            UploadRequestBean requestBean = new UploadRequestBean(null, false, false);
            mUploadRequests.add(requestBean);
            UploadManage.upload(mRxManager, this, outfile[i], new UploadManage.UploadListener() {
                @Override
                public void onUploadSuccess(UploadBean bean) {
                    requestBean.url = bean.url;
                    requestBean.isComplete = true;
                    requestBean.isSuccess = true;
                    if (checkRequests(mUploadRequests))
                        mPresenter.sendFeedBack(etFeedback.getText().toString().trim(), mUploadRequests);
                }

                @Override
                public void onFailed(String code, String msg) {
                    requestBean.isComplete = true;
                    requestBean.isSuccess = false;
                    if (checkRequests(mUploadRequests))
                        mPresenter.sendFeedBack(etFeedback.getText().toString().trim(), mUploadRequests);
                }
            });
        }

    }

    private boolean checkRequests(List<UploadRequestBean> mCosRequests) {
        int num = 0;
        for (UploadRequestBean bean : mCosRequests) {
            if (bean.isComplete)
                num++;
        }
        if (num == mCosRequests.size()) {
            return true;
        }
        return false;
    }

    private void checkPermissions() {
        PermissionUtils.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                toPictureSelector();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(FeedbackActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, 1000);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(FeedbackActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, 1000);
            }
        });

    }

    private void toPictureSelector() {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(FeedbackActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(PhotoAdapter.MAX)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .isCamera(false)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .isGif(false)// 是否显示gif图片
                .openClickSound(true)// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    public void showFeedbackSuccess() {
        stopProgressDialog();
        NormalTitleAlert alert = new NormalTitleAlert(this);
        alert.content(getString(R.string.feedback_reminding));
        alert.title(getString(R.string.feedback_success));
        alert.showAnim(new ZoomInEnter()).dismissAnim(new ZoomInExit()).show();
        alert.setCanceledOnTouchOutside(false);
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
            }
        });

    }

    @Override
    public void showLoading(String title) {
        startProgressDialog(title);

    }

    @Override
    public void stopLoading() {
        stopProgressDialog();

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showShort(msg);
        NormalTitleAlert alert = new NormalTitleAlert(this);
        alert.content(getString(R.string.feedback_reminding));
        alert.title(getString(R.string.feedback_success));
        alert.showAnim(new ZoomInEnter()).dismissAnim(new ZoomInExit()).show();
        alert.setCanceledOnTouchOutside(false);
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 500);
            }
        });

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;

        }
    }

    /**
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000:
                PermissionUtils.onRequestPermissionResult(this, Manifest.permission.READ_EXTERNAL_STORAGE, grantResults, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        toPictureSelector();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        PermissionUtils.showDialog(FeedbackActivity.this, "请在权限管理中开启存储权限", 150);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.showDialog(FeedbackActivity.this, "请在权限管理中开启存储权限", 150);
                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    photoAdapter.setList(selectList);
                    photoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
