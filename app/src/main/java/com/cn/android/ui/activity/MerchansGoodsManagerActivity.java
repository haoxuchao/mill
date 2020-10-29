package com.cn.android.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
import com.cn.android.ui.dialog.DoubleBtnPromptDialog;
import com.cn.android.ui.dialog.PromptDialog;
import com.cn.android.widget.GridSpacingItemDecoration;
import com.hjq.bar.TitleBar;
import com.hjq.dialog.base.BaseDialog;
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

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_RESULT_INFO;

public class MerchansGoodsManagerActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    MerchantsGoodsAdapter adapter;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    PublicInterfaceePresenetr presenetr;
    private int page = 1;
    private boolean isRefresh = true;
    List<MerchantsGoodsBean> merchantslist = new ArrayList<>();
    private int pos = 0;
    MerchantsGoodsBean delBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchans_goods_manager;
    }

    @Override
    protected void initView() {
        refresh.setOnRefreshLoadMoreListener(this);
        presenetr = new PublicInterfaceePresenetr(this);
        adapter = new MerchantsGoodsAdapter(merchantslist, true);
        recycle.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycle.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));
        recycle.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                pos = position;
                switch (view.getId()) {
                    case R.id.goods_del:
                        new DoubleBtnPromptDialog.Builder(getActivity()).setMessage("是否删除此商品").setCancel("否").setConfirm("是").setmOnListener(new DoubleBtnPromptDialog.OnListener() {
                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                dialog.dismiss();
                                delBean = merchantslist.get(pos);
                                showLoading();
                                presenetr.getPostTokenRequest(getActivity(),ServerUrl.delShop,Constant.delShop);
                            }
                        }).show();
                        break;
                    case R.id.goods_et:
                        startActivity(new Intent(getActivity(),ReleaseGoodsActivity.class).putExtra("goods",merchantslist.get(position)));
                        break;
                    case R.id.goods_adv_false:
                        startRecordVideoPage(pos);
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
                    startActivity(new Intent(getActivity(), PreviewActivity.class).putExtra("path", recordVideoResultInfo.getFilePath()).putExtra("goods_id", merchantslist.get(pos).getId()));
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

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.selectMyShopList:
                map.put("login_userid", UserBean().getId());
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.delShop:
                map.put("authorization", UserBean().getToken());
                map.put("id", merchantslist.get(pos).getId());
                return map;
        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectMyShopList:
                List<MerchantsGoodsBean> merchantsGoodsBeans = GsonUtils.getPersons(data, MerchantsGoodsBean.class);
                if (isRefresh) {
                    refresh.finishRefresh(300, true, merchantsGoodsBeans.size() < 10);
                } else {
                    refresh.finishLoadMore(300, true, merchantsGoodsBeans.size() < 10);
                }
                merchantslist.addAll(merchantsGoodsBeans);
                adapter.notifyDataSetChanged();
                break;
            case Constant.delShop:
                merchantslist.remove(delBean);
                adapter.notifyDataSetChanged();
                ToastUtils.show("删除成功");
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
        if(merchantslist.size()>0)merchantslist.clear();
        getdata();
    }
}
