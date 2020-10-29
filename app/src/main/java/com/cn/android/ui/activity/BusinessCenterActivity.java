package com.cn.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.MerchantsGoodsBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.MerchantsGoodsAdapter;
import com.cn.android.widget.GridSpacingItemDecoration;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.feature.main.container.ShootActivity;
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

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_RESULT_INFO;

public class BusinessCenterActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.m_order_tvm)
    TextView mOrderTvm;
    @BindView(R.id.order_yf)
    TextView orderYf;
    @BindView(R.id.order_all)
    TextView orderAll;
    @BindView(R.id.tv_push)
    TextView tvPush;
    @BindView(R.id.add_goods)
    TextView addGoods;
    @BindView(R.id.line1)
    LinearLayout line1;
    @BindView(R.id.mine_goods_tv)
    TextView mineGoodsTv;
    @BindView(R.id.m_mine_goods)
    TextView mMineGoods;
    @BindView(R.id.m_order_recycle)
    RecyclerView mOrderRecycle;
    MerchantsGoodsAdapter adapter;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1;
    PublicInterfaceePresenetr presenetr;
    private boolean isrefresh = true;
    List<MerchantsGoodsBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_business_center;
    }

    @Override
    protected void initView() {
        refresh.setOnRefreshLoadMoreListener(this);
        presenetr = new PublicInterfaceePresenetr(this);
        adapter = new MerchantsGoodsAdapter(null, false);
        mOrderRecycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mOrderRecycle.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));
        mOrderRecycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.goods_del:
                        ToastUtils.show("删除");
                        break;
                    case R.id.goods_et:
                        ToastUtils.show("编辑");
                        break;
                    case R.id.goods_adv_false:
                        startRecordVideoPage(position);
                        break;
                }
            }
        });
    }

    /**
     * 打开录制视频界面
     */
    private void startRecordVideoPage(int pos) {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        startActivityForResult(ShootActivity.class, new ActivityCallback() {
            @Override
            public void onActivityResult(int resultCode, @Nullable Intent data) {

                if (data != null) {
                    RecordVideoResultInfo recordVideoResultInfo = data.getParcelableExtra(EXTRA_RECORD_VIDEO_RESULT_INFO);
                    startActivity(new Intent(getActivity(), PreviewActivity.class).putExtra("path", recordVideoResultInfo.getFilePath()).putExtra("goods_id", list.get(pos).getId()));
                }
            }
        });


    }

    private void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectMyShopList, Constant.selectMyShopList);
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

    @OnClick({R.id.order_yf, R.id.order_all, R.id.add_goods, R.id.m_mine_goods})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_yf:
                startActivity(new Intent(this, OrderManageActivity.class).putExtra("type", 1));
                break;
            case R.id.order_all:
                startActivity(new Intent(this, OrderManageActivity.class).putExtra("type", 0));
                break;
            case R.id.add_goods:
                startActivity(ReleaseGoodsActivity.class);
                break;
            case R.id.m_mine_goods:
                startActivity(MerchansGoodsManagerActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh.autoRefresh(300);
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
        List<MerchantsGoodsBean> goodsBeans = GsonUtils.getPersons(data,MerchantsGoodsBean.class);
        if (isrefresh) {
            refresh.finishRefresh(300, true, goodsBeans.size() < 10);
        } else {
            refresh.finishLoadMore(300, true, goodsBeans.size() < 10);
        }
        list.addAll(goodsBeans);
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
        isrefresh = false;
        getdata();
    }
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        isrefresh = true;
        if(list.size()>0){
            list.clear();
        }
        getdata();
    }
}
