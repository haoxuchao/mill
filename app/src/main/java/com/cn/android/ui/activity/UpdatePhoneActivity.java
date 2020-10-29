package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.hjq.bar.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdatePhoneActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.prompt)
    TextView prompt;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.update_phone)
    TextView updatePhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_updatephone;
    }

    @Override
    protected void initView() {


    }

    @Override
    protected void initData() {
        if(TextUtils.isEmpty(UserBean().getUserphone())){
            startActivityFinish(ResetPhoneActivity.class);
        }else{
            phoneTv.setText(UserBean().getUserphone());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.update_phone)
    public void onViewClicked() {
        startActivityFinish(ResetPhoneActivity.class);
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map=new HashMap<>();
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {

    }
}
