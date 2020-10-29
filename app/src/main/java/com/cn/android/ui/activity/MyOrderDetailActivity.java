package com.cn.android.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.android.R;
import com.cn.android.bean.OrderBean;
import com.cn.android.common.MyActivity;
import com.cn.android.ui.adapter.OrderGoodsAdapter;
import com.hjq.bar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyOrderDetailActivity extends MyActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.address_user_name)
    TextView addressUserName;
    @BindView(R.id.address_user_phone)
    TextView addressUserPhone;
    @BindView(R.id.address_detail)
    TextView addressDetail;
    @BindView(R.id.address_c)
    ConstraintLayout addressC;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.prompt1)
    TextView prompt1;
    @BindView(R.id.b_price)
    TextView bPrice;
    @BindView(R.id.the_end_tv)
    TextView theEndTv;
    @BindView(R.id.confirm)
    TextView confirm;
    @BindView(R.id.confirm_send)
    TextView confirmSend;
    @BindView(R.id.item)
    ConstraintLayout item;
    @BindView(R.id.info_tv)
    TextView infoTv;
    @BindView(R.id.order_no)
    TextView orderNo;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.pay_time)
    TextView payTime;
    @BindView(R.id.send_time)
    TextView sendTime;
    @BindView(R.id.order_info_c)
    ConstraintLayout orderInfoC;
    OrderBean orderBean;
    OrderGoodsAdapter orderGoodsAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }
    @Override
    protected void initView() {
        orderBean = (OrderBean) getIntent().getSerializableExtra("order");
        if (orderBean != null) {
            addressUserName.setText(orderBean.getUsername());
            addressUserPhone.setText(orderBean.getUserphne());
            addressDetail.setText(orderBean.getAddress());
            orderGoodsAdapter = new OrderGoodsAdapter(orderBean.getShopList());
            recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycle.setAdapter(orderGoodsAdapter);
            recycle.setNestedScrollingEnabled(false);
            prompt1.setText(String.format("共%s件商品，总计：", orderBean.getShop_total_num()));
            bPrice.setText(String.format("%sEB", orderBean.getShop_total_eb()));
            orderNo.setText(String.format("订单编号：%s", orderBean.getOrdercode()));
            createTime.setText(String.format("创建时间：%s", orderBean.getCtime()));
            if (orderBean.getStatus() != 1)
                sendTime.setText(String.format("发货时间：%s", orderBean.getSend_time()));
            else {
                sendTime.setVisibility(View.GONE);
            }
        }
    }
    @Override
    protected void initData() {}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
