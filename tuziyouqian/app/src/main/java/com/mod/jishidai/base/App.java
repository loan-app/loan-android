package com.mod.jishidai.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.google.gson.GsonBuilder;
import com.lib.core.baseapp.BaseApplication;
import com.lib.core.utils.JsonUtils;
import com.lib.core.utils.LogUtils;
import com.lib.core.utils.NumberTypeAdapter;
import com.lib.core.utils.SPUtils;
import com.lib.core.utils.StringNullAdapter;
import com.mod.jishidai.BuildConfig;
import com.mod.jishidai.R;
import com.mod.jishidai.bean.SplashBean;
import com.mod.jishidai.http.MyInterceptor;
import com.mod.jishidai.http.ServerAPI;
import com.mod.jishidai.utils.AppConfig;
import com.moxie.client.manager.MoxieSDK;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.xinyan.facecheck.XinYanFaceSDK;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import ocr.android.xinyan.com.xinyan_android_ocr_sdk.XinyanOCRSDK;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends BaseApplication {
    private static App mApp;
    public static ServerAPI serverAPI;
    //! 当前设备唯一标识

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        LogUtils.logInit(BuildConfig.LOG_DEBUG);
        initApiService();
        /*initUM();*/
        MoxieSDK.init(this);//摩羯座
        /**
         * 初始化中各参数为必填参数，否则无法正常使用SDK功能；
         * @param context 上下文
         * @param xyOCRLicense  license
         * @param xyOCRMemberId   商户号 8150728867
         * @param xyOCRTerminalId  终端号
         * @param openRiskRecognition 是否开启风险提示
         */
        XinyanOCRSDK.getInstents().setDebug(true);
        XinyanOCRSDK.getInstents().init(this, AppConfig.LICENSE, AppConfig.MEMBER_ID, AppConfig.TERMINAL_ID, false);
        /**
         * 初始化库功能
         * @param context 上下文
         * @param memberId 商户号
         * @param terminalId 终端号
         * @param license 商户license信息
         * @param playSound 是否播放活体检测声音
         */
        XinYanFaceSDK.getInstance().init(this, AppConfig.MEMBER_ID, AppConfig.TERMINAL_ID,  AppConfig.LICENSE,true);
        XinYanFaceSDK.getInstance().setDebug(true);
    }


    private void initUM() {
        //友盟统计
        UMConfigure.setLogEnabled(true);
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "73abd58e3566d3393ee9b185ccfb96f5");
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                SPUtils.saveStringData(SPUtils.DEVICEID,deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        PushAgent.getInstance(this).onAppStart();
    }


    private void initApiService() {
        MyInterceptor interceptor = new MyInterceptor();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)  //设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(20, TimeUnit.SECONDS)//设置写的超时时间
                .retryOnConnectionFailure(true)//错误重连
                .addInterceptor(interceptor);
        OkHttpClient mClient = builder.build();
        GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
        gsonBuilder.registerTypeAdapter(String.class, new StringNullAdapter());
        gsonBuilder.registerTypeAdapter(int.class, new NumberTypeAdapter());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();
        serverAPI = retrofit.create(ServerAPI.class);

    }


    public static App getInstance() {
        return mApp;
    }

    public static String getResText(int strId) {
        return getAppResources().getString(strId);
    }

    public static int getResColor(int colorId) {
        return ContextCompat.getColor(App.getInstance(), colorId);
    }


    /**
     * @return String 版本号
     * @brief 获得当前应用版本号
     */
    public String getVersionCode() {
        return BuildConfig.VERSION_CODE + "";
    }

    /**
     * @return String 版本号
     * @brief 获得当前应用版本号
     */
    public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * @return brand系统厂商
     * @brief 获得该手机系统的定制产商名称
     */
    public String getPhoneBrand() {
        return Build.BRAND;
    }

    /**
     * @return
     * @brief 获得手机型号
     */
    public String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * @return String 系统版本号
     * @brief 获得当前系统版本号
     */
    public String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return String 当前设备ID
     * @brief 获得设备ID
     */
    public String getDeviceId() {
        String deviceId = SPUtils.getStringData(SPUtils.DEVICEID, null);
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getUniqueMarker();
            SPUtils.saveStringData(SPUtils.DEVICEID, deviceId);
            return deviceId;
        }
        return deviceId;
    }

    @SuppressLint("MissingPermission")
    private String getUniqueMarker() {
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice, tmSerial, androidId;
        tmDevice = tm.getDeviceId();
        tmSerial = tm.getSimSerialNumber();
        androidId = Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = null;
        String uniqueId = null;
        if (!TextUtils.isEmpty(androidId)) {
            deviceUuid = new UUID(androidId.hashCode(),
                    ((long) (tmDevice == null ? 0 : tmDevice.hashCode() << 32) |
                            (tmSerial == null ? 100 : tmSerial.hashCode())));
            uniqueId = deviceUuid.toString();
        } else {
            deviceUuid = UUID.randomUUID();
            uniqueId = deviceUuid.toString();
        }
        return uniqueId;
    }

    /**
     * @return String 用户登录的账号  手机号
     * @brief 获得用户登录密码, 默认为null
     */
    public String getLoginPhone() {
        String phone = SPUtils.getStringData(SPUtils.PHONE, null);
        return phone;
    }

    public void saveLoginPhone(String phone) {
        SPUtils.saveStringData(SPUtils.PHONE, phone);
    }

    /**
     * @return String 用户token
     * @brief 获得用户token, 默认为null
     */
    public String getToken() {
        String token = SPUtils.getStringData(SPUtils.TOKEN, null);
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiIxODEyOTAiLCJwaG9uZSI6IjE1Mjc5MzM4NjI1IiwiY2xpZW50VHlwZSI6ImFuZHJvaWQiLCJjbGllbnRBbGlhcyI6Imh1YXFpYW5ndWFuIiwiY2xpZW50VmVyc2lvbiI6IjEuMC4xX2RlYnVnIiwiaWF0IjoxNTU3MDY2Mzk0fQ.PlAugg58a3P9b86RGccm8vMUwS0Up6UsrBTCe7vQRuY";
    }

    public void saveToken(String token) {
        SPUtils.saveStringData(SPUtils.TOKEN, token);

    }

    public void saveUid(String uid) {
        SPUtils.saveStringData(SPUtils.USER_UD, uid);

    }

    public void saveSplash(SplashBean bean) {
        SPUtils.saveStringData(SPUtils.PHONE_SPLASH, JsonUtils.bean2json(bean));
    }

    public boolean isPhoneStart() {
        return SPUtils.getBooleanData(SPUtils.PHONE_START, false);
    }

    public void setPhoneStart(boolean phoneStart) {
        SPUtils.saveBooleanData(SPUtils.PHONE_START, phoneStart);
    }

    public SplashBean getSplash() {
        String splash = SPUtils.getStringData(SPUtils.PHONE_SPLASH, null);
        if(splash == null)
            return null;
        return JsonUtils.fromJsonForType(SPUtils.getStringData(SPUtils.PHONE_SPLASH, ""),SplashBean.class);
    }

    public String getUid() {
        String uid = SPUtils.getStringData(SPUtils.USER_UD, null);
        return uid;
    }

    /**
     * @return String 用户登录密码  MD5加密
     * @brief 获得用户登录密码, 默认为null
     */
    public String getLoginPassword() {
        String mPwd = SPUtils.getStringData(SPUtils.PASSWORD, null);
        return mPwd;
    }

    public void saveLoginPassword(String password) {
        SPUtils.saveStringData(SPUtils.PASSWORD, password);
    }

    /**
     * 退出登录
     */
    public static void logOut() {
        SPUtils.removeStringData(SPUtils.PASSWORD);
        SPUtils.removeStringData(SPUtils.TOKEN);
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.color_f5f5f5, R.color.color_333333);//全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }
}
