package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.android.R;
import com.cn.android.bean.GoodsBean;
import com.cn.android.bean.SubmitBean;
import com.cn.android.common.MyActivity;
import com.cn.android.network.Constant;
import com.cn.android.network.GsonUtils;
import com.cn.android.network.ServerUrl;
import com.cn.android.presenter.PublicInterfaceePresenetr;
import com.cn.android.presenter.view.PublicInterfaceView;
import com.cn.android.ui.dialog.PayDialog;
import com.cn.android.ui.dialog.PayPasswordDialog;
import com.cn.android.widget.NumIndicator;
import com.hjq.bar.TitleBar;
import com.hjq.dialog.base.BaseDialog;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailActivity extends MyActivity implements PublicInterfaceView {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.view_1)
    View view1;
    @BindView(R.id.user_img)
    ImageView userImg;
    @BindView(R.id.guanzhu)
    ImageView guanzhu;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.add_car)
    TextView addCar;
    @BindView(R.id.now_pay)
    TextView nowPay;
    private String goodsid;
    PublicInterfaceePresenetr presenetr;
    GoodsBean goodsBean;
    private int selectNum = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @Override
    protected void initView() {

        presenetr = new PublicInterfaceePresenetr(this);
        goodsid = getIntent().getStringExtra("id");
    }

    @Override
    protected void initData() {
        if (goodsid != null) {
            showLoading();
            presenetr.getGetRequest(getActivity(), ServerUrl.selectShopById, Constant.selectShopById);
        }

    }

    public void useBanner(List<String> goodsbanner) {


//        goodsbanner.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602321628341&di=1601f07d9529bf2538ae2b88feec4689&imgtype=0&src=http%3A%2F%2Fimg30.360buyimg.com%2FpopWaterMark%2Fg13%2FM05%2F17%2F0D%2FrBEhVFMMRrsIAAAAAAHs9z5uxJEAAJCmAJpGDkAAe0P393.jpg");
//        goodsbanner.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602321628341&di=1601f07d9529bf2538ae2b88feec4689&imgtype=0&src=http%3A%2F%2Fimg30.360buyimg.com%2FpopWaterMark%2Fg13%2FM05%2F17%2F0D%2FrBEhVFMMRrsIAAAAAAHs9z5uxJEAAJCmAJpGDkAAe0P393.jpg");
        //—————————————————————————如果你想偷懒，而又只是图片轮播————————————————————————
        banner.setAdapter(new BannerImageAdapter<String>(goodsbanner) {
            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                //图片加载自己实现
                ImageLoader.with(holder.itemView.getContext())
                        .load(data)
//                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                        .into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .isAutoLoop(true)
                .setIndicator(new NumIndicator(this));
        //更多使用方法仔细阅读文档，或者查看demo
    }


    //方法二：调用banner的addBannerLifecycleObserver()方法，让banner自己控制
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加生命周期观察者
        banner.addBannerLifecycleObserver(this);
    }

    @OnClick({R.id.add_car, R.id.now_pay, R.id.guanzhu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_car:
                new PayDialog.Builder(this).setPrice(String.valueOf(goodsBean.getEb())).setPayType("加入购物车").setOnClickListener(new PayDialog.OnClickListener() {
                    @Override
                    public void addCar(int num, String price) {
                        selectNum = num;
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.addCartOrder, Constant.addCartOrder);
                    }
                    @Override
                    public void Pay(int num, String price) {}
                }).show();
                break;
            case R.id.now_pay:
                new PayDialog.Builder(this).setPrice(String.valueOf(goodsBean.getEb())).setPayType("立即支付").setOnClickListener(new PayDialog.OnClickListener() {
                    @Override
                    public void addCar(int num, String price) {
//                        selectNum = num;
//                        showLoading();
//                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.addCartOrder, Constant.addCartOrder);
                    }

                    @Override
                    public void Pay(int num, String price) {
                        selectNum = num;
                        showLoading();
                        presenetr.getPostTokenRequest(getActivity(), ServerUrl.addOrder, Constant.addOrder);
//                        new PayPasswordDialog.Builder(getActivity()).setTitle("请输入交易密码").setListener(new PayPasswordDialog.OnListener() {
//                            @Override
//                            public void onCompleted(BaseDialog dialog, String password) {
//
//                                dialog.dismiss();
//                            }
//                            @Override
//                            public void onCancel(BaseDialog dialog) { }
//                        }).show();
                    }
                }).show();
                break;
            case R.id.guanzhu:
//                showLoading();
//                presenetr.getPostTokenRequest(getActivity(), ServerUrl.addFocus, Constant.addFocus);
                break;
        }
    }


    @Override
    public Map<String, Object> setPublicInterfaceData(int tag) {
        Map<String, Object> map = new HashMap<>();
        switch (tag) {
            case Constant.selectShopById:
                map.put("shopid", goodsid);
                return map;
            case Constant.addCartOrder:
            case Constant.addOrder:
                map.put("authorization", UserBean().getToken());
                map.put("userid", UserBean().getId());
                map.put("shopid", goodsid);
                map.put("num", selectNum);
                return map;
            case Constant.addFocus:
//                map.put("authorization", UserBean().getToken());
//                map.put("userid", goodsBean.get());//视频用户id
//                map.put("login_userid", userbean.getId());
                return map;

        }
        return null;
    }

    @Override
    public void onPublicInterfaceSuccess(String data, int tag) {
        showComplete();
        switch (tag) {
            case Constant.selectShopById:
                String[] strs;
                goodsBean = GsonUtils.getPerson(data, GoodsBean.class);
                if (goodsBean.getImgs().contains(",")) {
                    strs = goodsBean.getImgs().split(",");
                } else {
                    strs = new String[]{goodsBean.getImg()};
                }
                useBanner(Arrays.asList(strs));
                ImageLoader.with(getActivity()).load(goodsBean.getHead_img()).circle().into(userImg);
                price.setText(String.format("%s(EB)", goodsBean.getEb()));
                contentTv.setText(goodsBean.getTitle());
                break;
            case Constant.addCartOrder:
                ToastUtils.show("加入购物车成功");
                break;
            case Constant.addOrder:
                startActivity(new Intent(getActivity(),CommitOrderActivity.class).putExtra("sub_bean",GsonUtils.getPerson(data, SubmitBean.class)));
                break;
        }
    }

    @Override
    public void onPublicInterfaceError(String error, int tag) {
        showComplete();
        ToastUtils.show(error);
    }
}
