package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.SubmitBean;
import com.cn.android.bean.shopCarBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.ShopCarAdapter;
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
import butterknife.OnClick;

public class ShopCarActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.shop_cart_tv)
    TextView shopCartTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.top_bar)
    ConstraintLayout topBar;
    @BindView(R.id.all_check)
    TextView allCheck;
    @BindView(R.id.btn_delete)
    TextView btnDelete;
    @BindView(R.id.jies_btn)
    TextView jiesBtn;
    @BindView(R.id.cars_prompt_tv)
    TextView carsPromptTv;
    @BindView(R.id.sum_money)
    TextView sumMoney;
    boolean isManager = true;
    boolean isAll = false;
    boolean isRefresh = true;
    List<shopCarBean> goodsBeans = new ArrayList<>();
    @BindView(R.id.car_recycle)
    RecyclerView carRecycle;
    @BindView(R.id.bottom_c)
    ConstraintLayout bottomC;
    ShopCarAdapter adapter;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1;
    PublicInterfaceePresenetr presenetr;
    private int pos;
    private boolean isjia;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_cart;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        ImmersionBar.setTitleBar(this, topBar);
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new ShopCarAdapter(null);
        carRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        carRecycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pos = position;
                switch (view.getId()) {
                    case R.id.check:
                        goodsBeans.get(position).setCheck(!goodsBeans.get(position).isCheck());
                        setBottomPrice();
                        break;
                    case R.id.jia_btn:
                        isjia = true;
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.updateCartOrderNum, Constant.updateCartOrderNum);
                        break;
                    case R.id.jian_btn:
                        isjia=false;
                        if (goodsBeans.get(position).getShop_num() < 2) {
                            ToastUtils.show("数量最少为1");
                        } else {
                            showLoading();
                            presenetr.getPostTokenRequest(getActivity(), ServerUrl.updateCartOrderNum, Constant.updateCartOrderNum);
                        }
                        break;
                }
                adapter.notifyItemChanged(position);
            }
        });
    }

    private String setBottomPrice() {
        int sumNum = 0;
        int sumPrice = 0;
        List<String> sb=new ArrayList<>();
        for (int i=0;i<goodsBeans.size(); i++) {
            if (goodsBeans.get(i).isCheck()) {
                sb.add(goodsBeans.get(i).getId());
                sumNum += goodsBeans.get(i).getShop_num();
                sumPrice = (goodsBeans.get(i).getShop_num() * goodsBeans.get(i).getShop_eb()) + sumPrice;
            }
        }
        carsPromptTv.setText(String.format("共%s件，合计:", sumNum));
        sumMoney.setText(String.format("%sEB", sumPrice));
        return new Gson().toJson( sb );

    }

    @Override
    protected void initData() {
        getdata();
    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectCartOrderList, Constant.selectCartOrderList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.right_tv, R.id.all_check, R.id.btn_delete, R.id.jies_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right_tv:
                rightTv.setText(isManager ? "完成" : "管理");
                btnDelete.setVisibility(isManager ? View.VISIBLE : View.GONE);
                jiesBtn.setVisibility(!isManager ? View.VISIBLE : View.GONE);
                carsPromptTv.setVisibility(!isManager ? View.VISIBLE : View.GONE);
                sumMoney.setVisibility(!isManager ? View.VISIBLE : View.GONE);
                isManager = !isManager;
                break;
            case R.id.all_check:
                isAll = !isAll;
                allCheck.setSelected(isAll);
                for (shopCarBean item : goodsBeans) {
                    item.setCheck(isAll);
                }
                setBottomPrice();
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_delete:
                showLoading();
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.delCartOrder, Constant.delCartOrder);
                break;
            case R.id.jies_btn:
                showLoading();
                presenetr.getPostTokenRequest(getActivity(), ServerUrl.saveOrder, Constant.saveOrder);
                break;
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.selectCartOrderList:
                map.put("login_userid", UserBean().getId());
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.updateCartOrderNum:
                map.put("authorization", UserBean().getToken());
                map.put("orderid", goodsBeans.get(pos).getId());
                map.put("num", goodsBeans.get(pos).getShop_num());
                return map;
            case Constant.delCartOrder:
                map.put("authorization", UserBean().getToken());
                map.put("type", isAll ? 1 : 2);
                map.put("userid", UserBean().getId());
                if (!isAll) {
                    map.put("orderids",setBottomPrice());
                }
                return map;
            case Constant.saveOrder:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("ids", setBottomPrice());
                return map;
        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectCartOrderList:
                List<shopCarBean> shopCarBeans = GsonUtils.getPersons(data, shopCarBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(300, true, shopCarBeans.size() < 10);
                } else {
                    refresh.finishLoadMore(300, true, shopCarBeans.size() < 10);
                }
                goodsBeans.addAll(shopCarBeans);
                adapter.setNewData(goodsBeans);
                setBottomPrice();
                break;
            case Constant.updateCartOrderNum:
                if (isjia) {
                    goodsBeans.get(pos).setShop_num(goodsBeans.get(pos).getShop_num() + 1);
                } else {
                    goodsBeans.get(pos).setShop_num(goodsBeans.get(pos).getShop_num() - 1);
                }
                setBottomPrice();
                adapter.notifyItemChanged(pos);
                break;
            case Constant.delCartOrder:
                onRefresh(refresh);
                break;
            case Constant.saveOrder:
                startActivity(new Intent(getActivity(),CommitOrderActivity.class).putExtra("sub_bean",GsonUtils.getPerson(data, SubmitBean.class)));
                break;
        }
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
        if (goodsBeans.size() > 0) {
            goodsBeans.clear();
        }
        getdata();
    }
}
