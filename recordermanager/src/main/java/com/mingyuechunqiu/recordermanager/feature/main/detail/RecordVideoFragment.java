package com.mingyuechunqiu.recordermanager.feature.main.detail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.mingyuechunqiu.recordermanager.R;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoButtonOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecordVideoOption;
import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;
import com.mingyuechunqiu.recordermanager.data.constants.KeyPrefixConstants;
import com.mingyuechunqiu.recordermanager.feature.main.container.RecordVideoActivity;
import com.mingyuechunqiu.recordermanager.framework.RMKeyBackCallback;
import com.mingyuechunqiu.recordermanager.framework.RMOnRecordVideoListener;
import com.mingyuechunqiu.recordermanager.ui.fragment.BasePresenterFragment;
import com.mingyuechunqiu.recordermanager.ui.widget.CircleProgressButton;
import com.mingyuechunqiu.recordermanager.util.FilePathUtils;
import com.mingyuechunqiu.recordermanager.util.RecordPermissionUtils;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mingyuechunqiu.recordermanager.data.constants.RecorderManagerConstants.DEFAULT_RECORD_VIDEO_DURATION;

/**
 * <pre>
 *     author : xyj
 *     Github : https://github.com/MingYueChunQiu
 *     e-mail : yujie.xi@ehailuo.com
 *     time   : 2019/1/28
 *     desc   : 录制视频界面
 *              继承自BasePresenterFragment
 *     version: 1.0
 * </pre>
 */
public class RecordVideoFragment extends BasePresenterFragment<RecordVideoContract.View<RecordVideoContract.Presenter<?>>, RecordVideoContract.Presenter<?>>
        implements RecordVideoContract.View<RecordVideoContract.Presenter<?>>, View.OnClickListener, SurfaceHolder.Callback, EasyPermissions.PermissionCallbacks {

    private static final String BUNDLE_EXTRA_RECORD_VIDEO_OPTION = KeyPrefixConstants.KEY_BUNDLE + "record_video_option";

    private SurfaceView svVideo;
    private AppCompatTextView tvTiming;
    private CircleProgressButton cpbRecord;
    private AppCompatImageView ivFlipCamera, ivPlay, ivCancel, ivConfirm, ivBack;
    private AppCompatImageView ivFlashlight, xcimg;
    private RecordVideoOption mOption;
    private boolean isSurfaceHolderDestroyed;//标记SurfaceHolder是否被销毁了
    private SelectListener selectListener;

    public void setSelectListener(SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rm_fragment_record_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkHasPermissions();
        initRecorderOption();

        mPresenter.initConfiguration(mOption);
        svVideo = view.findViewById(R.id.sv_record_video_screen);
        xcimg = view.findViewById(R.id.iv_camera);
        tvTiming = view.findViewById(R.id.tv_record_video_timing);
        cpbRecord = view.findViewById(R.id.cpb_record_video_record);
        ivFlipCamera = view.findViewById(R.id.iv_record_video_flip_camera);
        ivPlay = view.findViewById(R.id.iv_record_video_play);
        ivCancel = view.findViewById(R.id.iv_record_video_cancel);
        ivConfirm = view.findViewById(R.id.iv_record_video_confirm);
        ivBack = view.findViewById(R.id.iv_record_video_back);
        ivFlashlight = view.findViewById(R.id.iv_record_video_flashlight);
        svVideo.getHolder().addCallback(this);
        svVideo.getHolder().setKeepScreenOn(true);
        svVideo.setOnClickListener(this);
        xcimg.setOnClickListener(this);
        ivFlipCamera.setVisibility(mOption.isHideFlipCameraButton() ? View.GONE : View.VISIBLE);
        xcimg.setVisibility(mOption.isHideFlipCameraButton() ? View.GONE : View.VISIBLE);
        ivFlipCamera.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivConfirm.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvTiming.setText(mPresenter == null ? "" : mPresenter.getTimingHint("00", false));
        ivFlashlight.setVisibility(mOption.isHideFlashlightButton() ? View.GONE : View.VISIBLE);
        ivFlashlight.setOnClickListener(this);
        initCircleProgressButton(mOption.getRecordVideoButtonOption());
        cpbRecord.setMaxProgress(mOption.getMaxDuration());
        cpbRecord.setOnCircleProgressButtonListener(new CircleProgressButton.OnCircleProgressButtonListener() {
            @Override
            public boolean onPreProgress(CircleProgressButton v) {
                if (!checkHasPermissions() || mPresenter == null || ivFlipCamera == null || ivBack == null) {
                    return false;
                }
                return mPresenter.pressToStartRecordVideo(svVideo.getHolder(), ivFlipCamera, ivFlashlight, xcimg);
            }

            @Override
            public void onProgress(CircleProgressButton v, float progress) {
            }

            @Override
            public int onReleaseProgress(CircleProgressButton v) {
                if (mPresenter != null) {
                    mPresenter.releaseToStopRecordVideo(false);
                }
                if (cpbRecord != null) {
                    cpbRecord.reset();
                }
                return 360;
            }

            @Override
            public void onCancelProgress(CircleProgressButton v) {
                if (mPresenter != null) {
                    mPresenter.releaseToStopRecordVideo(false);
                }
            }
        });

        if (getActivity() instanceof RMKeyBackCallback) {
            ((RMKeyBackCallback) getActivity()).addOnKeyBackListener(new RecordVideoActivity.OnKeyBackListener() {
                @Override
                public boolean onClickKeyBack(KeyEvent event) {
                    if (mPresenter != null) {
                        mPresenter.onClickBack();
                        return true;
                    }
                    return false;
                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        //在红米Note5A上，锁屏没有调用surfaceDestroyed方法，所以要加以判断
        if (!isSurfaceHolderDestroyed && mPresenter != null && ivPlay != null && svVideo != null) {
            mPresenter.resumePlayVideo(false, ivPlay, svVideo.getHolder());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null && ivPlay != null) {
            mPresenter.pausePlayVideo(false, ivPlay);
        }
        //此时闪光灯会被切断，要重置状态
        if (ivFlashlight != null) {
            ivFlashlight.setSelected(false);
        }
    }

    @Override
    protected RecordVideoContract.Presenter<?> initPresenter() {
        return new RecordVideoPresenter();
    }

    @Override
    protected void releaseOnDestroyView() {
    }

    @Override
    protected void releaseOnDestroy() {
        mOption = null;
        isSurfaceHolderDestroyed = false;
    }

    @Override
    public void onClick(View v) {
        //库需要使用if else，使用switch判断id会导致问题
        int id = v.getId();
        if (id == R.id.sv_record_video_screen) {
            if (mPresenter != null & ivPlay != null && svVideo != null) {
                mPresenter.controlPlayOrPauseVideo(ivPlay, svVideo.getHolder());
            }
        } else if (id == R.id.iv_record_video_flip_camera) {
            flipCamera();
        } else if (id == R.id.iv_record_video_play) {
            if (mPresenter != null && ivPlay != null && svVideo != null) {
                mPresenter.resumePlayVideo(true, ivPlay, svVideo.getHolder());
            }
        } else if (id == R.id.iv_record_video_cancel) {
            if (mPresenter != null) {
                mPresenter.resetResource();
                mPresenter.onClickCancel();
            }
        } else if (id == R.id.iv_record_video_confirm) {
            if (mPresenter != null) {
                mPresenter.onClickConfirm();
            }
        } else if (id == R.id.iv_record_video_back) {
            if (mPresenter != null) {
                mPresenter.onClickBack();
            }
        } else if (id == R.id.iv_record_video_flashlight) {
            switchFlashlightState();
        } else if (id == R.id.iv_camera) {
            if (selectListener != null)
                selectListener.SelectVideo();
//            mPresenter.selectCamera();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mPresenter != null && holder != null && ivPlay != null) {
            mPresenter.onSurfaceCreated(holder, ivPlay);
        }
        isSurfaceHolderDestroyed = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        holder.setKeepScreenOn(true);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mPresenter != null) {
            mPresenter.releaseCamera();
        }
        isSurfaceHolderDestroyed = true;
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
    public Context getCurrentContext() {
        return getContext();
    }

    @Override
    public void showTimingText(@NonNull String text) {
        if (tvTiming == null) {
            return;
        }
        tvTiming.setText(text);
    }

    @Nullable
    @Override
    public SurfaceHolder getSurfaceHolder() {
        return svVideo == null ? null : svVideo.getHolder();
    }

    @Override
    public void controlRecordOrPlayVisibility(boolean isInPlayingState) {

        if (tvTiming == null || ivFlipCamera == null || ivPlay == null || ivCancel == null ||
                ivConfirm == null || cpbRecord == null || ivBack == null || ivFlashlight == null) {
            return;
        }
        int playViewsVisibility, recordViewsVisibility;
        if (isInPlayingState) {
            playViewsVisibility = View.VISIBLE;
            recordViewsVisibility = View.GONE;
        } else {
            playViewsVisibility = View.GONE;
            recordViewsVisibility = View.VISIBLE;
            if (mPresenter != null) {
                showTimingText(mPresenter.getTimingHint("00", false));
            }
            if (!mOption.isHideFlipCameraButton()) {
                ivFlipCamera.setVisibility(recordViewsVisibility);
            }
            if (!mOption.isHideFlashlightButton()) {
                ivFlashlight.setVisibility(recordViewsVisibility);
            }
            ivPlay.setVisibility(playViewsVisibility);
        }
        ivCancel.setVisibility(playViewsVisibility);
        ivConfirm.setVisibility(playViewsVisibility);
        tvTiming.setVisibility(recordViewsVisibility);
        cpbRecord.setVisibility(recordViewsVisibility);
        ivBack.setVisibility(recordViewsVisibility);
        xcimg.setVisibility(recordViewsVisibility);
    }

    @Override
    public void showFlashlightButton(boolean show) {
        if (ivFlashlight == null) {
            return;
        }
        ivFlashlight.setSelected(!show);
        ivFlashlight.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setPresenter(@NonNull RecordVideoContract.Presenter<?> presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCompleteRecordVideo(@org.jetbrains.annotations.Nullable String filePath, int videoDuration) {
        Log.e("sss", "onCompleteRecordVideo");
        FragmentActivity activity = getActivity();
        if (activity instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) activity).onCompleteRecordVideo(filePath, videoDuration);
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) parentFragment).onCompleteRecordVideo(filePath, videoDuration);
        }
    }

    @Override
    public void onClickConfirm(@org.jetbrains.annotations.Nullable String filePath, int videoDuration) {
        Log.e("sss", "onClickConfirm");
        FragmentActivity activity = getActivity();
        if (activity instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) activity).onClickConfirm(filePath, videoDuration);
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) parentFragment).onClickConfirm(filePath, videoDuration);
        }
    }

    @Override
    public void onClickCancel(@org.jetbrains.annotations.Nullable String filePath, int videoDuration) {
        FragmentActivity activity = getActivity();
        if (activity instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) activity).onClickCancel(filePath, videoDuration);
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) parentFragment).onClickCancel(filePath, videoDuration);
        }
    }

    @Override
    public void onClickBack() {
        FragmentActivity activity = getActivity();
        if (activity instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) activity).onClickBack();
        }
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof RMOnRecordVideoListener) {
            ((RMOnRecordVideoListener) parentFragment).onClickBack();
        }
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath 存储文件路径
     * @return 返回RecordVideoFragment
     */
    @NonNull
    public static RecordVideoFragment newInstance(@Nullable String filePath) {
        return newInstance(filePath, DEFAULT_RECORD_VIDEO_DURATION);
    }

    /**
     * 获取录制视频Fragment实例（使用默认配置项）
     *
     * @param filePath    存储文件路径
     * @param maxDuration 最大时长（秒数）
     * @return 返回RecordVideoFragment
     */
    @NonNull
    public static RecordVideoFragment newInstance(@Nullable String filePath, int maxDuration) {
        return newInstance(new RecordVideoOption.Builder()
                .setRecorderOption(new RecorderOption.Builder().buildDefaultVideoBean(filePath))
                .setMaxDuration(maxDuration)
                .build());
    }

    /**
     * 获取录制视频Fragment实例
     *
     * @param option 录制配置信息对象
     * @return 返回RecordVideoFragment
     */
    @NonNull
    public static RecordVideoFragment newInstance(@Nullable RecordVideoOption option) {
        RecordVideoFragment fragment = new RecordVideoFragment();
        Bundle args = new Bundle();
        args.putParcelable(BUNDLE_EXTRA_RECORD_VIDEO_OPTION, option == null ? new RecordVideoOption() : option);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 获取计时控件
     *
     * @return 返回计时AppCompatTextView
     */
    public AppCompatTextView getTimingView() {
        return tvTiming;
    }

    /**
     * 获取圆形进度按钮
     *
     * @return 返回进度CircleProgressButton
     */
    public CircleProgressButton getCircleProgressButton() {
        return cpbRecord;
    }

    /**
     * 获取翻转摄像头控件
     *
     * @return 返回翻转摄像头AppCompatImageView
     */
    public AppCompatImageView getFlipCameraView() {
        return ivFlipCamera;
    }

    /**
     * 获取播放控件
     *
     * @return 返回播放AppCompatImageView
     */
    public AppCompatImageView getPlayView() {
        return ivPlay;
    }

    /**
     * 获取取消控件
     *
     * @return 返回取消AppCompatImageView
     */
    public AppCompatImageView getCancelView() {
        return ivCancel;
    }

    /**
     * 获取确认控件
     *
     * @return 返回确认AppCompatImageView
     */
    public AppCompatImageView getConfirmView() {
        return ivConfirm;
    }

    /**
     * 获取返回控件
     *
     * @return 返回返回AppCompatImageView
     */
    public AppCompatImageView getBackView() {
        return ivBack;
    }

    /**
     * 检测是否已经获取到权限
     *
     * @return 如果获取到权限返回true，否则返回false
     */
    private boolean checkHasPermissions() {
        return RecordPermissionUtils.checkRecordPermissions(this);
    }

    /**
     * 初始化录制参数信息对象
     */
    private void initRecorderOption() {
        Bundle args = getArguments();
        if (args != null) {
            mOption = args.getParcelable(BUNDLE_EXTRA_RECORD_VIDEO_OPTION);
        }
        if (mOption == null) {
            mOption = new RecordVideoOption();
        }
        if (mOption.getRecorderOption() == null) {
            mOption.setRecorderOption(new RecorderOption.Builder()
                    .buildDefaultVideoBean(
                            FilePathUtils.getSaveFilePath(getContext())
                    ));
        }
        if (TextUtils.isEmpty(mOption.getRecorderOption().getFilePath())) {
            mOption.getRecorderOption().setFilePath(
                    FilePathUtils.getSaveFilePath(getContext()));
        }
    }

    /**
     * 翻转摄像头
     */
    private void flipCamera() {
        if (mPresenter != null) {
            mPresenter.flipCamera(svVideo.getHolder());
        }
        ValueAnimator animator = ValueAnimator.ofFloat(1.0F, 0.6F, 1.0F).setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (ivFlipCamera == null) {
                    return;
                }
                ivFlipCamera.setScaleX((Float) animation.getAnimatedValue());
                ivFlipCamera.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    /**
     * 切换闪光灯状态
     */
    private void switchFlashlightState() {
        if (mPresenter == null || !mPresenter.switchFlashlightState(!ivFlashlight.isSelected())) {
            Context context = getContext();
            if (context != null) {
                Toast.makeText(context, getString(R.string.rm_error_switch_flashlight), Toast.LENGTH_SHORT).show();
            }
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(1.0F, 0.6F, 1.0F).setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (ivFlashlight == null) {
                    return;
                }
                ivFlashlight.setScaleX((Float) animation.getAnimatedValue());
                ivFlashlight.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (ivFlashlight == null) {
                    return;
                }
                ivFlashlight.setSelected(!ivFlashlight.isSelected());
            }
        });
        animator.start();
    }

    public interface SelectListener {
        void SelectVideo();
    }

    /**
     * 初始化圆形进度按钮相关属性
     *
     * @param option 录制视频按钮配置信息类
     */
    private void initCircleProgressButton(@Nullable RecordVideoButtonOption option) {
        if (cpbRecord == null || option == null) {
            return;
        }
        if (option.getIdleCircleColor() != 0) {
            cpbRecord.setIdleCircleColor(option.getIdleCircleColor());
        }
        if (option.getPressedCircleColor() != 0) {
            cpbRecord.setPressedCircleColor(option.getPressedCircleColor());
        }
        if (option.getReleasedCircleColor() != 0) {
            cpbRecord.setReleasedCircleColor(option.getReleasedCircleColor());
        }
        if (option.getIdleRingColor() != 0) {
            cpbRecord.setIdleRingColor(option.getIdleRingColor());
        }
        if (option.getPressedRingColor() != 0) {
            cpbRecord.setPressedRingColor(option.getPressedRingColor());
        }
        if (option.getReleasedRingColor() != 0) {
            cpbRecord.setReleasedRingColor(option.getReleasedRingColor());
        }
        if (option.getIdleRingWidth() > 0) {
            cpbRecord.setIdleRingWidth(option.getIdleRingWidth());
        }
        if (option.getPressedRingWidth() > 0) {
            cpbRecord.setPressedRingWidth(option.getPressedRingWidth());
        }
        if (option.getReleasedRingWidth() > 0) {
            cpbRecord.setReleasedRingWidth(option.getReleasedRingWidth());
        }
        if (option.getIdleInnerPadding() > 0) {
            cpbRecord.setIdleInnerPadding(option.getIdleInnerPadding());
        }
        if (option.getPressedInnerPadding() > 0) {
            cpbRecord.setPressedInnerPadding(option.getPressedInnerPadding());
        }
        if (option.getReleasedInnerPadding() > 0) {
            cpbRecord.setReleasedInnerPadding(option.getReleasedInnerPadding());
        }
        cpbRecord.setIdleRingVisible(option.isIdleRingVisible());
        cpbRecord.setPressedRingVisible(option.isPressedRingVisible());
        cpbRecord.setReleasedRingVisible(option.isReleasedRingVisible());
    }
}
