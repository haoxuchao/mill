package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.TradBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class TradingAdapter  extends BaseQuickAdapter<TradBean, BaseViewHolder> {
    public TradingAdapter(@Nullable List<TradBean> data) {
        super(R.layout.item_trading,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TradBean item) {
        helper.setText(R.id.user_name,item.getNickname());
        helper.setText(R.id.price,String.format("单价：%s CNY",item.getBi_one_money()));
        helper.setText(R.id.num,String.format("数量：%s-%s",item.getMin_bi_num(),item.getMax_bi_num()));
        ImageLoader.with(helper.itemView.getContext()).load(item.getHead_img()).circle().into(helper.getView(R.id.user_img));
        helper.addOnClickListener(R.id.chu_btn);
    }
}
