package com.cn.android.ui.fragment;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.MillBean;
import com.cn.android.bean.UserBean;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.activity.MillManagerActivity;
import com.cn.android.ui.adapter.MillAdapetr;
import com.cn.android.ui.dialog.PayPasswordDialog;
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

public class MillManagerFragment extends MyLazyFragment<MillManagerActivity> implements OnRefreshLoadMoreListener, PublicInterfaceView {
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int type;
    MillAdapetr adapetr;
    UserBean userBean;
    private int page = 1;
    PublicInterfaceePresenetr presenetr;
    List<MillBean> list = new ArrayList<>();
    boolean isRefresh = true;
    private String pay_pwd;
    private int pos;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_mill_m;
    }

    public Fragment instance(int i) {
        MillManagerFragment fragment = new MillManagerFragment();
        fragment.type = i;
        return fragment;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        userBean = new Gson().fromJson(SPUtils.getString(Constant.UserBean, ""), UserBean.class);
        refresh.setOnRefreshLoadMoreListener(this);
        adapetr = new MillAdapetr(null);
        adapetr.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pos = position;
                switch (view.getId()) {
                    case R.id.buy_btn:
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
                            String str2 = String.format("总计<font color=\"#F44336\"><big>%sEB</big></font>", list.get(position).getDuihuan_eb_num());
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
                    case R.id.qi_btn:
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.useMill, Constant.useMill);
                        break;
                }
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle.setAdapter(adapetr);
    }

    private void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectMillList, Constant.selectMillList);
    }

    @Override
    protected void initData() {
        getdata();
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
            case Constant.selectMillList:
                map.put("type", type);
                if (type == 1)
                    map.put("login_userid", userBean.getId());
                map.put("page", page);
                map.put("rows", 10);
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
            case Constant.buyMill:
                map.put("authorization", userBean.getToken());
                map.put("userid", userBean.getId());
                map.put("id", list.get(pos).getId());
                return map;
            case Constant.useMill:
                map.put("authorization", userBean.getToken());
                map.put("id", list.get(pos).getId());
                return map;
        }
        return null;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&refresh!=null){
            refresh.autoRefresh(300);
        }
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectMillList:
                List<MillBean> millBeans = GsonUtils.getPersons(data, MillBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(300, true, millBeans.size() < 10);
                } else {
                    refresh.finishLoadMore(300, true, millBeans.size() < 10);
                }
                list.addAll(millBeans);
                adapetr.setNewData(list);
                break;
            case Constant.buyMill:
            case Constant.useMill:
                refresh.autoRefresh(300);
                break;
            case Constant.panduanPayPassword:
                showLoading();
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.buyMill, Constant.buyMill);
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
