package com.mingyuechunqiu.recordermanager.feature.main.container;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;



import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;

import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.ToastUtils;
import com.mingyuechunqiu.recordermanager.R;

import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoRequestOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoResultInfo;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;

import com.mingyuechunqiu.recordermanager.feature.main.detail.RecordVideoFragment;
import com.mingyuechunqiu.recordermanager.framework.RMOnRecordVideoListener;
import com.mingyuechunqiu.recordermanager.ui.activity.BaseRecordVideoActivity;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;
import com.mingyuechunqiu.recordermanager.util.FilePathUtils;
import com.mingyuechunqiu.recordermanager.util.GlideEngine;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.util.List;


import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.DEFAULT_RECORD_VIDEO_DURATION;
import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_REQUEST_OPTION;
import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.EXTRA_RECORD_VIDEO_RESULT_INFO;

public class ShootActivity extends BaseRecordVideoActivity implements EasyPermissions.PermissionCallbacks, RMOnRecordVideoListener{

    private List<LocalMedia> mSelected;
    private String path;
    private String goods_id;


    // // 进入相册 以下是例子：不需要的api可以不写
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        path = PictureSelector.obtainMultipleResult(data).get(0).getPath();
                        MediaPlayer player = new MediaPlayer();
                        player.setDataSource(path);
                        player.prepare();
                        int mediaDuration = player.getDuration();
                        int length = (mediaDuration / 1000);
                        if (length > 31) {
                            ToastUtils.s(this,"请选择30秒以内的视频");
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_RECORD_VIDEO_RESULT_INFO, new RecordVideoResultInfo.Builder()
                                    .setDuration(mediaDuration)
                                    .setFilePath(path)
                                    .build());

                            setResult(RESULT_OK, intent);
                            finishActivity();
//                            PreviewActivity.start(this, path, PreviewActivity.TYPE_VIDEO, 4);
                        }

                    } catch (Exception e) {

                    }
                }
                break;
        }
    }

    private RecordVideoFragment mRecordVideoFg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rm_activity_record_video);
        if (!RecordPermissionUtils.checkRecordPermissions(this)) {
            finishActivity();
            return;
        }
        mRecordVideoFg = RecordVideoFragment.newInstance(initRecordVideoOption());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_record_video_container, mRecordVideoFg,
                        RecordVideoFragment.class.getSimpleName())
                .commitAllowingStateLoss();
        mRecordVideoFg.setSelectListener(new RecordVideoFragment.SelectListener() {
            @Override
            public void SelectVideo() {
                PictureSelector.create(ShootActivity.this)
                        .openGallery(PictureMimeType.ofVideo())
                        .theme(R.style.picture_default_style)
                        .selectionMode(PictureConfig.SINGLE)
                        .isCamera(false)
                        .glideOverride(160, 160)
                        .previewEggs(true)
                        .selectionMedia(mSelected)
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .forResult(PictureConfig.CHOOSE_REQUEST);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRecordVideoFg != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mRecordVideoFg)
                    .commitAllowingStateLoss();
        }
        mRecordVideoFg = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        RecordPermissionUtils.handleOnPermissionDenied(this);
    }

    @Override
    public void onCompleteRecordVideo(@Nullable String filePath, int videoDuration) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECORD_VIDEO_RESULT_INFO, new RecordVideoResultInfo.Builder()
                .setDuration(videoDuration)
                .setFilePath(filePath)
                .build());
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void onClickConfirm(@Nullable String filePath, int videoDuration) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RECORD_VIDEO_RESULT_INFO, new RecordVideoResultInfo.Builder()
                .setDuration(videoDuration)
                .setFilePath(filePath)
                .build());
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void onClickCancel(@Nullable String filePath, int videoDuration) {
    }

    @Override
    public void onClickBack() {
        finishActivity();
    }

    /**
     * 获取计时控件
     *
     * @return 返回计时AppCompatTextView
     */
    protected AppCompatTextView getTimingView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getTimingView();
    }

    /**
     * 获取圆形进度按钮
     *
     * @return 返回进度CircleProgressButton
     */
    protected CircleProgressButton getCircleProgressButton() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getCircleProgressButton();
    }

    /**
     * 获取翻转摄像头控件
     *
     * @return 返回翻转摄像头AppCompatImageView
     */
    protected AppCompatImageView getFlipCameraView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getFlipCameraView();
    }

    /**
     * 获取播放控件
     *
     * @return 返回播放AppCompatImageView
     */
    protected AppCompatImageView getPlayView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getPlayView();
    }

    /**
     * 获取取消控件
     *
     * @return 返回取消AppCompatImageView
     */
    protected AppCompatImageView getCancelView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getCancelView();
    }

    /**
     * 获取确认控件
     *
     * @return 返回确认AppCompatImageView
     */
    protected AppCompatImageView getConfirmView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getConfirmView();
    }

    /**
     * 获取返回控件
     *
     * @return 返回返回AppCompatImageView
     */
    protected AppCompatImageView getBackView() {
        return mRecordVideoFg == null ? null : mRecordVideoFg.getBackView();
    }

    @NonNull
    private RecordVideoOption initRecordVideoOption() {
        String filePath = null;
        int maxDuration = DEFAULT_RECORD_VIDEO_DURATION;

        Intent intent = getIntent();
        if (intent != null) {
            RecordVideoRequestOption option = intent.getParcelableExtra(EXTRA_RECORD_VIDEO_REQUEST_OPTION);
            if (option != null && option.getRecordVideoOption() != null) {
                return option.getRecordVideoOption();
            }
            maxDuration = option == null ? DEFAULT_RECORD_VIDEO_DURATION : option.getMaxDuration();
            filePath = option == null ? null : option.getFilePath();
        }

        if (maxDuration < 1) {
            maxDuration = DEFAULT_RECORD_VIDEO_DURATION;
        }
        if (TextUtils.isEmpty(filePath)) {
            filePath = FilePathUtils.getSaveFilePath(this);
        }

        return new RecordVideoOption.Builder()
                .setRecorderOption(new RecorderOption.Builder()
                        .buildDefaultVideoBean(filePath))
                .setMaxDuration(maxDuration)
                .build();
    }

    /**
     * 销毁界面
     */
    private void finishActivity() {
        finish();
        overridePendingTransition(R.anim.rm_fix_stand, R.anim.rm_slide_out_bottom);
    }

}
