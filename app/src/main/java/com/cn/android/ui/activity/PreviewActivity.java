package com.cn.android.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.FileOperationPresenetr;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.FileOperationView;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.widget.VideoPlayer;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JzvdStd;

public class PreviewActivity extends MyActivity implements FileOperationView, PublicInterfaceView {
    public static String TYPE_VIDEO = "videoType";
    @BindView(R.id.jz_video)
    VideoPlayer jzVideo;
    @BindView(R.id.push_btn)
    TextView pushBtn;
    private String path;
    FileOperationPresenetr fileOperationPresenetr;
    PublicInterfaceePresenetr presenetr;
    private String goods_id,video_url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        fileOperationPresenetr=new FileOperationPresenetr(this);
        presenetr=new PublicInterfaceePresenetr(this);
        path = getIntent().getStringExtra("path");
        goods_id=getIntent().getStringExtra("goods_id");
        // 设置视频封面、url地址
        jzVideo.setUp(path, "", JzvdStd.SCREEN_STATE_OFF);
        jzVideo.posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // 设置饺子视频播放器播放的模式，根据父容器大小来展示视频内容
        jzVideo.setVideoImageDisplayType(JzvdStd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
        ImageLoader.with(getActivity()).load(path).into(jzVideo.posterImageView);
        jzVideo.startVideo();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void FileOperationSuccess(Object data, int tag) {
        showComplete();
//        ToastUtils.show(String.valueOf(data));
        video_url= (String) data;
        showLoading();
        presenetr.getPostTokenRequest(getActivity(),ServerUrl.addVideo, Constant.addVideo);

    }

    @Override
    public void FileOperationProgress(float progress, int tag) {

    }

    @Override
    public void FileOperationError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }

    @OnClick(R.id.push_btn)
    public void onViewClicked() {
        showLoading();
        fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(path), ServerUrl.upload, 101);
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map=new HashMap<>();
        map.put("authorization",UserBean().getId());
        map.put("id",goods_id);
        map.put("video_url",video_url);
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        finish();
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
