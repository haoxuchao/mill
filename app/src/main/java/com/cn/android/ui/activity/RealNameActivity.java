package com.cn.android.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.FileOperationPresenetr;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.FileOperationView;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.dialog.BigImgDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RealNameActivity extends MyActivity implements PublicInterfaceView, FileOperationView {
    @BindView(R.id.back_img)
    ImageButton backImg;
    @BindView(R.id.id_card_tv)
    TextView idCardTv;
    @BindView(R.id.edit_id_card_no)
    EditText editIdCardNo;
    @BindView(R.id.title_bar)
    ConstraintLayout titleBar;
    @BindView(R.id.ali_img_prompt)
    TextView aliImgPrompt;
    @BindView(R.id.photo_img)
    ImageView photoImg;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.ali_no_tv)
    TextView aliNoTv;
    @BindView(R.id.ali_info)
    ConstraintLayout aliInfo;
    @BindView(R.id.confirm_btn)
    TextView confirmBtn;
    String photo;
    PublicInterfaceePresenetr presenetr;
    FileOperationPresenetr fileOperationPresenetr;
    @BindView(R.id.edit_ali_no)
    EditText editAliNo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name;
    }

    @Override
    protected void initView() {
        presenetr = new PublicInterfaceePresenetr(this);
        fileOperationPresenetr = new FileOperationPresenetr(this);
        ImmersionBar.setTitleBar(this, titleBar);
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

    @OnClick({R.id.back_img, R.id.photo_img, R.id.confirm_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_img:
                finish();
                break;
            case R.id.photo_img:
                if (TextUtils.isEmpty(photo)) {
                    PhotoActivity.start(getActivity(), new PhotoActivity.OnPhotoSelectListener() {
                        @Override
                        public void onSelect(List<String> data) {
                            ImageLoader.with(getActivity())
                                    .load(data.get(0))
                                    .into(photoImg);
                            photo = data.get(0);
                            showLoading();
                            fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(data.get(0)), ServerUrl.upload, 101);
                        }

                        @Override
                        public void onCancel() {
                            toast("取消了");
                        }
                    });
                } else {
                    new BigImgDialog.Builder(this).setImg(photo).show();
                }

                break;
            case R.id.confirm_btn:
                if (isCheck()) {
                    showLoading();
                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.realAppuser, Constant.realAppuser);
                }
                break;
        }
    }

    private boolean isCheck() {
        if(TextUtils.isEmpty(editIdCardNo.getText().toString().trim())){
            ToastUtils.show("请输入身份证号");
            return false;
        }
        if(TextUtils.isEmpty(photo)){
            ToastUtils.show("请选择收款码");
            return false;
        }
        if(TextUtils.isEmpty(editAliNo.getText().toString().trim())){
            ToastUtils.show("请输入支付宝账号");
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("authorization", UserBean().getToken());
        map.put("userid", UserBean().getId());
        map.put("idcard", editIdCardNo.getText().toString().trim());
        map.put("alipay_img", photo);
        map.put("alipayno", editAliNo.getText().toString().trim());
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

    @Override
    public void FileOperationSuccess(Object data, int tag) {
        showComplete();
        switch (tag) {
            case 101:
                photo = (String) data;
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
}
