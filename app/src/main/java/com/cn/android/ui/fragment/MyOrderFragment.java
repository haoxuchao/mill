package com.cn.android.ui.fragment;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.OrderBean;
import com.cn.android.bean.UserBean;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.activity.MyOrderActivity;
import com.cn.android.ui.activity.MyOrderDetailActivity;
import com.cn.android.ui.adapter.OrderAdapter;
import com.cn.android.utils.SPUtils;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class MyOrderFragment extends MyLazyFragment<MyOrderActivity> implements OnRefreshLoadMoreListener, PublicInterfaceView,OrderAdapter.onItemListener {
    @BindView(R.id.order_recycle)
    RecyclerView orderRecycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int type = 0;
    OrderAdapter adapter;
    UserBean userBean;
    private int page = 1;
    private String orderid;
    private boolean isRefresh = true;
    PublicInterfaceePresenetr presenetr;
    List<OrderBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.frag_order;
    }

    @Override
    protected void initView() {
        refresh.setOnRefreshLoadMoreListener(this);
        presenetr = new PublicInterfaceePresenetr(this);
        userBean = new Gson().fromJson(SPUtils.getString(Constant.UserBean, ""), UserBean.class);
        adapter = new OrderAdapter(null, 1);
        adapter.setOnItemListener(this);
        orderRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.confirm:
                        orderid = list.get(position).getOrdercode();
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.reciveOrder, Constant.reciveOrder);
                        break;
                    case R.id.item:
                        startActivity(new Intent(getActivity(), MyOrderDetailActivity.class).putExtra("order", list.get(position)));
                        break;
                }
            }
        });

    }

    public Fragment instance(int i) {
        MyOrderFragment fragment = new MyOrderFragment();
        fragment.type = i;
        return fragment;
    }

    @Override
    protected void initData() {
        getdata();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && presenetr != null) {
            refresh.autoRefresh(200);
        }
    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectOrderList, Constant.selectOrderList);
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
            case Constant.selectOrderList:
                map.put("type", 1);
                map.put("login_userid", userBean.getId());
                map.put("status", type == 0 ? "" : String.valueOf(type));
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.reciveOrder:
                map.put("ordercode", orderid);
                return map;
        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectOrderList:
                List<OrderBean> orderBeans = GsonUtils.getPersons(data, OrderBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(300, true, orderBeans.size() < 10);
                } else {
                    refresh.finishLoadMore(300, true, orderBeans.size() < 10);
                }
                list.addAll(orderBeans);
                adapter.setNewData(orderBeans);
                break;
            case Constant.reciveOrder:
                onRefresh(refresh);
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }

    @Override
    public void onlick(int pos) {
        startActivity(new Intent(getActivity(), MyOrderDetailActivity.class).putExtra("order", list.get(pos)));
    }
}
