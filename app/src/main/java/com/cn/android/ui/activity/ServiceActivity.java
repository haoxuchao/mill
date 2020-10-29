package com.cn.android.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.CommonBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import com.hjq.image.ImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceActivity extends MyActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.prompt)
    TextView prompt;
    @BindView(R.id.qr_bg)
    ImageView qrBg;
    @BindView(R.id.qr_img)
    ImageView qrImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service;
    }

    @Override
    protected void initView() {
        CommonBean commonBean=   new Gson().fromJson(SPUtils.getString(Constant.Common,""), CommonBean.class);
        ImageLoader.with(getActivity()).load(commonBean.getCustomerImg()).into(qrImg);
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
}
