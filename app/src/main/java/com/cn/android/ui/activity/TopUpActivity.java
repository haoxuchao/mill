package com.cn.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.hjq.bar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopUpActivity extends MyActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.is_cny)
    TextView isCny;
    @BindView(R.id.money_tv)
    TextView moneyTv;
    @BindView(R.id.edit_money)
    EditText editMoney;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.h_view)
    View hView;
    @BindView(R.id.confirm)
    TextView confirm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_topup;
    }

    @Override
    protected void initView() {
        isCny.setText(String.format("当前已有：%sCNY",UserBean().getUmoney()));
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

    @OnClick(R.id.confirm)
    public void onViewClicked() {
    }
}
