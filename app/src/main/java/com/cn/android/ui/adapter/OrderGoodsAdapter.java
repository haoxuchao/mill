package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.OrderBean;
import com.cn.android.bean.SubmitBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class OrderGoodsAdapter extends BaseQuickAdapter<OrderBean.ShopListBean, BaseViewHolder> {
    public OrderGoodsAdapter(@Nullable List<OrderBean.ShopListBean> data) {
        super(R.layout.item_commit, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean.ShopListBean item) {
        ImageLoader.with(helper.itemView.getContext()).load(item.getImg()).circle(10).into(helper.getView(R.id.goods_img));
        helper.setText(R.id.goods_name,item.getShop_name());
        helper.setText(R.id.num,String.format("x%s",item.getShop_num()));
        helper.setText(R.id.money,String.format("%s(EB)",item.getShop_eb()));
    }
}
