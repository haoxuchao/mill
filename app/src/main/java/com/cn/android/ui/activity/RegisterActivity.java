package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.UserBean;
import com.cn.android.common.MyActivity;
import com.cn.android.helper.InputTextHelper;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.utils.SPUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.view.CountdownView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 注册界面
 */
public final class RegisterActivity extends MyActivity implements PublicInterfaceView {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.tv_prompt1)
    TextView tvPrompt1;
    @BindView(R.id.tv_prompt2)
    TextView tvPrompt2;
    @BindView(R.id.go_tv)
    TextView goTv;
    @BindView(R.id.phone_tv)
    TextView phoneTv;
    @BindView(R.id.edit_phone)
    EditText editPhone;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.yzm_tv)
    TextView yzmTv;
    @BindView(R.id.edit_yzm)
    EditText editYzm;
    @BindView(R.id.view_2)
    View view2;
    @BindView(R.id.cv_password_forget_countdown)
    CountdownView cvPasswordForgetCountdown;
    @BindView(R.id.invite_tv)
    TextView inviteTv;
    @BindView(R.id.edit_invite)
    EditText editInvite;
    @BindView(R.id.view_3)
    View view3;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    PublicInterfaceePresenetr presenetr;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        presenetr=new PublicInterfaceePresenetr(this);
        InputTextHelper.with(this)
                .addView(editPhone)
                .addView(editYzm)
                .setMain(loginBtn)
                .setListener(new InputTextHelper.OnInputTextListener() {
                    @Override
                    public boolean onInputChange(InputTextHelper helper) {
                        return editPhone.getText().toString().length() == 11 &&
                                editYzm.getText().toString().length() >= 6 ;

                    }
                })
                .build();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected ImmersionBar statusBarConfig() {
        // 不要把整个布局顶上去
        return super.statusBarConfig().keyboardEnable(true);
    }

    public boolean isCheack() {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.go_tv, R.id.cv_password_forget_countdown, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.go_tv:
                startActivity(LoginActivity.class);
                break;
            case R.id.cv_password_forget_countdown:
                if(TextUtils.isEmpty(editPhone.getText().toString().trim())){
                    ToastUtils.show("请输入手机号");
                    cvPasswordForgetCountdown.resetState();
                    return;
                }else{
                    showLoading();
                    presenetr.getGetRequest(getActivity(), ServerUrl.sendSms,Constant.sendSms);
                }
                break;
            case R.id.login_btn:
                if(isCheack()){
                    showLoading();
                    presenetr.getPostRequest(getActivity(), ServerUrl.register,Constant.register);
                }
                break;
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.register:
                map.put("userphone", editPhone.getText().toString().trim());
                map.put("smscode", editYzm.getText().toString().trim());
                if (!editInvite.getText().toString().trim().equals(""))
                    map.put("pid", editInvite.getText().toString().trim());
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
        Log.e("sss",data);
        switch (tag) {
            case Constant.register:
                UserBean userBean= GsonUtils.getPerson(data,UserBean.class);
                if(userBean!=null){
                    SPUtils.putString(Constant.UserBean,data);
                    SPUtils.putString(Constant.PayPwd,userBean.getPayPassword());
                    startActivityFinish(HomeActivity.class);
                }
                break;
            case Constant.sendSms:
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}