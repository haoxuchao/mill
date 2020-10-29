package com.cn.android.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cn.android.R;
import com.cn.android.bean.Address;


import java.util.List;

public class AddressAdapter extends BaseQuickAdapter<Address, BaseViewHolder> {
    public AddressAdapter(@Nullable List<Address> data) {
        super(R.layout.item_address, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Address item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.address, String.format("%s %s", item.getProCityArea(), item.getAddress()));
        helper.setText(R.id.phone, item.getPhone());
        helper.addOnClickListener(R.id.item_address);
        helper.addOnClickListener(R.id.edit_img);
    }
}
