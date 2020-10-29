package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.shopBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.NewsGoodsAdapter;
import com.cn.android.widget.GridSpacingItemDecoration;
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

import static org.litepal.LitePalApplication.getContext;

public class SeachActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.goods_recycle)
    RecyclerView goodsRecycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1;
    PublicInterfaceePresenetr presenetr;
    private boolean isRefresh = true;
    private List<shopBean> list = new ArrayList<>();
    private String keyword;
    NewsGoodsAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seach;
    }

    @Override
    protected void initView() {
        refresh.setOnRefreshLoadMoreListener(this);
        presenetr = new PublicInterfaceePresenetr(this);
        keyword = getIntent().getStringExtra("word");
        adapter = new NewsGoodsAdapter(null);
        goodsRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        goodsRecycle.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));
        goodsRecycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), GoodsDetailActivity.class).putExtra("id", list.get(position).getId()));
            }
        });
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
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("search", keyword);
        map.put("rows", 10);
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        List<shopBean> listbeans = GsonUtils.getPersons(data, shopBean.class);
        if (isRefresh) {
            refresh.finishRefresh(300, true, listbeans.size() < 10);
        } else {
            refresh.finishLoadMore(300, true, listbeans.size() < 10);
        }
        list.addAll(listbeans);
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

    private void getdata() {
        showComplete();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectShopListBySearch, Constant.selectShopListBySearch);
    }
}
