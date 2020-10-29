package com.cn.android.ui.fragment;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.OrderBean;
import com.cn.android.bean.TradingOrder;
import com.cn.android.bean.UserBean;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.activity.MyOrderDetailActivity;
import com.cn.android.ui.activity.MyTradingDetailActivity;
import com.cn.android.ui.activity.OrderManageActivity;
import com.cn.android.ui.adapter.OrderAdapter;
import com.cn.android.ui.adapter.TradingOrderAdapter;
import com.cn.android.ui.dialog.PayPasswordDialog;
import com.cn.android.ui.dialog.WayBillDialog;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MyTradingFragment extends MyLazyFragment<OrderManageActivity> implements OnRefreshLoadMoreListener, PublicInterfaceView {
    @BindView(R.id.order_recycle)
    RecyclerView orderRecycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int type = 0;
    TradingOrderAdapter adapter;
    UserBean userBean;
    private int page = 1;
    boolean isRefresh = true;
    List<TradingOrder> list = new ArrayList<>();
    PublicInterfaceePresenetr presenetr;
    private int pos;
    private String pay_pwd;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_order;
    }

    @Override
    protected void initView() {
        refresh.setOnRefreshLoadMoreListener(this);
        presenetr = new PublicInterfaceePresenetr(this);
        userBean = new Gson().fromJson(SPUtils.getString(Constant.UserBean, ""), UserBean.class);
        adapter = new TradingOrderAdapter(null);
        orderRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.get(position).getType() == 3)
                    startActivity(new Intent(getActivity(), MyTradingDetailActivity.class).putExtra("id", list.get(position).getId()).putExtra("type", list.get(position).getType()));
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pos = position;
                switch (view.getId()) {
                    case R.id.to_pay:
                        String pwd = SPUtils.getString(Constant.PayPwd, "");
                        if (TextUtils.isEmpty(pwd)) {
                            new PayPasswordDialog.Builder(getActivity()).setTitle("请设置6位交易密码").setListener(new PayPasswordDialog.OnListener() {
                                @Override
                                public void onCompleted(BaseDialog dialog, String password) {
                                    pay_pwd = password;
                                    showLoading();
                                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.updatePayPassword, Constant.updatePayPassword);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                }
                            }).show();
                        } else {
                            String str2 = String.format("总计:<font color=\"#F44336\"><big>%sCNY</big></font>", list.get(pos).getTotalMoney());
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
                        break;
                    case R.id.confirm:
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.finishBiAccount, Constant.finishBiAccount);
                        break;
                    case R.id.cancel:
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.deleteBiAccount, Constant.deleteBiAccount);
                        break;
                }
            }
        });
    }

    public Fragment instance(int i) {
        MyTradingFragment fragment = new MyTradingFragment();
        fragment.type = i;
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh.autoRefresh(400);
    }

    @Override
    protected void initData() {
        getdata();
    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectBiAccountListByUserid, Constant.selectBiAccountListByUserid);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        isRefresh = false;
        getdata();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        isRefresh = true;
        if (list.size() > 0) list.clear();
        getdata();
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.selectBiAccountListByUserid:
                map.put("userid", userBean.getId());
                map.put("type", type);
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.finishBiAccount:
            case Constant.deleteBiAccount:
            case Constant.payBiAccount:
                map.put("authorization", userBean.getToken());
                map.put("id", list.get(pos).getId());
                map.put("userid", userBean.getId());
                return map;
            case Constant.panduanPayPassword:
                map.put("userid", userBean.getId());
                map.put("pay_password", pay_pwd);
                return map;

            case Constant.updatePayPassword:
                map.put("authorization", userBean.getToken());
                map.put("userid", userBean.getId());
                map.put("pay_password", pay_pwd);
                return map;
        }

        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectBiAccountListByUserid:
                List<TradingOrder> tradingOrders = GsonUtils.getPersons(data, TradingOrder.class);
                if (isRefresh) {
                    refresh.finishRefresh(300, true, tradingOrders.size() < 10);
                } else {
                    refresh.finishLoadMore(300, true, tradingOrders.size() < 10);
                }
                list.addAll(tradingOrders);
                adapter.setNewData(list);
                break;
            case Constant.updatePayPassword:
                ToastUtils.show("设置成功");
                SPUtils.putString(Constant.PayPwd, pay_pwd);
                break;
            case Constant.panduanPayPassword:
                showLoading();
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.payBiAccount, Constant.payBiAccount);
                break;
            case Constant.finishBiAccount:
            case Constant.deleteBiAccount:
            case Constant.payBiAccount:
                refresh.autoRefresh(400);
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
