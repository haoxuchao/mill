package com.cn.android.ui.adapter;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.OrderBean;

import java.util.List;

public class OrderAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    private  int mtype;
    onItemListener onItemListener;

    public void setOnItemListener(OrderAdapter.onItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public OrderAdapter(@Nullable List<OrderBean> data, int type) {
        super(R.layout.item_my_order, data);
        mtype=type;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        RecyclerView recyclerView=helper.getView(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(helper.itemView.getContext()));
        OrderGoodsAdapter adapter =  new OrderGoodsAdapter(item.getShopList());
        helper.setText(R.id.prompt1,String.format("共%s件商品，总计：",item.getShop_total_num()));
        helper.setText(R.id.b_price,String.format("%sEB",item.getShop_total_eb()));
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemListener.onlick(helper.getLayoutPosition());
            }
        });
        helper.addOnClickListener(R.id.item);
        recyclerView.setAdapter(adapter);
        switch (item.getStatus()) {
            case 1:
                helper.setGone(R.id.confirm, false);
                helper.setGone(R.id.the_end_tv, false);
                helper.setGone(R.id.confirm_send, mtype==2);
                helper.addOnClickListener(R.id.confirm_send);
                break;
            case 2:
                helper.setGone(R.id.confirm, mtype==1);
                helper.addOnClickListener(R.id.confirm);
                helper.setGone(R.id.confirm_send, false);
                helper.setGone(R.id.the_end_tv, false);
                break;
            case 3:
                helper.setGone(R.id.confirm, false);
                helper.setGone(R.id.the_end_tv, true);
                break;

        }
    }
    public  interface  onItemListener{
        void onlick(int pos);
    }
}
