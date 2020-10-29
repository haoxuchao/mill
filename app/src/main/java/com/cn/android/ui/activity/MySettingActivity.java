package com.cn.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.UserBean;
import com.cn.android.common.MyActivity;
import com.cn.android.helper.ActivityStackManager;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.utils.SPUtils;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MySettingActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.set_info)
    TextView setInfo;
    @BindView(R.id.set_pwd)
    TextView setPwd;
    @BindView(R.id.set_phone)
    TextView setPhone;
    @BindView(R.id.set_address)
    TextView setAddress;
    @BindView(R.id.logout_btn)
    TextView logoutBtn;
    PublicInterfaceePresenetr presenetr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mysetting;
    }

    @Override
    protected void initView() {
        presenetr=new PublicInterfaceePresenetr(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (presenetr != null) {
            showLoading();
            presenetr.getGetRequest(getActivity(), ServerUrl.selectAppUserByUserid, Constant.selectAppUserByUserid);
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.set_info, R.id.set_pwd, R.id.set_phone, R.id.set_address, R.id.logout_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_info:
                startActivity(MyInfoSetActivity.class);//基本信息设置
                break;
            case R.id.set_pwd:
                startActivity(UpdatePwdActivity.class);//密码设置
                break;
            case R.id.set_phone:
                startActivity(UpdatePhoneActivity.class);//修改手机
                break;
            case R.id.set_address:
                startActivity(AddressActivity.class);//收货地址
                break;
            case R.id.logout_btn:

                SPUtils.removeAll();
                startActivity(LoginActivity.class);
                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities(LoginActivity.class);
                break;
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserBean().getId());
        return map;

    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectAppUserByUserid:
                UserBean userBean= GsonUtils.getPerson(data,UserBean.class);
                SPUtils.putString(Constant.UserBean, data);
                SPUtils.putString(Constant.PayPwd,userBean.getPayPassword());
//                finish();
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
