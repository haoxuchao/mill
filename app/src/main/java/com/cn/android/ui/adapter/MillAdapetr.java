
package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.MillBean;

import java.util.List;

public class MillAdapetr extends BaseMultiItemQuickAdapter<MillBean, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MillAdapetr(List<MillBean> data) {
        super(data);
        addItemType(1, R.layout.item_mill);
        addItemType(2, R.layout.item_mill_red);
    }

    @Override
    protected void convert(BaseViewHolder helper, MillBean item) {
        switch (item.getItemType()) {
            case 1:
                helper.setText(R.id.name,item.getName());
                helper.setText(R.id.dui_price,String.format("%sEB",item.getDuihuan_eb_num()));
                helper.setText(R.id.chan_price,String.format("%sEB",item.getProduc_eb_num()));
                helper.setText(R.id.time,String.format("时间：%s天",item.getTime()));
                helper.setGone(R.id.buy_btn, item.getCategory() == 2);
                if(item.getCategory()==2){
                    helper.addOnClickListener(R.id.buy_btn);
                    helper.setVisible(R.id.ctime,false);
                }else{
                    helper.setVisible(R.id.ctime,true);
                    helper.setText(R.id.ctime,String.format("日期:%s-%s",item.getStime(),item.getEtime()));
                }
                break;
            case 2:
                helper.setText(R.id.name,item.getName());
                helper.setText(R.id.model,String.format("型号:%s",item.getModel()));
                helper.setText(R.id.tiao_tv,String.format("条件:%s",item.getTime()));
                helper.setText(R.id.chan_red_price,String.format("%sCNY",item.getDuihuan_eb_num()));
                if (item.getCategory() == 1) {
                    helper.setGone(R.id.qi_btn, item.getIs_use() == 2);
                    helper.addOnClickListener(R.id.qi_btn);
                } else {
                    helper.setGone(R.id.qi_btn, false);
                }
                break;

        }
    }
}
