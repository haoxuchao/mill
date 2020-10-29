package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.FocusBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.FocusAdapter;
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

public class MyfocusActivity extends MyActivity implements OnRefreshLoadMoreListener, PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    FocusAdapter adapter;
    private int page = 1;
    List<FocusBean> list = new ArrayList<>();
    PublicInterfaceePresenetr presenetr;
    private boolean isRefresh = true;
    private int pos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_focus;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new FocusAdapter(null);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pos = position;
                switch (view.getId()) {
                    case R.id.item:
                        startActivity(new Intent(getActivity(),IndividualShopsActivity.class).putExtra("name",list.get(position).getNickname()).putExtra("id",list.get(position).getShop_userid()));
                        break;
                    case R.id.cancel:
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(),ServerUrl.delUserFocus,Constant.delUserFocus);
                        break;
                }
            }
        });
    }

    private void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectUserFocusList, Constant.selectUserFocusList);
    }

    @Override
    protected void initData() {
        getdata();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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
            case Constant.selectUserFocusList:
                map.put("login_userid", UserBean().getId());
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.delUserFocus:
                map.put("id", list.get(pos).getId());
                return map;
        }

        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectUserFocusList:
                List<FocusBean> focusBeans = GsonUtils.getPersons(data, FocusBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(200, true, focusBeans.size() < 10);
                } else {
                    refresh.finishLoadMore(200, true, focusBeans.size() < 10);
                }
                list.addAll(focusBeans);
                adapter.setNewData(list);
                break;
            case Constant.delUserFocus:
                onRefresh(refresh);
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);

    }
}
