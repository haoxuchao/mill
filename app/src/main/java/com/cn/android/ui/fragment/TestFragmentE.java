package com.cn.android.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.android.R;
import com.cn.android.common.MyLazyFragment;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.FileOperationPresenetr;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.FileOperationView;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by desk 3-2 on 2018/11/19.
 */

public class TestFragmentE extends MyLazyFragment implements PublicInterfaceView, FileOperationView {


    private PublicInterfaceePresenetr postPresenetr;
    private FileOperationPresenetr filePresenetr;

    public static TestFragmentE newInstance() {
        return new TestFragmentE();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_e;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_test_e_title;
    }


    @Override
    protected void initView() {
        postPresenetr = new PublicInterfaceePresenetr(this);
        filePresenetr = new FileOperationPresenetr(this);
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        switch (tag) {
            case Constant.GETHTML:
                ToastUtils.show(error);
                break;
            case Constant.POSTSTRING:
                ToastUtils.show(error);
                break;
        }
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> paramsMap = new HashMap<>();
        switch (tag) {
            case Constant.GETHTML:
                paramsMap.put("key", "bd_hyrzjjfb4modhj");
                paramsMap.put("size", "10");
                paramsMap.put("page", "1");
                return paramsMap;
            case Constant.POSTSTRING:
                paramsMap.put("userid", "1");
                paramsMap.put("name", "pkgg123445");
                paramsMap.put("judge", "1");
                return paramsMap;
        }
        return null;
    }

    //===========================   File    ==============================

    @Override
    public void FileOperationSuccess(Object data, int tag) {
        switch (tag) {
        }
    }

    @Override
    public void FileOperationError(String error, int tag) {
        switch (tag) {
        }
    }

    @Override
    public void FileOperationProgress(float progress, int tag) {
        switch (tag) {
        }
    }

    //===========================   File    ==============================

    @Override
    public boolean isStatusBarEnabled() {
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onStop() {
        super.onStop();
        OkHttpUtils.getInstance().cancelTag(this);
    }
}
