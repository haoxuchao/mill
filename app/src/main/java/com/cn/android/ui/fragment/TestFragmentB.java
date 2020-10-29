package com.cn.android.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.UserBean;
import com.cn.android.bean.shopBean;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.activity.GoodsDetailActivity;
import com.cn.android.ui.activity.HomeActivity;
import com.cn.android.ui.activity.LoginActivity;
import com.cn.android.ui.activity.OpenStoreActivity;
import com.cn.android.ui.activity.SeachActivity;
import com.cn.android.ui.activity.ShopCarActivity;
import com.cn.android.ui.activity.TradingActivity;
import com.cn.android.ui.adapter.NewsGoodsAdapter;
import com.cn.android.utils.SPUtils;
import com.cn.android.widget.GridSpacingItemDecoration;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.toast.ToastUtils;
import com.hjq.widget.view.SwitchButton;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目自定义控件展示
 */
public final class TestFragmentB extends MyLazyFragment<HomeActivity>
        implements PublicInterfaceView, OnRefreshLoadMoreListener {


    @BindView(R.id.edit_seach)
    EditText editSeach;
    @BindView(R.id.btn_seach)
    TextView btnSeach;
    @BindView(R.id.gwc_img)
    ImageView gwcImg;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.open_tv)
    TextView openTv;
    @BindView(R.id.shengq_tv)
    TextView shengqTv;
    @BindView(R.id.open_shops_img)
    ImageView openShopsImg;
    @BindView(R.id.view_1)
    ConstraintLayout view1;
    @BindView(R.id.jiaoyi_tv)
    TextView jiaoyiTv;
    @BindView(R.id.jion_tv)
    TextView jionTv;
    @BindView(R.id.jiaoyi_img)
    ImageView jiaoyiImg;
    @BindView(R.id.view_2)
    ConstraintLayout view2;
    @BindView(R.id.news_goods_tv)
    TextView newsGoodsTv;
    @BindView(R.id.news_goods_recycle)
    RecyclerView newsGoodsRecycle;
    NewsGoodsAdapter adapter;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    boolean isRefresh = true;
    private int page = 1;
    private List<shopBean> list = new ArrayList<>();
    PublicInterfaceePresenetr presenetr;
    UserBean userBean;

    public static TestFragmentB newInstance() {
        return new TestFragmentB();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_b;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        ImmersionBar.setTitleBar(this, titleBar);
        refresh.setOnRefreshLoadMoreListener(this);
        adapter = new NewsGoodsAdapter(null);
        newsGoodsRecycle.setNestedScrollingEnabled(true);
        newsGoodsRecycle.setLayoutManager(new GridLayoutManager(getContext(), 2));
        newsGoodsRecycle.addItemDecoration(new GridSpacingItemDecoration(2, 30, true));
        newsGoodsRecycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (userBean != null) {
                    startActivity(new Intent(getActivity(), GoodsDetailActivity.class).putExtra("id", list.get(position).getId()));
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
    }


    @Override
    protected void initData() {
        getdata();
    }

    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectNewShopList, Constant.selectNewShopList);
    }


    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    /**
     * {@link SwitchButton.OnCheckedChangeListener}
     */


    @OnClick({R.id.btn_seach, R.id.gwc_img, R.id.view_1, R.id.view_2})
    public void onViewClicked(View view) {
        if (userBean != null) {
            switch (view.getId()) {
                case R.id.btn_seach:
                    startActivity(new Intent(getActivity(), SeachActivity.class).putExtra("word", editSeach.getText().toString().trim()));
                    break;
                case R.id.gwc_img:
                    startActivity(ShopCarActivity.class);
                    break;
                case R.id.view_1:
                    if (userBean.getIsOpen() == 1) {
                        ToastUtils.show("已开通商铺");
                    } else if (userBean.getIsOpen() == 0) {
                        ToastUtils.show("申请审核中");
                    } else {
                        startActivity(OpenStoreActivity.class);
                    }
                    break;
                case R.id.view_2:
                    if (userBean.getIsActivation() != 1) {
                        ToastUtils.show("未激活");
                    } else {
                        startActivity(TradingActivity.class);
                    }
                    break;
            }
        } else {
            startActivity(LoginActivity.class);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        isRefresh = false;
        getdata();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (list.size() > 0) list.clear();
        page = 1;
        isRefresh = true;
        getdata();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            userBean = GsonUtils.getPerson(SPUtils.getString(Constant.UserBean, ""), UserBean.class);
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
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
}