package com.cn.android.ui.adapter;

import android.widget.CheckBox;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.GoodsBean;
import com.cn.android.bean.shopCarBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class ShopCarAdapter extends BaseQuickAdapter<shopCarBean, BaseViewHolder> {
    public ShopCarAdapter(@Nullable List<shopCarBean> data) {
        super(R.layout.item_shop_car,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, shopCarBean item) {
        helper.getView(R.id.check).setSelected(item.isCheck());
        helper.setText(R.id.num_tv,String.valueOf(item.getShop_num()));
        helper.setText(R.id.goods_name,item.getShop_name());
        helper.setText(R.id.goods_price,String.format("%sEB",item.getShop_eb()));
        helper.setText(R.id.num_tv,String.format("%s",item.getShop_num()));
        ImageLoader.with(helper.itemView.getContext()).load(item.getImg()).into(helper.getView(R.id.goods_img));
        helper.addOnClickListener(R.id.check);
        helper.addOnClickListener(R.id.jia_btn);
        helper.addOnClickListener(R.id.jian_btn);
    }
}
