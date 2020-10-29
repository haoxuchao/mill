package com.cn.android.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cn.android.R;
import com.cn.android.bean.SubmitBean;
import com.cn.android.bean.UserBean;
import com.cn.android.bean.VideoBean;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.activity.CommitOrderActivity;
import com.cn.android.ui.activity.HomeActivity;
import com.cn.android.ui.activity.LoginActivity;
import com.cn.android.ui.adapter.VideoAdapter;
import com.cn.android.utils.SPUtils;
import com.cn.android.widget.VideoPlayer;
import com.cn.android.widget.ViewPagerLayoutManager;
import com.google.gson.Gson;
import com.hjq.toast.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.jzvd.JzvdStd;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目炫酷效果示例
 */
public final class TestFragmentA extends MyLazyFragment<HomeActivity> implements PublicInterfaceView, OnRefreshLoadMoreListener {


    @BindView(R.id.recycle)
    RecyclerView mRecycler;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefresh;
    VideoAdapter mAdapter;
    private int firstVisibleItem;
    private int lastVisibleItem;
    private UserBean userbean;
    private int page = 1;
    List<VideoBean> videolist = new ArrayList<>();
    boolean isRefresh = true;
    PublicInterfaceePresenetr presenetr;
    private int pos = 0;

    public static TestFragmentA newInstance() {
        return new TestFragmentA();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_a;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        // 给这个ToolBar设置顶部内边距，才能和TitleBar进行对齐
//        ImmersionBar.setTitleBar(getAttachActivity(), mToolbar);
        mAdapter = new VideoAdapter(null);
        mAdapter.setMlooklistener(new VideoAdapter.LookListener() {
            @Override
            public void onLook(int mpos) {
                pos = mpos;
                if (userbean != null)
                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.addVideoNum, Constant.addVideoNum);
            }
        });
//        urlList.add("http://jzvd.nathen.cn/video/1137e480-170bac9c523-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/e0bd348-170bac9c3b8-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/7bf938c-170bac9c18a-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/2f03c005-170bac9abac-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/477s88f38-170bac9ab8a-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/2d6ffe8f-170bac9ab87-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/633e0ce-170bac9ab65-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/2d6ffe8f-170bac9ab87-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/51f7552c-170bac98718-0007-1823-c86-de200.mp4");
//        urlList.add("http://jzvd.nathen.cn/video/2a101070-170bad88892-0007-1823-c86-de200.mp4");
        initRecycler();
        // 是否在加载完成时滚动列表显示新的内容
        mRefresh.setEnableScrollContentWhenLoaded(false);
        mRefresh.setOnRefreshLoadMoreListener(this);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (userbean != null) {
                    pos = position;
                    switch (view.getId()) {
                        case R.id.like_img:
                            showLoading();
                            presenetr.getPostTokenRequest(getActivity(), ServerUrl.updateParise, Constant.updateParise);
                            break;
                        case R.id.guanzhu:
                            showLoading();
                            presenetr.getPostTokenRequest(getActivity(), ServerUrl.addFocus, Constant.addFocus);
                            break;
                        case R.id.now_pay:
                            showLoading();
                            presenetr.getPostTokenRequest(getActivity(), ServerUrl.addOrder, Constant.addOrder);
                            break;
                        case R.id.car_img:
                            showLoading();
                            presenetr.getPostTokenRequest(getActivity(), ServerUrl.addCartOrder, Constant.addCartOrder);
                            break;
                    }
                } else {
                    startActivity(LoginActivity.class);
                }
            }
        });
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滚动
                        /**在这里执行，视频的自动播放与停止*/
                        autoPlayVideo(recyclerView);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        autoPlayVideo(recyclerView);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        JzvdStd.releaseAllVideos();
                        break;
                }
            }
        });
    }

    /**
     * 自动播放
     */
    private void autoPlayVideo(RecyclerView recyclerView) {
        if (firstVisibleItem == 0 && lastVisibleItem == 0 && recyclerView.getChildAt(0) != null) {
            VideoPlayer videoView = null;
            if (recyclerView != null && recyclerView.getChildAt(0) != null) {
                videoView = recyclerView.getChildAt(0).findViewById(R.id.jz_video);
            }
            if (videoView != null) {
                if (videoView.state == JzvdStd.STATE_NORMAL || videoView.state == JzvdStd.STATE_PAUSE) {
                    videoView.startVideo();
//                    videoView.init();
                }
            }
        }

        for (int i = 0; i <= lastVisibleItem; i++) {
            if (recyclerView == null || recyclerView.getChildAt(i) == null) {
                return;
            }
            VideoPlayer videoView = recyclerView.getChildAt(i).findViewById(R.id.jz_video);
            if (videoView != null) {

                Rect rect = new Rect();
                //获取视图本身的可见坐标，把值传入到rect对象中
                videoView.getLocalVisibleRect(rect);
                //获取视频的高度
                int videoHeight = videoView.getHeight();

                if (rect.top <= 100 && rect.bottom >= videoHeight) {
                    if (videoView.state == JzvdStd.STATE_NORMAL || videoView.state == JzvdStd.STATE_PAUSE) {
                        videoView.startVideo();
                    }
                    return;
                }
                JzvdStd.releaseAllVideos();
            } else {
                JzvdStd.releaseAllVideos();
            }
        }
    }

    /**
     * 在数据加载完成之后初始化recyclerView，避免没有加载到数据，上下滑动RecyclerView崩溃的问题
     */
    private void initRecycler() {
        ViewPagerLayoutManager layoutManager = new ViewPagerLayoutManager(getActivity(), OrientationHelper.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        getdata();
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            JzvdStd.releaseAllVideos();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.e("sss", "isVisibleToUser");
            JzvdStd.goOnPlayOnResume();
            userbean = new Gson().fromJson(SPUtils.getString(Constant.UserBean, ""), UserBean.class);
        } else {
            JzvdStd.goOnPlayOnPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        JzvdStd.releaseAllVideos();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        isRefresh = false;
        getdata();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isRefresh = true;
        page = 1;
        if (videolist.size() > 0) videolist.clear();
        getdata();
    }

    private void getdata() {
        showLoading();
        presenetr.getGetRequest(getActivity(), ServerUrl.selectVideoList, Constant.selectVideoList);
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.selectVideoList:
                if (userbean != null)
                    map.put("login_userid", userbean.getId());
                map.put("page", page);
                map.put("rows", 10);
                return map;
            case Constant.updateParise:
                map.put("authorization", userbean.getToken());
                map.put("videoid", videolist.get(pos).getId());//视频id
                map.put("login_userid", userbean.getId());
                return map;
            case Constant.addFocus:
                map.put("authorization", userbean.getToken());
                map.put("userid", videolist.get(pos).getUserid());//视频用户id
                map.put("login_userid", userbean.getId());
                return map;
            case Constant.addOrder:
            case Constant.addCartOrder:
                map.put("authorization", userbean.getToken());
                map.put("userid", userbean.getId());
                map.put("shopid", videolist.get(pos).getShopid());
                map.put("num", 1);
                return map;
            case Constant.addVideoNum:
                map.put("authorization", userbean.getToken());
                map.put("login_userid", userbean.getId());
                map.put("videoid", videolist.get(pos).getId());
                return map;

        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.addFocus:
                for (int i = 0; i < videolist.size(); i++) {
                    if (videolist.get(i).getNickname().equals(videolist.get(pos).getNickname())) {
                        videolist.get(i).setIs_focus(1);
                        ImageView imageView = (ImageView) mAdapter.getViewByPosition(mRecycler, i, R.id.guanzhu);
                        if (imageView != null) {
                            imageView.setVisibility(View.INVISIBLE);
                        } else {
                            mAdapter.notifyItemChanged(i);
                        }
                    }
                }
                break;
            case Constant.updateParise:
                videolist.get(pos).setIs_praise(videolist.get(pos).getIs_praise() == 1 ? 2 : 1);
                ImageView imageView = (ImageView) mAdapter.getViewByPosition(mRecycler, pos, R.id.like_img);
                TextView tvLike = (TextView) mAdapter.getViewByPosition(mRecycler, pos, R.id.like_num);
                if (videolist.get(pos).getIs_praise() == 1) {
                    imageView.setImageResource(R.mipmap.aixin_red);
                } else {
                    imageView.setImageResource(R.mipmap.shoucang);
                }
                tvLike.setText(data);
                videolist.get(pos).setPraise_num(data);
                break;
            case Constant.selectVideoList:
                List<VideoBean> videoBeans = GsonUtils.getPersons(data, VideoBean.class);
                if (isRefresh) {
                    mRefresh.finishRefresh(300, true, videoBeans.size() < 10);
                } else {
                    mRefresh.finishLoadMore(300, true, videoBeans.size() < 10);
                }
                videolist.addAll(videoBeans);
                mAdapter.setNewData(videolist);
                break;
            case Constant.addOrder:
                startActivity(new Intent(getActivity(), CommitOrderActivity.class).putExtra("sub_bean", GsonUtils.getPerson(data, SubmitBean.class)));
                break;
            case Constant.addCartOrder:
                for (int i = 0; i < videolist.size(); i++) {
                    TextView tv = (TextView) mAdapter.getViewByPosition(mRecycler, i, R.id.car_num);
                    mAdapter.setShowThis(true, data);
                    if (tv != null) {
                        tv.setText(data);
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        mAdapter.notifyItemChanged(i);
                    }

                }
                ToastUtils.show("加入购物车成功");
                break;
        }

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        if (tag != Constant.addVideoNum)
            ToastUtils.show(error);
    }
}