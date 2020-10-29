package com.cn.android.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.Address;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.dialog.DoubleBtnPromptDialog;
import com.hjq.bar.TitleBar;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.toast.ToastUtils;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertAddressActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.address_select)
    TextView addressSelect;
    @BindView(R.id.address_detail)
    EditText addressDetail;
    @BindView(R.id.default_address_tv)
    TextView defaultAddressTv;
    @BindView(R.id.default_switch)
    Switch defaultSwitch;
    @BindView(R.id.detele_address_tv)
    TextView deteleAddressTv;
    private CityPicker mCP;
    private Address address;
    PublicInterfaceePresenetr presenetr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_insert_address;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        address = (Address) getIntent().getSerializableExtra("address");
        if (address != null) {
            titleBar.setTitle("修改地址");
            editName.setText(address.getName());
            editPhone.setText(address.getPhone());
            addressSelect.setText(address.getProCityArea());
            addressDetail.setText(address.getAddress());
            defaultSwitch.setChecked(address.getIsDefault() == 1);
            deteleAddressTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        //保存
        if (isCheck()) {
            showLoading();
            if (address != null)
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.updateAddress, Constant.updateAddress);
            else
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.addAddress, Constant.addAddress);
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

    public void mYunCityPicher() {
        mCP = new CityPicker.Builder(getActivity())
                .textSize(12)
                //地址选择
                .title("  ")
//                .titleBackgroundColor("#0E9DFF")
                .backgroundPop(0xa0000000)
                //文字的颜色
                .titleBackgroundColor("#EEEEEE")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                //滑轮文字的颜色
                .textColor(Color.parseColor("#000000"))
                //省滑轮是否循环显示
                .provinceCyclic(true)
                //市滑轮是否循环显示
                .cityCyclic(false)
                //地区（县）滑轮是否循环显示
                .districtCyclic(false)
                //滑轮显示的item个数
                .visibleItemsCount(5)
                //滑轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();

        //监听
        mCP.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省
                String province = citySelected[0];
                //市
                String city = citySelected[1];
                //区。县。（两级联动，必须返回空）
                String district = citySelected[2];
                //邮证编码
                String code = citySelected[3];

                addressSelect.setText(province + "\t" + city + "\t" + district);
            }

            @Override
            public void onCancel() {


            }
        });
    }

    public boolean isCheck() {
        if (TextUtils.isEmpty(editName.getText().toString().trim())) {
            ToastUtils.show("请输入名称");
            return false;
        }
        if (TextUtils.isEmpty(editPhone.getText().toString().trim())) {
            ToastUtils.show("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(addressSelect.getText().toString().trim())) {
            ToastUtils.show("请选择省市区");
            return false;
        }
        if (TextUtils.isEmpty(addressDetail.getText().toString().trim())) {
            ToastUtils.show("请输入详细地址");
            return false;
        }
        return true;
    }

    /**
     * 隐藏软键盘
     *
     * @param et
     */
    protected void hideInputMethod(EditText et) {
        InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        et.clearFocus();
    }

    @OnClick({R.id.address_select, R.id.detele_address_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.address_select:
                hideInputMethod(editName);
                hideInputMethod(addressDetail);
                hideInputMethod(editPhone);
                mYunCityPicher();
                mCP.show();
                break;
            case R.id.detele_address_tv:
                new DoubleBtnPromptDialog.Builder(this).setmOnListener(new DoubleBtnPromptDialog.OnListener() {
                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onConfirm(BaseDialog dialog) {
                        dialog.dismiss();
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.deleteAddress, Constant.deleteAddress);
                    }
                }).show();
                break;
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.addAddress:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("name", editName.getText().toString().trim());
                map.put("phone", editPhone.getText().toString().trim());
                map.put("pro_city_area", addressSelect.getText().toString().trim());
                map.put("address", addressDetail.getText().toString().trim());
                map.put("is_default", defaultSwitch.isChecked() ? 1 : 2);
                return map;
            case Constant.updateAddress:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("name", editName.getText().toString().trim());
                map.put("phone", editPhone.getText().toString().trim());
                map.put("pro_city_area", addressSelect.getText().toString().trim());
                map.put("address", addressDetail.getText().toString().trim());
                map.put("is_default", defaultSwitch.isChecked() ? 1 : 2);
                map.put("id", address.getId());
                return map;
            case Constant.deleteAddress:
                map.put("authorization", UserBean().getToken());
                map.put("id", address.getId());
                return map;
        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.addAddress:
                ToastUtils.show(" 添加成功");
                finish();
                break;
            case Constant.updateAddress:
                ToastUtils.show(" 修改成功");
                finish();
                break;
            case Constant.deleteAddress:
                ToastUtils.show("删除成功");
                finish();
                break;

        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
