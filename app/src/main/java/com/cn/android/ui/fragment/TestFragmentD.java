package com.cn.android.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.android.R;
import com.cn.android.bean.UserBean;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.activity.ActivationActivity;
import com.cn.android.ui.activity.BusinessCenterActivity;
import com.cn.android.ui.activity.HomeActivity;
import com.cn.android.ui.activity.InviteActivity;
import com.cn.android.ui.activity.LoginActivity;
import com.cn.android.ui.activity.MerchansGoodsManagerActivity;
import com.cn.android.ui.activity.MillManagerActivity;
import com.cn.android.ui.activity.MyMsgActivity;
import com.cn.android.ui.activity.MyOrderActivity;
import com.cn.android.ui.activity.MySettingActivity;
import com.cn.android.ui.activity.MyTeamActivity;
import com.cn.android.ui.activity.MyTradingActivity;
import com.cn.android.ui.activity.MyfocusActivity;
import com.cn.android.ui.activity.RealNameActivity;
import com.cn.android.ui.activity.ServiceActivity;
import com.cn.android.ui.activity.ShopCarActivity;
import com.cn.android.ui.activity.TopUpActivity;
import com.cn.android.ui.activity.TradingActivity;
import com.cn.android.ui.activity.WithdrawalActivity;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目界面跳转示例
 */
public final class TestFragmentD extends MyLazyFragment<HomeActivity> implements PublicInterfaceView {
    @BindView(R.id.set_iv)
    ImageView setIv;
    @BindView(R.id.car_iv)
    ImageView carIv;
    @BindView(R.id.msg_iv)
    ImageView msgIv;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.real_status)
    TextView realStatus;
    @BindView(R.id.juhuo_status)
    TextView juhuoStatus;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.order_tv)
    TextView orderTv;
    @BindView(R.id.order_more)
    TextView orderMore;
    @BindView(R.id.order_df_tv)
    TextView orderDfTv;
    @BindView(R.id.order_ds_tv)
    TextView orderDsTv;
    @BindView(R.id.order_end_tv)
    TextView orderEndTv;
    @BindView(R.id.order_c)
    ConstraintLayout orderC;
    @BindView(R.id.released_tv)
    TextView releasedTv;
    @BindView(R.id.release_tv)
    TextView releaseTv;
    @BindView(R.id.can_w_tv)
    TextView canWTv;
    @BindView(R.id.view_h)
    View viewH;
    @BindView(R.id.view_s)
    View viewS;
    @BindView(R.id.btn_w)
    TextView btnW;
    @BindView(R.id.btn_top_up)
    TextView btnTopUp;
    @BindView(R.id.money_c)
    ConstraintLayout moneyC;
    @BindView(R.id.mine_team)
    TextView mineTeam;
    @BindView(R.id.mine_jiaoyi)
    TextView mineJiaoyi;
    @BindView(R.id.mine_focus)
    TextView mineFocus;
    @BindView(R.id.mine_kuangji)
    TextView mineKuangji;
    @BindView(R.id.mine_merchants)
    TextView mineMerchants;
    @BindView(R.id.mine_service)
    TextView mineService;
    @BindView(R.id.mine_invite)
    TextView mineInvite;
    UserBean userBean;
    PublicInterfaceePresenetr presenetr;

    public static TestFragmentD newInstance() {
        return new TestFragmentD();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_d;
    }

    @Override
    protected void initView() {
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
        ImmersionBar.with(this).statusBarDarkFont(false);
        ImmersionBar.setTitleBar(getAttachActivity(), titleBar);
        presenetr = new PublicInterfaceePresenetr(this);
//        ImmersionBar.setTitleBar(this, titleBar);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        userBean = new Gson().fromJson(SPUtils.getString(Constant.UserBean, ""), UserBean.class);
        if (userBean != null && presenetr != null) {
            showLoading();
            presenetr.getGetRequest(getActivity(), ServerUrl.selectAppUserByUserid, Constant.selectAppUserByUserid);
            presenetr.getGetRequest(getActivity(), ServerUrl.selectAppCommon, Constant.selectAppCommon);
        }else{
            userName.setText("未登录");
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onResume();
        }
    }

    @OnClick({R.id.set_iv, R.id.juhuo_status, R.id.car_iv, R.id.real_status, R.id.msg_iv, R.id.user_img, R.id.order_more, R.id.order_df_tv, R.id.order_ds_tv, R.id.order_end_tv, R.id.btn_w, R.id.btn_top_up, R.id.mine_team, R.id.mine_jiaoyi, R.id.mine_focus, R.id.mine_kuangji, R.id.mine_merchants, R.id.mine_service, R.id.mine_invite})
    public void onViewClicked(View view) {
        if (userBean == null) {
            startActivity(LoginActivity.class);
        } else {
            switch (view.getId()) {
                case R.id.juhuo_status:
                    if (userBean.getIsActivation() == 2)
                        startActivity(ActivationActivity.class);//账号激活
                    else
                        ToastUtils.show("已激活");
                    break;
                case R.id.real_status:
                    if (userBean.getIsReal() == 2)
                        startActivity(RealNameActivity.class);//实名认证
                    else
                        ToastUtils.show("已实名");
                    break;
                case R.id.set_iv:
                    startActivity(MySettingActivity.class);//设置
                    break;
                case R.id.car_iv:
                    startActivity(ShopCarActivity.class);//购物车
                    break;
                case R.id.msg_iv:
                    startActivity(MyMsgActivity.class);//消息
                    break;
                case R.id.user_img:
                    break;
                case R.id.order_more:
                    startActivity(new Intent(getActivity(), MyOrderActivity.class).putExtra("order_type", 0));
                    break;
                case R.id.order_df_tv:
                    startActivity(new Intent(getActivity(), MyOrderActivity.class).putExtra("order_type", 1));
                    break;
                case R.id.order_ds_tv:
                    startActivity(new Intent(getActivity(), MyOrderActivity.class).putExtra("order_type", 2));
                    break;
                case R.id.order_end_tv:
                    startActivity(new Intent(getActivity(), MyOrderActivity.class).putExtra("order_type", 3));
                    break;
                case R.id.btn_w:
                    startActivity(WithdrawalActivity.class);//提现
                    break;
                case R.id.btn_top_up:
                    startActivity(TopUpActivity.class);//充值
                    break;
                case R.id.mine_team:
                    startActivity(MyTeamActivity.class);//我的团队
                    break;
                case R.id.mine_jiaoyi:
                    startActivity(MyTradingActivity.class);//我的交易
                    break;
                case R.id.mine_focus:
                    startActivity(MyfocusActivity.class);//我的关注
                    break;
                case R.id.mine_kuangji:
                    startActivity(MillManagerActivity.class);//我的矿机
                    break;
                case R.id.mine_merchants:
                    startActivity(BusinessCenterActivity.class);//商户中心
                    break;
                case R.id.mine_service:
                    startActivity(ServiceActivity.class);//联系客服
                    break;
                case R.id.mine_invite:
                    startActivity(InviteActivity.class);//邀请码
                    break;
            }
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.selectAppUserByUserid:
                map.put("userid", userBean.getId());
                return map;
        }
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectAppUserByUserid:
                userBean = GsonUtils.getPerson(data, UserBean.class);
                SPUtils.putString(Constant.UserBean, data);
                setData();
                break;
            case Constant.selectAppCommon:
                SPUtils.putString(Constant.Common, data);
                break;
        }
    }

    private void setData() {
        userName.setText(userBean.getNickname());
        ImageLoader.with(getActivity()).load(userBean.getHeadImg()).circle().into(userImg);
        realStatus.setText(userBean.getIsReal() == 1 ? "已实名" : "未实名");
        juhuoStatus.setText(userBean.getIsActivation() == 1 ? "已激活" : "未激活");
        juhuoStatus.setBackground(getResources().getDrawable(userBean.getIsActivation()==1?R.drawable.mine_jihuo_ed:R.drawable.mine_jihuo_bg));
        releasedTv.setText(String.format("待释放EB: %s", userBean.getNoUseEb()));
        releaseTv.setText(String.format("可用EB: %s", userBean.getUseEb()));
        canWTv.setText(String.format("可提现: %s", userBean.getUmoney()));
        SPUtils.putString(Constant.PayPwd,userBean.getPayPassword());
        mineMerchants.setVisibility(userBean.getIsOpen()==1?View.VISIBLE:View.GONE);
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}