package com.mod.tuziyouqian.http;

import com.mod.tuziyouqian.BuildConfig;
import com.mod.tuziyouqian.bean.BalanceBean;
import com.mod.tuziyouqian.bean.BaseBean;
import com.mod.tuziyouqian.bean.LaunchBean;
import com.mod.tuziyouqian.bean.OrderBean;
import com.mod.tuziyouqian.bean.PersonInfoBean;
import com.mod.tuziyouqian.bean.PhoneBean;
import com.mod.tuziyouqian.bean.RealNameBean;
import com.mod.tuziyouqian.bean.SplashBean;
import com.mod.tuziyouqian.bean.TokenBean;
import com.mod.tuziyouqian.bean.UploadBean;
import com.mod.tuziyouqian.bean.VersionBean;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @author 周竹
 * @file ServerAPI
 * @brief
 * @date 2017/12/2 下午7:01
 * All rights reserved.
 */

public interface ServerAPI {
    public static final String BASE_URL = BuildConfig.API_HOST;

    /**
     * 检测是否注册
     *
     * @param phone
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_judge_register")
    Flowable<BaseBean> judgeRegister(@Field("phone") String phone);

    /**
     * 获取验证码
     *
     * @param phone
     * @param graph_code
     * @param sms_type
     * @return
     */
    @FormUrlEncoded
    @POST("user/mobile_code")
    Flowable<BaseBean> getVerificationCode(@Field("phone") String phone, @Field("graph_code") String graph_code, @Field("sms_type") String sms_type);

    /**
     * 注册
     *
     * @param phone
     * @param password
     * @param phone_code
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_register")
    Flowable<BaseBean> register(@Field("phone") String phone, @Field("password") String password, @Field("phone_code") String phone_code, @Field("origin") String origin);

    /**
     * 登录
     *
     * @param phone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_login")
    Flowable<BaseBean<TokenBean>> login(@Field("phone") String phone, @Field("password") String password);

    /**
     * 修改密码
     *
     * @param phone
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_update_pwd")
    Flowable<BaseBean> updatePwd(@Field("phone") String phone, @Field("password") String password, @Field("phone_code") String phone_code);


    /**
     * 获取个人信息
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_info")
    Flowable<BaseBean<PersonInfoBean>> getUserInfo(@Field("token") String token);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_loginout")
    Flowable<BaseBean> logOut(@Field("token") String token);

    /**
     * 获取实名信息
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("user/real_name_info")
    Flowable<BaseBean<RealNameBean>> getRealNameInfo(@Field("token") String token);

    /**
     * 图片文件上传
     *
     * @param token
     * @param file
     * @return
     */
    @Multipart
    @POST("upload")
    Flowable<BaseBean<UploadBean>> uploadFile(@Part("token") RequestBody token, @Part MultipartBody.Part file);




    /**
     * 提交实名认证
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("user/real_name_save")
    Flowable<BaseBean> saveRealNameInfo(@Field("token") String token, @Field("userName") String userName
            , @Field("userCertNo") String userCertNo, @Field("ia") String ia
            , @Field("indate") String indate, @Field("address") String address
            , @Field("nation") String nation, @Field("imgCertFront") String imgCertFront
            , @Field("imgCertBack") String imgCertBack);

    /**
     * 提交个人信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_info_save")
    Flowable<BaseBean<RealNameBean>> saveUserInfo(@FieldMap Map<String, String> map);

    /**
     * 上传通讯录
     *
     * @param token
     * @param addressList
     * @return
     */
    @FormUrlEncoded
    @POST("user/address_list")
    Flowable<BaseBean> uploadContact(@Field("token") String token, @Field("addressList") String addressList);

    /**
     * 上传用户硬件信息
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_device")
    Flowable<BaseBean> uploadDevice(@FieldMap Map<String, String> map);

    /**
     * 问题反馈
     *
     * @param token
     * @param questionDesc
     * @param questionImg
     * @return
     */
    @FormUrlEncoded
    @POST("user/feedback")
    Flowable<BaseBean> sendFeedback(@Field("token") String token, @Field("questionDesc") String questionDesc, @Field("questionImg") String questionImg);

    /**
     * 获取APP启动数据  包括启动页图片，首页轮播图和首页公告
     *
     * @return
     */
    @FormUrlEncoded
    @POST("app_index")
    Flowable<BaseBean<LaunchBean>> getLaunchData(@Field("token") String token);

    /**
     * 首页金额和期限
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_balance")
    Flowable<BaseBean<BalanceBean>> getBalance(@Field("token") String token);
/*

    */
/**
     * 上传人脸检测照片
     *
     * @param token
     * @param imgFace
     * @return
     *//*

    @FormUrlEncoded
    @POST("user/face_check")
    Flowable<BaseBean> uploadFaceCheck(@Field("token") String token, @Field("imgFace") String imgFace);
*/

    /**
     * 图片文件上传
     *
     * @param token
     * @param file
     * @return
     */
    @Multipart
    @POST("user/face_save")
    Flowable<BaseBean> saveSace(@Part("token") RequestBody token, @Part MultipartBody.Part file);

    /**
     * 版本更新
     *
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("check_version")
    Flowable<BaseBean<VersionBean>> checkVersion(@Field("token") String token);

    /**
     * 查找手机价格
     *
     * @param model
     * @param memory
     * @return
     */
    @FormUrlEncoded
    @POST("phone/phone_search")
    Flowable<BaseBean<PhoneBean>> searchPhone(@Field("model") String model, @Field("memory") String memory);

    @FormUrlEncoded
    @POST("order/loan_home")
    Flowable<BaseBean<OrderBean>> getLoanData(@Field("token") String token);

    @FormUrlEncoded
/*    @POST("order/current_order")*///回收
    @POST("order/loan_current_order")//现金贷
    Flowable<BaseBean<OrderBean>> getOrderStatus(@Field("token") String token);

    @FormUrlEncoded
    @POST("app_home")
    Flowable<BaseBean<SplashBean>> SplashBean(@Field("token") String token);
}
