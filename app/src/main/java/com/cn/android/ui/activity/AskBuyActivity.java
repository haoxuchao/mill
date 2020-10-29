package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AskBuyActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.max_num_tv)
    TextView maxNumTv;
    @BindView(R.id.edit_max_num)
    EditText editMaxNum;
    @BindView(R.id.min_num_tv)
    TextView minNumTv;
    @BindView(R.id.edit_min_num)
    EditText editMinNum;
    @BindView(R.id.price_tv)
    TextView priceTv;
    @BindView(R.id.edit_price)
    EditText editPrice;
    @BindView(R.id.edit_c)
    ConstraintLayout editC;
    @BindView(R.id.push_btn)
    TextView pushBtn;
    PublicInterfaceePresenetr presenetr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_askbuy;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
    }

    @Override
    protected void initData() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.seletOneBiMoney, Constant.seletOneBiMoney);
    }

    public boolean ischeck() {
        if (TextUtils.isEmpty(editMaxNum.getText().toString().trim())) {
            ToastUtils.show("请输入最高数量");
            return false;
        }
        if (TextUtils.isEmpty(editMinNum.getText().toString().trim())) {
            ToastUtils.show("请输入最低数量");
            return false;
        }
        if (TextUtils.isEmpty(editPrice.getText().toString().trim())) {
            ToastUtils.show("请输入单价");
            return false;
        }
        if (Integer.parseInt(editMinNum.getText().toString().trim()) < 1) {
            ToastUtils.show("最低数量不能小于1");
            return false;
        }
        if (Integer.parseInt(editMaxNum.getText().toString().trim()) < Integer.parseInt(editMinNum.getText().toString().trim())) {
            ToastUtils.show(" 最高数量不能小于最低数量");
            return false;
        }
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.push_btn)
    public void onViewClicked() {
        showLoading();
        presenetr.getPostTokenRequest(getActivity(), ServerUrl.addBiAccount, Constant.addBiAccount);

    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.addBiAccount:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("min_bi_num", editMinNum.getText().toString().trim());
                map.put("max_bi_num", editMaxNum.getText().toString().trim());
                map.put("bi_one_money", editPrice.getText().toString().trim());
                return map;

        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.addBiAccount:
                ToastUtils.show("发布成功");
                finish();
                break;
            case Constant.seletOneBiMoney:
                editPrice.setText(data);
                break;
        }


    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
