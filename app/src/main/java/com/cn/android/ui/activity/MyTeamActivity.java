package com.cn.android.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.android.R;
import com.cn.android.bean.TeamBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.MyTeamAdapter;
import com.hjq.bar.TitleBar;
import com.hjq.image.ImageLoader;
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

public class MyTeamActivity extends MyActivity implements PublicInterfaceView, OnRefreshLoadMoreListener {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.top_view)
    ImageView topView;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.real_status)
    TextView realStatus;
    @BindView(R.id.info_huo)
    TextView infoHuo;
    @BindView(R.id.sum_huo)
    TextView sumHuo;
    @BindView(R.id.info_pp_num)
    TextView infoPpNum;
    @BindView(R.id.sum_pp_num)
    TextView sumPpNum;
    @BindView(R.id.team_info)
    ConstraintLayout teamInfo;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    MyTeamAdapter adapter;
    PublicInterfaceePresenetr presenetr;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private int page = 1;
    boolean isRefresh = true;
    private List<TeamBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myteam;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        adapter = new MyTeamAdapter(null);
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(adapter);
    }
    @Override
    protected void initData() {
        ImageLoader.with(getActivity()).load(UserBean().getHeadImg()).circle().into(userImg);
        userName.setText(UserBean().getNickname());
        realStatus.setText(UserBean().getIsReal() == 1 ? "已实名" : "未实名");
        infoHuo.setText(String.format("在线活跃度\n %s", UserBean().getOnlineHuoyueNum()));
        sumHuo.setText(String.format("总活跃度：%s", UserBean().getTotalHuoyueNum()));
        infoPpNum.setText(String.format("总人数\n %s", UserBean().getTotalPersionNum()));
        sumPpNum.setText(String.format("直推/V1人数：%s/%s", UserBean().getPersionNum(), UserBean().getV1PersionNum()));
        getdata();
    }
    public void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectDownUserList, Constant.selectDownUserList);
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
        map.put("login_userid", UserBean().getId());
        map.put("page", page);
        map.put("rows", 10);
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        List<TeamBean> teamBeans = GsonUtils.getPersons(data, TeamBean.class);
        if (isRefresh) {
            refresh.finishRefresh(300, true, teamBeans.size() < 10);
        } else {
            refresh.finishLoadMore(300, true, teamBeans.size() < 10);
        }
        list.addAll(teamBeans);
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
}
