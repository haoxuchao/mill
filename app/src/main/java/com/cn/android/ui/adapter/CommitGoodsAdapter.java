package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.SubmitBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class CommitGoodsAdapter extends BaseQuickAdapter<SubmitBean.ShopListBean, BaseViewHolder> {
    public CommitGoodsAdapter(@Nullable List<SubmitBean.ShopListBean> data) {
        super(R.layout.item_commit, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubmitBean.ShopListBean item) {
        ImageLoader.with(helper.itemView.getContext()).load(item.getImg()).circle(10).into(helper.getView(R.id.goods_img));
        helper.setText(R.id.goods_name,item.getName());
        helper.setText(R.id.num,String.format("x%s",item.getNum()));
        helper.setText(R.id.money,String.format("%s(EB)",item.getEb()));
    }
}
