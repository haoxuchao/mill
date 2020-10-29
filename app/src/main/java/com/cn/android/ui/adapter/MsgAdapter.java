package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.MsgBean;

import java.util.List;

public class MsgAdapter extends BaseQuickAdapter<MsgBean, BaseViewHolder> {
    public MsgAdapter(@Nullable List<MsgBean> data) {
        super(R.layout.item_msg,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MsgBean item) {
        helper.setText(R.id.msg_tv, item.getTitle());
        helper.setText(R.id.time, item.getCtime());
        if(item.getType()==2){
            helper.addOnClickListener(R.id.item);
        }
    }
}
