package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cn.android.R;
import com.cn.android.biz.FileOperationBiz;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.FileOperationPresenetr;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.FileOperationView;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.utils.SPUtils;
import com.hjq.bar.TitleBar;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cn.android.network.Constant.updateAppUser;

public class MyInfoSetActivity extends MyActivity implements PublicInterfaceView, FileOperationView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.set_nick_name)
    TextView setNickName;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.set_user_img)
    TextView setUserImg;
    @BindView(R.id.user_img)
    ImageView userImg;
    private FileOperationPresenetr fileOperationPresenetr;
    private PublicInterfaceePresenetr presenetr;
    private String headimg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myinfoset;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        fileOperationPresenetr = new FileOperationPresenetr(this);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        //保存
        showLoading();
        presenetr.getPostTokenRequest(getActivity(), ServerUrl.updateAppUser, Constant.updateAppUser);
    }

    @Override
    protected void initData() {
        headimg = UserBean().getHeadImg();
        ImageLoader.with(getActivity()).load(headimg).circle().into(userImg);
        nickName.setText(UserBean().getNickname());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.set_nick_name, R.id.set_user_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_nick_name:
                startActivityForResult(MyInputActivity.class, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int resultCode, @Nullable Intent data) {
                        String nick = data.getStringExtra("edit");
                        if (!nick.equals(""))
                            nickName.setText(nick);
                    }
                });
                break;
            case R.id.set_user_img:
                PhotoActivity.start(getActivity(), new PhotoActivity.OnPhotoSelectListener() {
                    @Override
                    public void onSelect(List<String> data) {
                        showLoading();
                        fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(data.get(0)), ServerUrl.upload, 101);
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
        }
    }

    @Override
    public void FileOperationSuccess(Object data, int tag) {
        showComplete();
        switch (tag) {
            case 101:
                headimg = (String) data;
                ImageLoader.with(getActivity())
                        .load(headimg)
                        .circle()
                        .into(userImg);
                break;
        }

    }

    @Override
    public void FileOperationProgress(float progress, int tag) {

    }

    @Override
    public void FileOperationError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);

    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.updateAppUser:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("nickname", nickName.getText().toString().trim());
                map.put("head_img", headimg);
                return map;

        }
        return map;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.updateAppUser:
                ToastUtils.show("保存成功");
                finish();

                break;

        }

    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
