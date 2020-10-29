package com.cn.android.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cn.android.R;
import com.cn.android.bean.MerchantsGoodsBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.FileOperationPresenetr;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.FileOperationView;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.adapter.GridImageAdapter;
import com.cn.android.widget.FullyGridLayoutManager;
import com.cn.android.widget.GridSpacingItemDecoration;
import com.hjq.bar.TitleBar;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReleaseGoodsActivity extends MyActivity implements PublicInterfaceView, FileOperationView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.goods_m_tv)
    TextView goodsMTv;
    @BindView(R.id.et_goods_m)
    EditText etGoodsM;
    @BindView(R.id.goods_photo_tv)
    TextView goodsPhotoTv;
    @BindView(R.id.goods_img_recycle)
    RecyclerView goodsImgRecycle;
    @BindView(R.id.img_c)
    ConstraintLayout imgC;
    @BindView(R.id.goods_price_tv)
    TextView goodsPriceTv;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.price_prompt)
    TextView pricePrompt;
    @BindView(R.id.tvnum)
    TextView tvnum;
    @BindView(R.id.jia_btn)
    ImageView jiaBtn;
    @BindView(R.id.jian_btn)
    ImageView jianBtn;
    @BindView(R.id.num_tv)
    TextView numTv;
    @BindView(R.id.push_goods_btn)
    TextView pushGoodsBtn;
    private GridImageAdapter imgAdapter;
    PublicInterfaceePresenetr presenetr;
    FileOperationPresenetr fileOperationPresenetr;
    MerchantsGoodsBean goodsBean;

    public ArrayList<String> imgList = new ArrayList<>();
    public List<String> imgs = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_release_goods;
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        finish();
    }

    @Override
    protected void initView() {
        goodsBean = (MerchantsGoodsBean) getIntent().getSerializableExtra("goods");
        if (goodsBean != null) {
            String[] strs;
            etGoodsM.setText(goodsBean.getTitle());
            etPrice.setText(String.valueOf(goodsBean.getEb()));
            numTv.setText(String.valueOf(goodsBean.getNum()));
            if (goodsBean.getImgs().contains(",")) {
                strs = goodsBean.getImgs().split(",");
            } else {
                strs = new String[]{goodsBean.getImgs()};
            }
            imgList.addAll(Arrays.asList(strs));
            imgs.addAll(Arrays.asList(strs));
        }
        presenetr = new PublicInterfaceePresenetr(this);
        fileOperationPresenetr = new FileOperationPresenetr(this);
        imgAdapter = new GridImageAdapter(getActivity(), onAddPicClickListener);
        goodsImgRecycle.setNestedScrollingEnabled(false);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, true);
        goodsImgRecycle.setLayoutManager(manager);
        goodsImgRecycle.addItemDecoration(new GridSpacingItemDecoration(4, 2, true));
        goodsImgRecycle.setAdapter(imgAdapter);
        imgAdapter.setList(imgList);
        imgAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                ImageActivity.start(getActivity(), imgList);
            }
            @Override
            public void onItemDeleteListener(int position, View v) {
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        imgs.remove(position);
                        imgList.remove(position);
                        imgAdapter.setList(imgList);
                    } catch (Exception e) {
                        ToastUtils.show(e.getMessage());
                        Log.e("eee", e.getMessage());
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 申请存储权限
            XXPermissions.with(getActivity())
                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .constantRequest()
                    .request(new OnPermission() {
                        @Override
                        public void hasPermission(List<String> granted, boolean isAll) {
                            PhotoActivity.start(getActivity(),9-imgList.size(), new PhotoActivity.OnPhotoSelectListener() {
                                @Override
                                public void onSelect(List<String> data) {
                                    for (int i=0;i<data.size();i++){
                                        imgList.add(data.get(i));
                                        imgAdapter.setList(imgList);
                                        showLoading();
                                        fileOperationPresenetr.uploadSingleFileRequest(getActivity(), "file", new File(data.get(i)), ServerUrl.upload, 101);
                                    }

                                }
                                @Override
                                public void onCancel() {
                                    toast("取消了");
                                }
                            });
                        }
                        @Override
                        public void noPermission(List<String> denied, boolean quick) {
                            if (quick) {
                                toast(R.string.common_permission_fail);
                                XXPermissions.gotoPermissionSettings(getActivity(), false);
                            } else {
                                toast(R.string.common_permission_hint);
                            }
                        }
                    });
        }
    };

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        ButterKnife.bind(this);
    }

    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("authorization", UserBean().getToken());
        map.put("login_userid", UserBean().getId());
        map.put("title", etGoodsM.getText().toString().trim());
        map.put("imgs", getIMgs());
        map.put("num", num);
        map.put("eb", etPrice.getText().toString().trim());
        if (goodsBean != null) {
            map.put("id", goodsBean.getId());
        }
        return map;
    }

    public String getIMgs() {
        StringBuffer sb = new StringBuffer();
        for (String item : imgs) {
            if (sb.toString().length() > 0) {
                sb.append("," + item);
            } else {
                sb.append(item);
            }
        }
        return sb.toString().trim();
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        ToastUtils.show("发布成功");
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
                imgs.add((String) data);
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


    private boolean isCheck() {
        if (TextUtils.isEmpty(etGoodsM.getText().toString().trim())) {
            ToastUtils.show("请输入商品描述");
            return false;
        }
        if (TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            ToastUtils.show("请输入商品价格");
            return false;
        }
        if (imgList.size() == 0) {
            ToastUtils.show("请选择商品照片");
            return false;
        }
        return true;
    }

    int num = 1;

    @OnClick({R.id.jia_btn, R.id.push_goods_btn, R.id.jian_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.push_goods_btn:
                if (isCheck()) {
                    showLoading();
                    presenetr.getPostTokenRequest(getActivity(), ServerUrl.addShop, Constant.addShop);
                }
                break;
            case R.id.jia_btn:
                ++num;
                numTv.setText(String.valueOf(num));
                break;
            case R.id.jian_btn:
                if (num < 1) {
                    ToastUtils.show("数量不能小于1");
                } else {
                    num--;
                }
                numTv.setText(String.valueOf(num));
                break;
        }
    }
}
