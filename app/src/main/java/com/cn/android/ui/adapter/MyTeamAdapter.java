package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.TeamBean;
import com.hjq.image.ImageLoader;

import java.util.List;

public class MyTeamAdapter extends BaseQuickAdapter<TeamBean, BaseViewHolder> {
    public MyTeamAdapter(@Nullable List<TeamBean> data) {
        super(R.layout.item_myteam,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TeamBean item) {
        ImageLoader.with(helper.itemView.getContext()).load(item.getHeadImg()).circle().into(helper.getView(R.id.user_img));
        helper.setText(R.id.user_name,item.getNickname());
        helper.setText(R.id.grade,item.getGrade());
        helper.setText(R.id.time,item.getCtime());
    }
}
