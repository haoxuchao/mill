package com.cn.android.ui.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.TradingOrder;

import java.util.List;

public class TradingOrderAdapter extends BaseMultiItemQuickAdapter<TradingOrder, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public TradingOrderAdapter(List<TradingOrder> data) {
        super(data);
        addItemType(1, R.layout.item_beg_order);//求购单
        addItemType(3, R.layout.item_beg_order);//求购单
        addItemType(2, R.layout.item_ask_order);//出售单
    }

    @Override
    protected void convert(BaseViewHolder helper, TradingOrder item) {
        helper.setText(R.id.time, String.format("发布日期：%s", item.getCtime()));
        switch (item.getType()) {
            case 2:
                helper.setText(R.id.chu_tv, String.format("出售：%sEB", item.getMaxBiNum()));
                helper.setText(R.id.price_tv, String.format("单价：%sCNY", item.getBiOneMoney()));
                helper.setText(R.id.totlmoney, String.format("%sCNY", item.getTotalMoney()));
                switch (item.getStatus()) {
                    case 2:
                        helper.setGone(R.id.confirm, true);
                        helper.setGone(R.id.the_end, false);
                        helper.addOnClickListener(R.id.confirm);
                        break;
                    case 3:
                        helper.setGone(R.id.confirm, false);
                        helper.setGone(R.id.the_end, true);
                        break;
                }
                break;
            case 1:
                helper.setGone(R.id.dzf_c, false);
                helper.setGone(R.id.to_pay, false);
                helper.setGone(R.id.cancel, true);
                helper.setGone(R.id.the_end, false);
                helper.setGone(R.id.n_dzf_c, true);
                helper.addOnClickListener(R.id.cancel);
                helper.setText(R.id.n_dzf_num, String.format("数量：%s-%s", TextUtils.isEmpty(item.getMinBiNum()) ? 0 : item.getMinBiNum(), TextUtils.isEmpty(item.getMaxBiNum()) ? 0 : item.getMaxBiNum()));
                helper.setText(R.id.n_dzf_dan, String.format("单价：%sCNY", item.getBiOneMoney()));
                break;//非待支付(正在求购)
            case 3:
                switch (item.getStatus()) {
                    case 1:
                        helper.setGone(R.id.dzf_c, true);
                        helper.setGone(R.id.to_pay, true);
                        helper.setGone(R.id.cancel, false);
                        helper.setGone(R.id.the_end, false);
                        helper.setGone(R.id.n_dzf_c, false);
                        helper.addOnClickListener(R.id.to_pay);
                        helper.setText(R.id.prompt_tv, item.getTitle());
                        helper.setText(R.id.price, String.format("%s CNY",item.getTotalMoney()));
                        helper.setText(R.id.dzf_num, String.format("数量：%s-%s", TextUtils.isEmpty(item.getMinBiNum()) ? 0 : item.getMinBiNum(), TextUtils.isEmpty(item.getMaxBiNum()) ? 0 : item.getMaxBiNum()));
                        helper.setText(R.id.dzf_dan, String.format("单价：%sCNY", item.getBiOneMoney()));
                        break;//待支付
                    case 2:
                        helper.setGone(R.id.dzf_c, false);
                        helper.setGone(R.id.to_pay, false);
                        helper.setGone(R.id.cancel, true);
                        helper.setGone(R.id.the_end, false);
                        helper.setGone(R.id.n_dzf_c, true);
                        helper.addOnClickListener(R.id.cancel);
                        helper.setText(R.id.n_dzf_num, String.format("数量：%s-%s", TextUtils.isEmpty(item.getMinBiNum()) ? 0 : item.getMinBiNum(), TextUtils.isEmpty(item.getMaxBiNum()) ? 0 : item.getMaxBiNum()));
                        helper.setText(R.id.n_dzf_dan, String.format("单价：%sCNY", item.getBiOneMoney()));
                        break;//非待支付(正在求购)
                    case 3:
                        helper.setGone(R.id.dzf_c, false);
                        helper.setGone(R.id.to_pay, false);
                        helper.setGone(R.id.cancel, false);
                        helper.setGone(R.id.the_end, true);
                        helper.setGone(R.id.n_dzf_c, true);
                        helper.setText(R.id.n_dzf_num, String.format("数量：%s-%s", TextUtils.isEmpty(item.getMinBiNum()) ? 0 : item.getMinBiNum(), TextUtils.isEmpty(item.getMaxBiNum()) ? 0 : item.getMaxBiNum()));
                        helper.setText(R.id.n_dzf_dan, String.format("单价：%sCNY", item.getBiOneMoney()));
                        break;//非待支付(已完成)
                }
                break;
        }
    }
}
