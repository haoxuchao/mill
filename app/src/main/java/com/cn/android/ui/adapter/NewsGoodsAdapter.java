package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.shopBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class NewsGoodsAdapter extends BaseQuickAdapter<shopBean, BaseViewHolder> {
    public NewsGoodsAdapter(@Nullable List<shopBean> data) {
        super(R.layout.item_news_goods,data);
    }
    @Override
    protected void convert(BaseViewHolder helper, shopBean item) {
        ImageLoader.with(helper.itemView.getContext()).load(item.getImg()).circle(10).into(helper.getView(R.id.goods_img));
        ImageLoader.with(helper.itemView.getContext()).load(item.getHead_img()).circle().into(helper.getView(R.id.user_img));
        helper.setText(R.id.goods_name,item.getName());
        helper.setText(R.id.user_name,item.getNickname());
        helper.setText(R.id.price,String.format("%s(EB)",item.getEb()));
    }
}
