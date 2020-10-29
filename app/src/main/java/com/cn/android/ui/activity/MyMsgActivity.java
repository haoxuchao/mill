package com.cn.android.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.MsgBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.MsgAdapter;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyMsgActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    MsgAdapter adapter;
    PublicInterfaceePresenetr presenetr;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1;
    List<MsgBean> list = new ArrayList<>();
    boolean isRefresh = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        adapter = new MsgAdapter(null);
        refresh.setOnRefreshLoadMoreListener(this);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (list.get(position).getType() != 1)
                    ImageActivity.start(getActivity(), list.get(position).getContentImg());
            }
        });
    }

    @Override
    protected void initData() {
        getdata();
    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectMessageList, Constant.selectMessageList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("login_userid", UserBean().getId());
        map.put("page", page);
        map.put("rows", 10);
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        List<MsgBean> msgBeans = GsonUtils.getPersons(data, MsgBean.class);
        if (isRefresh) {
            refresh.finishRefresh(200, true, msgBeans.size() < 10);
        } else {
            refresh.finishLoadMore(200, true, msgBeans.size() < 10);
        }
        list.addAll(msgBeans);
        adapter.setNewData(list);
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
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


}
