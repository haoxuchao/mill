package com.cn.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.CommonBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawalActivity extends MyActivity {
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
    @BindView(R.id.h_view_1)
    View hView1;
    @BindView(R.id.prompt)
    TextView prompt;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.h_view_2)
    View hView2;
    @BindView(R.id.h_view_3)
    View hView3;
    @BindView(R.id.account_tv)
    TextView accountTv;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.confirm)
    TextView confirm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdrawal;
    }

    @Override
    protected void initView() {
        isCny.setText(String.format("当前已有：%sCNY",UserBean().getUmoney()));
        CommonBean commonBean=  new Gson().fromJson(SPUtils.getString(Constant.Common,""), CommonBean.class);
        prompt.setText(String.format("平台收取%s手续费",commonBean.getTixianPercentum()*100+"%"));
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
