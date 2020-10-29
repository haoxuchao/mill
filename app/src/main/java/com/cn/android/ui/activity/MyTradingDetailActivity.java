package com.cn.android.ui.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.TradOrderDetail;
import com.cn.android.bean.TradingOrder;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.hjq.bar.TitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTradingDetailActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.ask_num_tv)
    TextView askNumTv;
    @BindView(R.id.price_one_tv)
    TextView priceOneTv;
    @BindView(R.id.c_view1)
    LinearLayout cView1;
    @BindView(R.id.name_tv)
    TextView nameTv;
    @BindView(R.id.shou_num_tv)
    TextView shouNumTv;
    @BindView(R.id.sum_price_tv)
    TextView sumPriceTv;
    @BindView(R.id.c_view2)
    LinearLayout cView2;
    @BindView(R.id.order_info_tv)
    TextView orderInfoTv;
    @BindView(R.id.order_no)
    TextView orderNo;
    @BindView(R.id.order_ctime)
    TextView orderCtime;
    @BindView(R.id.order_ptime)
    TextView orderPtime;
    @BindView(R.id.order_push_time)
    TextView orderPushTime;
    @BindView(R.id.c_view3)
    LinearLayout cView3;
    private String id;
    private int type;
    PublicInterfaceePresenetr presenetr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trading_detail;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type", 1);

    }

    @Override
    protected void initData() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectAccountDetials, Constant.selectAccountDetials);
    }
    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);
        return map;
    }
    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        TradOrderDetail order = GsonUtils.getPerson(data, TradOrderDetail.class);
        setdata(order);
    }
    private void setdata(TradOrderDetail order) {
        askNumTv.setText(String.format("求购数量：%s-%s",order.getMin_bi_num(),order.getMax_bi_num()));
        priceOneTv.setText(String.format("求购单价：%sCNY",order.getBi_one_money()));
        nameTv.setText(String.format("出售人：%s",order.getNickname()));
        shouNumTv.setText(String.format("出售数量：%s",order.getBi_num()));
        sumPriceTv.setText(String.format("共计：%sCNY",order.getTotal_money()));
        orderNo.setText(String.format("订单编号：%s",order.getOrdercode()));
        orderCtime.setText(String.format("创建时间：%s",order.getCtime()));
        orderPtime.setText(String.format("付款时间：%s",order.getPay_time()));
        orderPushTime.setText(String.format("发布时间：%s",order.getAdd_time()));
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
