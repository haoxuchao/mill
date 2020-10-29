package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.MerchantsGoodsBean;
import com.cn.android.bean.shopBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.NewsGoodsAdapter;
import com.cn.android.widget.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
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

public class IndividualShopsActivity extends MyActivity implements OnRefreshLoadMoreListener, PublicInterfaceView {
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private NewsGoodsAdapter adapter;
    private int page = 1;
    PublicInterfaceePresenetr presenetr;
    private boolean isRefresh = true;
    List<shopBean> list = new ArrayList<>();
    private String id;
    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_individual;
    }
    @Override
    protected void initView() {
        presenetr=new PublicInterfaceePresenetr(this);
        id=getIntent().getStringExtra("id");name=getIntent().getStringExtra("name");
        title.setText(String.format("%s的秘密的店铺",name));
        ImmersionBar.setTitleBar(this, titleBar);
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new NewsGoodsAdapter(null);
        recycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycle.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), GoodsDetailActivity.class).putExtra("id", list.get(position).getId()));
            }
        });
    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectShops, Constant.selectShops);
    }

    @Override
    protected void initData() {
        getdata();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: addsetContentView(...) invocation
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
            case Constant.selectShops:
                map.put("userid", id);
                map.put("page", page);
                map.put("rows", 10);
                return map;
        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectShops:
                List<shopBean> shopBeans = GsonUtils.getPersons(data, shopBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(200, true, shopBeans.size() < 10);
                }else {
                    refresh.finishLoadMore(200, true, shopBeans.size() < 10);
                }
                list.addAll(shopBeans);
                adapter.setNewData(list);
                break;
        }

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
