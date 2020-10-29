package com.cn.android.ui.fragment;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.cn.android.R;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.ui.activity.HomeActivity;
import com.cn.android.ui.activity.PhotoActivity;
import com.cn.android.utils.TextDrawableUtils;
import com.hjq.image.ImageLoader;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 项目框架使用示例
 */
public final class TestFragmentC extends MyLazyFragment<HomeActivity> {



    public static TestFragmentC newInstance() {
        return new TestFragmentC();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_c;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }


}