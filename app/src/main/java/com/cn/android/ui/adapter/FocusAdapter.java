package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.FocusBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class FocusAdapter extends BaseQuickAdapter<FocusBean, BaseViewHolder> {
    public FocusAdapter(@Nullable List<FocusBean> data) {
        super(R.layout.item_focus,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FocusBean item) {
        ImageLoader.with(helper.itemView.getContext()).load(item.getHead_img()).circle().into(helper.getView(R.id.user_img));
        helper.setText(R.id.nick_name,item.getNickname());
        helper.addOnClickListener(R.id.cancel);
        helper.addOnClickListener(R.id.item);
    }
}
