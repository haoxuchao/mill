package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.android.R;
import com.cn.android.bean.Address;
import com.cn.android.bean.SubmitBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.CommitGoodsAdapter;
import com.cn.android.ui.dialog.PayPasswordDialog;
import com.cn.android.utils.SPUtils;
import com.hjq.bar.TitleBar;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.toast.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommitOrderActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.address_user_name)
    TextView addressUserName;
    @BindView(R.id.address_user_phone)
    TextView addressUserPhone;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.address_c)
    ConstraintLayout addressC;
    @BindView(R.id.goods_recycle)
    RecyclerView goodsRecycle;
    @BindView(R.id.pay_btn)
    TextView payBtn;
    @BindView(R.id.cars_prompt_tv)
    TextView carsPromptTv;
    @BindView(R.id.sum_money)
    TextView sumMoney;
    @BindView(R.id.bottom_c)
    ConstraintLayout bottomC;
    SubmitBean submitBean;
    @BindView(R.id.chip_group)
    Group chipGroup;
    @BindView(R.id.xiaoji_tv)
    TextView xiaojiTv;
    @BindView(R.id.xiao_price)
    TextView xiaoPrice;
    private Address maddress;
    CommitGoodsAdapter adapter;
    private String pay_pwd;
    PublicInterfaceePresenetr presenetr;
    private boolean isSet = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commit;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        submitBean = (SubmitBean) getIntent().getSerializableExtra("sub_bean");
        if (submitBean.getAddress() != null) {
            maddress = submitBean.getAddress();
            setAddress();
        } else {
            addressTv.setText("点击添加地址");
            chipGroup.setVisibility(View.GONE);
        }
        goodsRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommitGoodsAdapter(submitBean.getShopList());
        goodsRecycle.setAdapter(adapter);
        xiaojiTv.setText(String.format("共%s件商品，总计：", submitBean.getTotal_num()));
        carsPromptTv.setText(String.format("共%s件商品，总计：", submitBean.getTotal_num()));
        xiaoPrice.setText(String.format("%sEB", submitBean.getTotal_eb()));
        sumMoney.setText(String.format("%sEB", submitBean.getTotal_eb()));
    }

    public void setAddress() {
        addressTv.setText("");
        chipGroup.setVisibility(View.VISIBLE);
        addressUserName.setText(maddress.getName());
        addressUserPhone.setText(maddress.getPhone());
        addressDetail.setText(maddress.getAddress());
        addressDetail.setText(maddress.getAddress());
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

    @OnClick({R.id.address_tv, R.id.address_c, R.id.pay_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.address_tv:
            case R.id.address_c:
                startActivityForResult(AddressActivity.class, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int resultCode, @Nullable Intent data) {
                        if (data != null) {
                            maddress = (Address) data.getSerializableExtra("address");
                            setAddress();
                        }
                    }
                });
                break;
            case R.id.pay_btn:
                if(maddress!=null){
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
                    new PayPasswordDialog.Builder(getActivity()).setTitle("请输入交易密码").setListener(new PayPasswordDialog.OnListener() {
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
                }}else{
                    ToastUtils.show("请选择收货地址");
                }
                break;
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.panduanPayPassword:
                map.put("userid", UserBean().getId());
                map.put("pay_password", pay_pwd);
                return map;
            case Constant.payShop:
                map.put("authorization", UserBean().getToken());
                map.put("ordercodes", submitBean.getOrdercode());
                map.put("pay_password", pay_pwd);
                map.put("addressid", maddress.getId());
                map.put("total_money",submitBean.getTotal_eb());
                map.put("userid", UserBean().getId());
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
            case Constant.panduanPayPassword:
                showLoading();
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.payShop, Constant.payShop);
                break;
            case Constant.payShop:
                ToastUtils.show("下单成功");
                finish();
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
