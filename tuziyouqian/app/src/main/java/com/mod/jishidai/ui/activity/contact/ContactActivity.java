package com.mod.jishidai.ui.activity.contact;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.lib.core.utils.CollectionUtils;
import com.lib.core.utils.FormatUtil;
import com.lib.core.utils.JsonUtils;
import com.lib.core.utils.OnItemClickListener;
import com.lib.core.utils.PermissionUtils;
import com.lib.core.utils.ToastUtil;
import com.mod.jishidai.R;
import com.mod.jishidai.base.App;
import com.mod.jishidai.base.BaseActivity;
import com.mod.jishidai.bean.ContactBean;
import com.mod.jishidai.bean.ContactsInfo;
import com.mod.jishidai.bean.LetterComparator;
import com.mod.jishidai.bean.PersonInfoBean;
import com.mod.jishidai.bean.RealNameBean;
import com.mod.jishidai.ui.activity.certification.CertificatContract;
import com.mod.jishidai.ui.activity.certification.CertificatModel;
import com.mod.jishidai.ui.activity.certification.CertificatPresenter;
import com.mod.jishidai.widget.ClearEditText;
import com.mod.jishidai.widget.TitleHeaderBar;
import com.nanchen.wavesidebar.FirstLetterUtil;
import com.nanchen.wavesidebar.Trans2PinYinUtil;
import com.nanchen.wavesidebar.WaveSideBarView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class ContactActivity extends BaseActivity<CertificatPresenter, CertificatModel> implements CertificatContract.View {
    List<ContactsInfo> list = new ArrayList<>();
    private List<ContactsInfo> mShowModels = new ArrayList<>();
    @BindView(R.id.mTitle)
    TitleHeaderBar mTitle;
    @BindView(R.id.mEtSearch)
    ClearEditText mEtSearch;
    @BindView(R.id.mRecycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSideBar)
    WaveSideBarView mSideBar;
    ContactsAdapter mAdapter;


    @Override
    protected void getBundleExtras(Bundle bundle) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);

    }

    @Override
    public void initView() {
        mTitle.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initContacts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isActive) {
            isActive = true;
            initContacts();
        }
    }

    private void initContacts() {
        getContacts();
        mAdapter = new ContactsAdapter(mShowModels);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mSideBar.setOnSelectIndexItemListener(new WaveSideBarView.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String letter) {
                for (int i = 0; i < list.size(); i++) {
                    if (letter.equals(list.get(i).index)) {
                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mShowModels.clear();
                for (ContactsInfo model : list) {
                    String str = Trans2PinYinUtil.trans2PinYin(model.name);
                    if (str.contains(s.toString()) || model.name.contains(s.toString()) || model.phoneNum.contains(s.toString()) ) {
                        mShowModels.add(model);
                    }
                }
                if (mAdapter != null)
                    mAdapter.notifyDataSetChanged();

            }
        });

        mAdapter.setOnItemClickListener(new OnItemClickListener<ContactsInfo>() {
            @Override
            public void onItemClick(View v, int position, ContactsInfo info) {
                if (!FormatUtil.isMobileNO(info.phoneNum)) {
                    ToastUtil.showShort("你选择的号码不符合要求！");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("ContactsInfo", info);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        uploadContact();
    }


    private void uploadContact() {
        if (!CollectionUtils.isNullOrEmpty(list)) {
            List<ContactBean> mList = new ArrayList<>();
            for (ContactsInfo info : list) {
                ContactBean bean = new ContactBean("", info.name, info.phoneNum);
                mList.add(bean);
            }
            mPresenter.uploadContact(JsonUtils.bean2json(mList));
        }

    }

    private void getContacts() {
        try {
            list.clear();
            mShowModels.clear();
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = App.getInstance().getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id", "data1"},
                    null, null, "sort_key");
            String contactName;
            String contactNumber;
            int contactId;
            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (TextUtils.isEmpty(contactNumber)) {
                    continue;
                }
                contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                ContactsInfo contactsInfo = new ContactsInfo(contactName, FormatUtil.trimTelNum(contactNumber), FirstLetterUtil.getFirstLetter(contactName), contactId);
                if (contactName != null)
                    list.add(contactsInfo);
            }
            Collections.sort(list, new LetterComparator());
            mShowModels.addAll(list);
            if (CollectionUtils.isNullOrEmpty(list)){
                PermissionUtils.showDialog(ContactActivity.this, "请在权限管理中开启通讯录权限", 150);
            }
            if (cursor != null)
                cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

        } catch (Exception e) {
            PermissionUtils.showDialog(ContactActivity.this, "请在权限管理中开启通讯录权限", 150);
            e.printStackTrace();
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

    }

/*    @Override
    public void showFaceCheckSuccess() {

    }*/

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showToast(String msg) {

    }
}
