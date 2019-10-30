package com.huatu.android.ui.activity.certification;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.lib.core.utils.CollectionUtils;
import com.lib.core.utils.JsonUtils;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.huatu.android.R;
import com.huatu.android.base.App;
import com.huatu.android.base.BaseFragment;
import com.huatu.android.bean.ContactsInfo;
import com.huatu.android.bean.PersonInfoBean;
import com.huatu.android.bean.ProvinceBean;
import com.huatu.android.bean.RealNameBean;
import com.huatu.android.ui.activity.contact.ContactActivity;
import com.huatu.android.widget.CommonItem;
import com.huatu.android.widget.TitleHeaderBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 周竹
 * @file PersonalInfoFragment
 * @brief
 * @date 2018/4/25 下午7:52
 * Copyright (c) 2017
 * All rights reserved.
 */
public class PersonalInfoFragment extends BaseFragment<CertificatPresenter, CertificatModel> implements CertificatContract.View {
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.mCiEducation)
    CommonItem mCiEducation;
    @BindView(R.id.mProvince)
    CommonItem mProvince;
    @BindView(R.id.mEtAddress)
    EditText mEtAddress;
    @BindView(R.id.mCiLiveTime)
    CommonItem mCiLiveTime;
    @BindView(R.id.mCiLiveMarry)
    CommonItem mCiLiveMarry;
    @BindView(R.id.mCiWorkType)
    CommonItem mCiWorkType;
    @BindView(R.id.mEtCompany)
    EditText mEtCompany;
    @BindView(R.id.mEtCompanyAddress)
    EditText mEtCompanyAddress;
    @BindView(R.id.mCiDirectName)
    CommonItem mCiDirectName;
    @BindView(R.id.mCiDirectContact)
    CommonItem mCiDirectContact;
    @BindView(R.id.mCiOthersName)
    CommonItem mCiOthersName;
    @BindView(R.id.mCiOthersContact)
    CommonItem mCiOthersContact;
    List<String> mEducations;
    List<ProvinceBean> provinceBeans;
    ArrayList<ArrayList<String>> mCityList = new ArrayList<>();//该省的城市列表（第二级）
    ArrayList<ArrayList<ArrayList<String>>> mAreaList = new ArrayList<>();//该省的所有地区列表（第三极）
    List<String> liveTimes;
    List<String> liveMarrys;
    List<String> workTypes;
    List<String> directContactNames;
    List<String> othersContactNames;
    OptionsPickerView mOneSelectPicker;
    int pickerType = -1;
    String education;
    String liveProvince;
    String liveCity;
    String liveDistrict;
    String liveTime;
    String liveMarry;
    String workType;
    String directContactName;
    String othersContactName;
    String contactName1;
    String contactName2;
    String contactPhoneNum1;
    String contactPhoneNum2;


    public static PersonalInfoFragment newInstance() {
        Bundle args = new Bundle();
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal_info;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    protected void initView() {
        mPresenter.getUserInfo();
        mTitle.setCustomizedRightString("提交");
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _mActivity.finish();
            }
        });
        mTitle.setRightOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo();

            }
        });
        mOneSelectPicker = new OptionsPickerBuilder(_mActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                initOneSelectPickerData(pickerType, options1);

            }
        })
                .setContentTextSize(18)//设置滚轮文字大小
                .setSubCalSize(18)
                .setDividerColor(Color.parseColor("#cbcbcb"))//设置分割线的颜色
                .setBgColor(Color.parseColor("#ffffff"))
                .setTitleBgColor(Color.parseColor("#ffffff"))
                .setTitleColor(Color.parseColor("#ffffff"))
                .setTextColorCenter(Color.parseColor("#323232"))
                .setLineSpacingMultiplier(1.7f)
                .build();
        Resources mResources = App.getInstance().getResources();
        mEducations = Arrays.asList(mResources.getStringArray(R.array.education));
        provinceBeans = JsonUtils.json2List(JsonUtils.getJson("province.json"), ProvinceBean.class);
        initAreaList(provinceBeans);
        liveTimes = Arrays.asList(mResources.getStringArray(R.array.liveTime));
        liveMarrys = Arrays.asList(mResources.getStringArray(R.array.liveMarrys));
        workTypes = Arrays.asList(mResources.getStringArray(R.array.workTypes));
        directContactNames = Arrays.asList(mResources.getStringArray(R.array.directContactNames));
        othersContactNames = Arrays.asList(mResources.getStringArray(R.array.othersContactNames));
    }

    private void saveUserInfo() {
        /**
         *  String education;
         String liveProvince;
         String liveCity;
         String liveDistrict;
         String liveTime;
         String liveMarry;
         String workType;
         String directContactName;
         String othersContactName;
         String contactName1;
         String contactName2;
         String contactPhoneNum1;
         String contactPhoneNum2;
         */
        if (TextUtils.isEmpty(education)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(mEtAddress.getText())) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(mEtCompanyAddress.getText())) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(mEtCompany.getText())) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(liveProvince)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(liveCity)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(liveDistrict)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(liveTime)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(liveMarry)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(workType)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(directContactName)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(othersContactName)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(contactName1)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(contactName2)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(contactPhoneNum1)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (TextUtils.isEmpty(contactPhoneNum2)) {
            ToastUtil.showShort("请完善信息");
            return;
        }
        if (contactPhoneNum1.equals(contactPhoneNum2)) {
            ToastUtil.showShort("请勿选择同一人！");
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("token", App.getInstance().getToken());
        map.put("education", education);
        map.put("liveProvince", liveProvince);
        map.put("liveCity", liveCity);
        map.put("liveDistrict", liveDistrict);
        map.put("liveAddress", mEtAddress.getText().toString());
        map.put("liveTime", liveTime);
        map.put("liveMarry", liveMarry);
        map.put("workType", workType);
        map.put("workCompany", mEtCompany.getText().toString());
        map.put("workAddress", mEtCompanyAddress.getText().toString());
        map.put("directContact", directContactName);
        map.put("directContactName", contactName1);
        map.put("directContactPhone", contactPhoneNum1);
        map.put("othersContact", othersContactName);
        map.put("othersContactName", contactName2);
        map.put("othersContactPhone", contactPhoneNum2);
        mPresenter.saveUserInfo(map);

    }

    private void initAreaList(List<ProvinceBean> jsonBean) {
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).name;
                CityList.add(cityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (CollectionUtils.isNullOrEmpty(jsonBean.get(i).getCityList().get(c).area)) {
                    City_AreaList.add("");
                } else {
                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).area.size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).area.get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            mCityList.add(CityList);

            /**
             * 添加地区数据
             */

            mAreaList.add(Province_AreaList);
        }
    }


    private void showProvincePicker() {

        OptionsPickerView pvOptions = new OptionsPickerBuilder(_mActivity, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                liveProvince = provinceBeans.get(options1).name;
                liveCity = mCityList.get(options1).get(options2);
                liveDistrict = mAreaList.get(options1).get(options2).get(options3);
                mProvince.setContent(liveProvince + " " + liveCity + " " + liveDistrict);
            }
        }).setContentTextSize(18)//设置滚轮文字大小
                .setSubCalSize(18)
                .setDividerColor(Color.parseColor("#cbcbcb"))//设置分割线的颜色
                .setBgColor(Color.parseColor("#ffffff"))
                .setTitleBgColor(Color.parseColor("#ffffff"))
                .setTitleColor(Color.parseColor("#ffffff"))
                .setTextColorCenter(Color.parseColor("#323232"))
                .setLineSpacingMultiplier(1.7f)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(provinceBeans, mCityList, mAreaList);//三级选择器
        pvOptions.show();
    }

    @OnClick({R.id.mCiEducation, R.id.mProvince, R.id.mCiLiveTime, R.id.mCiLiveMarry, R.id.mCiWorkType, R.id.mCiDirectName, R.id.mCiDirectContact, R.id.mCiOthersName, R.id.mCiOthersContact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mCiEducation:
                showPickerView(0);
                break;
            case R.id.mProvince:
                showProvincePicker();
                break;
            case R.id.mCiLiveTime:
                showPickerView(1);
                break;
            case R.id.mCiLiveMarry:
                showPickerView(2);
                break;
            case R.id.mCiWorkType:
                showPickerView(3);
                break;
            case R.id.mCiDirectName:
                showPickerView(4);
                break;
            case R.id.mCiDirectContact:
                getContacts(1001);
                break;
            case R.id.mCiOthersName:
                showPickerView(5);
                break;
            case R.id.mCiOthersContact:
                getContacts(1002);
                break;
        }
    }

    private void getContacts(int requestCode) {//获取联系人，检查权限
        PermissionUtils.checkPermission(_mActivity, Manifest.permission.READ_CONTACTS, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                startActivityForResult(ContactActivity.class, requestCode);

            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                PermissionUtils.requestPermission(PersonalInfoFragment.this, Manifest.permission.READ_CONTACTS, 1000);
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestPermission(PersonalInfoFragment.this, Manifest.permission.READ_CONTACTS, 1000);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                PermissionUtils.onRequestPermissionResult(this, Manifest.permission.READ_CONTACTS, grantResults, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        startActivityForResult(ContactActivity.class, 1001);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        PermissionUtils.showDialog(_mActivity, "请在权限管理中开启通讯录权限", 150);
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        PermissionUtils.showDialog(_mActivity, "请在权限管理中开启通讯录权限", 150);
                    }
                });

                break;

        }
    }

    private void showPickerView(int type) {
        pickerType = type;
        switch (type) {
            case 0://选择学历
                mOneSelectPicker.setNPicker(mEducations, null, null);
                break;
            case 1://居住时间
                mOneSelectPicker.setNPicker(liveTimes, null, null);
                break;
            case 2://婚姻
                mOneSelectPicker.setNPicker(liveMarrys, null, null);
                break;
            case 3://职业
                mOneSelectPicker.setNPicker(workTypes, null, null);
                break;
            case 4://直系亲属
                mOneSelectPicker.setNPicker(directContactNames, null, null);
                break;
            case 5://其他关系
                mOneSelectPicker.setNPicker(othersContactNames, null, null);
                break;
        }
        mOneSelectPicker.show();
    }

    private void initOneSelectPickerData(int pickerType, int options) {
        switch (pickerType) {
            case 0:
                education = mEducations.get(options);
                mCiEducation.setContent(education);
                break;
            case 1:
                liveTime = liveTimes.get(options);
                mCiLiveTime.setContent(liveTime);
                break;
            case 2:
                liveMarry = liveMarrys.get(options);
                mCiLiveMarry.setContent(liveMarry);
                break;
            case 3:
                workType = workTypes.get(options);
                mCiWorkType.setContent(workType);
                break;
            case 4:
                directContactName = directContactNames.get(options);
                mCiDirectName.setContent(directContactName);
                break;
            case 5:
                othersContactName = othersContactNames.get(options);
                mCiOthersName.setContent(othersContactName);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    if (data != null) {
                        ContactsInfo info = data.getParcelableExtra("ContactsInfo");
                        if (info != null) {
                            contactName1 = info.name;
                            contactPhoneNum1 = info.phoneNum;
                            if (mCiDirectContact!=null)
                            mCiDirectContact.setContent(info.name + " " + info.phoneNum);
                        }

                    }
                    break;
                case 1002:
                    if (data != null) {
                        ContactsInfo info = data.getParcelableExtra("ContactsInfo");
                        if (info != null) {
                            contactName2 = info.name;
                            contactPhoneNum2 = info.phoneNum;
                            if (mCiOthersContact!=null)
                            mCiOthersContact.setContent(info.name + " " + info.phoneNum);
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public void showRealNameInfo(RealNameBean nameBean) {

    }

    @Override
    public void saveRealNameSuccess() {

    }

    @Override
    public void showUserInfo(PersonInfoBean infoBean) {
        if (infoBean != null) {
            education = infoBean.education;
            mCiEducation.setContent(education);
            liveProvince = !TextUtils.isEmpty(infoBean.liveProvince) ? infoBean.liveProvince : "";
            liveCity = !TextUtils.isEmpty(infoBean.liveCity) ? infoBean.liveCity : "";
            liveDistrict = !TextUtils.isEmpty(infoBean.liveDistrict) ? infoBean.liveDistrict : "";
            mProvince.setContent(liveProvince + " " + liveCity + "" + liveDistrict);
            mEtAddress.setText(infoBean.liveAddress);
            if (!TextUtils.isEmpty(infoBean.liveAddress)) {
                mEtAddress.setSelection(infoBean.liveAddress.length());
            }
            liveTime = infoBean.liveTime;
            mCiLiveTime.setContent(liveTime);
            liveMarry = infoBean.liveMarry;
            mCiLiveMarry.setContent(liveMarry);
            workType = infoBean.workType;
            mCiWorkType.setContent(workType);
            mEtCompany.setText(infoBean.workCompany);
            mEtCompanyAddress.setText(infoBean.workAddress);
            directContactName = infoBean.directContact;
            mCiDirectName.setContent(directContactName);
            contactName1 = infoBean.directContactName;
            contactPhoneNum1 = infoBean.directContactPhone;
            mCiDirectContact.setContent(contactName1 + " " + contactPhoneNum1);
            othersContactName = infoBean.othersContact;
            mCiOthersName.setContent(othersContactName);
            contactName2 = infoBean.othersContactName;
            contactPhoneNum2 = infoBean.othersContactPhone;
            mCiOthersContact.setContent(contactName2 + " " + contactPhoneNum2);
        }
    }

    /**
     * String directContactName;
     * String othersContactName;
     * String contactName1;
     * String contactName2;
     * String contactPhoneNum1;
     * String contactPhoneNum2;
     */

/*
    @Override
    public void showFaceCheckSuccess() {

    }*/

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

    }
}
