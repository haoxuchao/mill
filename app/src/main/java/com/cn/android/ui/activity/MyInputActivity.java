package com.cn.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.hjq.bar.TitleBar;
import com.hjq.widget.view.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInputActivity extends MyActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_info)
    ClearEditText editInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myinput;
    }

    @Override
    protected void initView() {
        Intent intent = new Intent();
        intent.putExtra("edit", editInfo.getText().toString().trim());
        setResult(RESULT_OK, intent);
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        Intent intent = new Intent();
        intent.putExtra("edit", editInfo.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
