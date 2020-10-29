package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.Address;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.AddressAdapter;
import com.hjq.bar.TitleBar;
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

public class AddressActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.address_recycle)
    RecyclerView addressRecycle;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.add_address)
    TextView addAddress;
    AddressAdapter addressAdapter;
    PublicInterfaceePresenetr presenetr;
    private boolean isRefresh = true;
    int page = 1;
    private List<Address> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        refresh.setOnRefreshLoadMoreListener(this);
        addressAdapter = new AddressAdapter(null);
        addressRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        addressRecycle.setAdapter(addressAdapter);
        addressAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.item_address:
                        Intent intent = new Intent();
                        intent.putExtra("address", list.get(position));
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case R.id.edit_img:
                        startActivity(new Intent(getActivity(),InsertAddressActivity.class).putExtra("address",list.get(position)));
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh.autoRefresh(500);
    }

    @Override
    protected void initData() {

    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectAddressByUserid, Constant.selectAddressByUserid);

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

    @OnClick(R.id.add_address)
    public void onViewClicked() {
        startActivity(InsertAddressActivity.class);
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("userid", UserBean().getId());
        map.put("page", page);
        map.put("rows", 10);
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        List<Address> addresses = GsonUtils.getPersons(data, Address.class);
        if (isRefresh) {
            refresh.finishRefresh(300, true, addresses.size() < 10);
        } else {
            refresh.finishLoadMore(300, true, addresses.size() < 10);
        }
        list.addAll(addresses);
        addressAdapter.setNewData(list);
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {

    }
}
