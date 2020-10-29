package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.MerchantsGoodsBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class MerchantsGoodsAdapter extends BaseQuickAdapter<MerchantsGoodsBean, BaseViewHolder> {
    boolean isShowEt;
    public MerchantsGoodsAdapter(@Nullable List<MerchantsGoodsBean> data,boolean isShowEt) {
        super(R.layout.item_store_goods,data);
        this.isShowEt=isShowEt;
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantsGoodsBean item) {
        ImageLoader.with(helper.itemView.getContext()).load(item.getImg()).circle(10).into(helper.getView(R.id.goods_img));
        helper.setText(R.id.goods_name,item.getName());
        helper.setText(R.id.goods_price,String.format("%s(EB)",item.getEb()));
        helper.setGone(R.id.ran_out_tv,item.getNum()==0);
        helper.setGone(R.id.goods_adv_false,item.getIsVideo()==2);
        helper.setGone(R.id.goods_adv_true,item.getIsVideo()==1);
        helper.setGone(R.id.goods_et,isShowEt);
        helper.setGone(R.id.goods_del,isShowEt);
        helper.addOnClickListener(R.id.goods_del);
        helper.addOnClickListener(R.id.goods_et);
        helper.addOnClickListener(R.id.goods_adv_false);
    }

}
