package com.cn.android.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.TradBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.TradingAdapter;
import com.cn.android.ui.dialog.WayBillDialog;
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

public class TradingActivity extends MyActivity implements OnRefreshLoadMoreListener, PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    TradingAdapter adapter;
    PublicInterfaceePresenetr presenetr;
    private List<TradBean> list = new ArrayList<>();
    private int page = 1;
    private boolean isRefresh = true;
    private int shouNum;
    private int pos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trading;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new TradingAdapter(null);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pos = position;
                switch (view.getId()) {
                    case R.id.chu_btn:
                        new WayBillDialog.Builder(getActivity()).setCancel("取消").setConfirm("提交").setWayBillTv("请输入出售数量").setOnListener(new WayBillDialog.OnListener() {
                            @Override
                            public void onConfirm(String content) {
                                shouNum = Integer.parseInt(content);
                                if (shouNum > list.get(position).getMax_bi_num()) {
                                    ToastUtils.show(" 出售数量不能大于求购最大数量");
                                } else {
                                    showLoading();
                                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.addBiAccountByPid, Constant.addBiAccountByPid);
                                }
                            }
                        }).show();
                        break;
                }
            }
        });
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        startActivity(AskBuyActivity.class);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refresh.autoRefresh(400);

    }
    private void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectBiAccountList, Constant.selectBiAccountList);
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
            case Constant.selectBiAccountList:
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.addBiAccountByPid:
                map.put("authorization", UserBean().getToken());
                map.put("id", list.get(pos).getId());
                map.put("userid", UserBean().getId());
                map.put("bi_num", shouNum);
                return map;
        }
        return null;


    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectBiAccountList:
                List<TradBean> tradBeans = GsonUtils.getPersons(data, TradBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(300, true, tradBeans.size() < 10);
                } else {
                    refresh.finishLoadMore(300, true, tradBeans.size() < 10);
                }
                list.addAll(tradBeans);
                adapter.setNewData(list);
                break;
            case Constant.addBiAccountByPid:
                ToastUtils.show("出售成功,待对方付钱");
                break;
        }

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
