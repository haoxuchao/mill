package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.android.R;
import com.cn.android.bean.CommonBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.FileOperationPresenetr;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.FileOperationView;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.dialog.PayPasswordDialog;
import com.cn.android.ui.dialog.PromptDialog;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OpenStoreActivity extends MyActivity implements PublicInterfaceView, FileOperationView {
    @BindView(R.id.back_img)
    ImageButton backImg;
    @BindView(R.id.top_prompt_tv)
    TextView topPromptTv;
    @BindView(R.id.top_prompt_red_tv)
    TextView topPromptRedTv;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.id_card_prompt)
    TextView idCardPrompt;
    @BindView(R.id.front_img)
    ImageView frontImg;
    @BindView(R.id.reverse_img)
    ImageView reverseImg;
    @BindView(R.id.id_card_img_c)
    ConstraintLayout idCardImgC;
    @BindView(R.id.header_prompt)
    TextView headerPrompt;
    @BindView(R.id.handheld_img)
    ImageView handheldImg;
    @BindView(R.id.header_c)
    ConstraintLayout headerC;
    @BindView(R.id.id_card_tv)
    TextView idCardTv;
    @BindView(R.id.edit_id_card_no)
    EditText editIdCardNo;
    @BindView(R.id.confirm_btn)
    TextView confirmBtn;
    boolean isSet = true;
    FileOperationPresenetr fileOperationPresenetr;
    PublicInterfaceePresenetr presenetr;
    String firstPwd = "", secend = "";
    String front_img, reverse_img, handheld_img;
    private String pay_pwd;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_open_stroe;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        fileOperationPresenetr = new FileOperationPresenetr(this);
        ImmersionBar.setTitleBar(this, titleBar);
        CommonBean commonBean = new Gson().fromJson(SPUtils.getString(Constant.Common, ""), CommonBean.class);
        topPromptRedTv.setText(String.format("%s(EB)", commonBean.getOpenShopEb()));

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

    @OnClick({R.id.back_img, R.id.front_img, R.id.reverse_img, R.id.handheld_img, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.front_img:
                PhotoActivity.start(getActivity(), new PhotoActivity.OnPhotoSelectListener() {
                    @Override
                    public void onSelect(List<String> data) {
                        ImageLoader.with(getActivity())
                                .load(data.get(0))
                                .into(frontImg);
                        showLoading();
                        fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(data.get(0)), ServerUrl.upload, 100);
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            case R.id.reverse_img:
                PhotoActivity.start(getActivity(), new PhotoActivity.OnPhotoSelectListener() {
                    @Override
                    public void onSelect(List<String> data) {
                        ImageLoader.with(getActivity())
                                .load(data.get(0))
                                .into(reverseImg);
                        showLoading();
                        fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(data.get(0)), ServerUrl.upload, 101);
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            case R.id.handheld_img:
                PhotoActivity.start(getActivity(), new PhotoActivity.OnPhotoSelectListener() {
                    @Override
                    public void onSelect(List<String> data) {
                        ImageLoader.with(getActivity())
                                .load(data.get(0))
                                .into(handheldImg);
                        showLoading();
                        fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(data.get(0)), ServerUrl.upload, 102);
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            case R.id.confirm_btn:
                if (isCheck()) {
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
                    }
                }
                break;
        }
    }

    private boolean isCheck() {
        if (TextUtils.isEmpty(editIdCardNo.getText().toString().trim())) {
            ToastUtils.show("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(front_img)) {
            ToastUtils.show("请选择身份证正面照片");
            return false;
        }
        if (TextUtils.isEmpty(reverse_img)) {
            ToastUtils.show("请选择身份证反面照片");
            return false;
        }
        if (TextUtils.isEmpty(handheld_img)) {
            ToastUtils.show("请选择手持身份证照片");
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();

        switch (tag) {
            case Constant.panduanPayPassword:
                map.put("userid", UserBean().getId());
                map.put("pay_password", pay_pwd);
                return map;
            case Constant.updatePayPassword:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("pay_password", pay_pwd);
                return map;
            case Constant.openShop:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("frond_card_img", front_img);
                map.put("back_card_img", reverse_img);
                map.put("hand_card_img", handheld_img);
                map.put("phone", editIdCardNo.getText().toString().trim());
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
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.openShop, Constant.openShop);
                break;
            case Constant.updatePayPassword:
                SPUtils.putString(Constant.PayPwd, pay_pwd);
                break;
            case Constant.openShop:
                new PromptDialog.Builder(getActivity()).setPrompt1("已申请开通商铺").show();
                break;
        }

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }

    @Override
    public void FileOperationSuccess(Object data, int tag) {
        showComplete();
        switch (tag) {
            case 100:
                front_img = (String) data;
                break;
            case 101:
                reverse_img = (String) data;
                break;
            case 102:
                handheld_img = (String) data;
                break;
        }

    }

    @Override
    public void FileOperationProgress(float progress, int tag) {

    }

    @Override
    public void FileOperationError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
