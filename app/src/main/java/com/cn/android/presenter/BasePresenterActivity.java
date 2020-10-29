package com.cn.android.presenter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cn.android.common.MyActivity;
import com.mingyuechunqiu.recordermanager.base.presenter.IBasePresenter;
import com.mingyuechunqiu.recordermanager.base.view.IBaseView;

public abstract class BasePresenterActivity<V extends IBaseView<P>, P extends IBasePresenter>  extends MyActivity {
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        attachPresenter();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            getLifecycle().removeObserver(mPresenter);
            //不能放在onDestroyView中执行，因为像输入框失去焦点这种事件会在onDestroyView之后才被调用
            mPresenter = null;
        }

    }

    /**
     * 添加Present相关
     */
    @SuppressWarnings("unchecked")
    protected void attachPresenter() {
        ((V) this).setPresenter(initPresenter());
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
            getLifecycle().addObserver(mPresenter);
        }
    }

    protected abstract P initPresenter();

    /**
     * 释放资源（在onDestroyView时调用）
     */
    protected abstract void releaseOnDestroyView();

    /**
     * 释放资源（在onDestroy时调用）
     */
    protected abstract void releaseOnDestroy();

    /**
     * Fragment回调
     */
    public interface FragmentCallback {

        void onCall(Fragment fragment, Bundle bundle);
    }
}
