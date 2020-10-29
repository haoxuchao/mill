package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.helper.InputTextHelper;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.view.CountdownView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPhoneActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.prompt)
    TextView prompt;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.cv_test_countdown)
    CountdownView cvTestCountdown;
    @BindView(R.id.gui_tv)
    TextView guiTv;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.bind_phone)
    TextView bindPhone;
    PublicInterfaceePresenetr presenetr;
    @BindView(R.id.edit_yzm)
    EditText editYzm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_phone;
    }

    @Override
    protected void initView() {
        InputTextHelper.with(this)
                .addView(editPhone)
                .addView(editYzm)
                .setMain(bindPhone)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return editPhone.getText().toString().length() == 11 &&
                                editYzm.getText().toString().length() >= 6 ;
                    }
                })
                .build();
        presenetr = new PublicInterfaceePresenetr(this);

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

    @OnClick({R.id.cv_test_countdown, R.id.bind_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_test_countdown:
                if (TextUtils.isEmpty(editPhone.getText().toString().trim())) {
                    ToastUtils.show("请输入手机号");
                    cvTestCountdown.resetState();
                    return;
                } else {
                    showLoading();
                    presenetr.getGetRequest(getActivity(), ServerUrl.sendSms, Constant.sendSms);
                }
                break;
            case R.id.bind_phone:
                if (isCheck()) {
                    showLoading();
                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.updatePhone, Constant.updatePhone);
                }
                break;
        }
    }

    public boolean isCheck() {
        if (TextUtils.isEmpty(editPhone.getText().toString().trim())) {
            ToastUtils.show("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(editYzm.getText().toString().trim())) {
            ToastUtils.show("请输入验证码");
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.updatePhone:
                map.put("authorization", UserBean().getToken());
                map.put("userphone", editPhone.getText().toString().trim());
                map.put("smscode", editYzm.getText().toString().trim());
                map.put("userid", UserBean().getId());
                return map;
            case Constant.sendSms:
                map.put("loginName", editPhone.getText().toString().trim());
                return map;
        }

        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.updatePhone:
                finish();
                break;
            case Constant.sendSms:
                ToastUtils.show("发送成功");
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
