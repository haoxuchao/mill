package com.mingyuechunqiu.recordermanager.feature.record;

import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mingyuechunqiu.recordermanager.data.bean.RecorderOption;

/**
 * <pre>
 *     author : 明月春秋
 *     e-mail : xiyujieit@163.com
 *     time   : 2018/10/31
 *     desc   : 录制接口，约束要实现的方法
 *     version: 1.0
 * </pre>
 */
public interface IRecorderHelper {

    /**
     * 录制音频
     *
     * @param path 文件存储路径
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    boolean recordAudio(@NonNull String path);

    /**
     * 录制音频
     *
     * @param option 存储录制信息的对象
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    boolean recordAudio(@NonNull RecorderOption option);

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param path    文件存储路径
     * @return 返回是否成功开启录制，成功返回true，否则返回false
     */
    boolean recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable String path);

    /**
     * 录制视频
     *
     * @param camera  相机
     * @param surface 表面视图
     * @param option  存储录制信息的对象
     * @return 返回是否成功开启视频录制，成功返回true，否则返回false
     */
    boolean recordVideo(@Nullable Camera camera, @Nullable Surface surface, @Nullable RecorderOption option);

    /**
     * 释放资源
     */
    void release();

    /**
     * 获取录制器
     *
     * @return 返回实例对象
     */
    @NonNull
    MediaRecorder getMediaRecorder();

    /**
     * 获取配置信息对象
     *
     * @return 返回实例对象
     */
    @Nullable
    RecorderOption getRecorderOption();
}
