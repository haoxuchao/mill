package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.CommonBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.dialog.PayPasswordDialog;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.hjq.bar.TitleBar;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.toast.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivationActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.activation_tv)
    TextView activationTv;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.days_tv)
    TextView daysTv;
    @BindView(R.id.prompt_tv)
    TextView promptTv;
    @BindView(R.id.activation_btn)
    TextView activationBtn;
    PublicInterfaceePresenetr presenetr;
    private String pay_pwd;
    CommonBean commonBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_activation;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        commonBean = new Gson().fromJson(SPUtils.getString(Constant.Common, ""), CommonBean.class);
        price.setText(String.format("%s(CNY)", commonBean.getActivationMoney()));
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

    @OnClick(R.id.activation_btn)
    public void onViewClicked() {
        String pwd = SPUtils.getString(Constant.PayPwd, "");
        if (TextUtils.isEmpty(pwd)) {
            new PayPasswordDialog.Builder(getActivity()).setTitle("请设置6位交易密码").setListener(new PayPasswordDialog.OnListener() {
                @Override
                public void onCompleted(BaseDialog dialog, String password) {
                    pay_pwd = password;
                    showLoading();
                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.updatePayPassword, Constant.updatePayPassword);
                    dialog.dismiss();
//                            isSet = !isSet;

                }

                @Override
                public void onCancel(BaseDialog dialog) {
                }
            }).show();

        } else {

            String str2 = String.format("总计<font color=\"#F44336\"><big>%sCNY</big></font>", commonBean.getActivationMoney());
            new PayPasswordDialog.Builder(getActivity()).setTitle(str2).setListener(new PayPasswordDialog.OnListener() {
                @Override
                public void onCompleted(BaseDialog dialog, String password) {
                    pay_pwd = password;
                    showLoading();
                    presenetr.getGetRequest(getActivity(), ServerUrl.panduanPayPassword, Constant.panduanPayPassword);
                    dialog.dismiss();
                }

                @Override
                public void onCancel(BaseDialog dialog) {
                }
            }).show();
        }

    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.buyAccount:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                return map;
            case Constant.panduanPayPassword:
                map.put("userid", UserBean().getId());
                map.put("pay_password", pay_pwd);
                return map;

            case Constant.updatePayPassword:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("pay_password", pay_pwd);
                return map;
        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.buyAccount:
                finish();
                break;
            case Constant.panduanPayPassword:
                showLoading();
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.buyAccount, Constant.buyAccount);
                break;
            case Constant.updatePayPassword:
                ToastUtils.show("设置成功");
                SPUtils.putString(Constant.PayPwd, pay_pwd);
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
