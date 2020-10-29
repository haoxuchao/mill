package com.cn.android.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.cn.android.R;
import com.cn.android.common.MyActivity;
import com.cn.android.ui.fragment.MyOrderFragment;
import com.cn.android.ui.fragment.OrderManagerFragment;
import com.google.android.material.tabs.TabLayout;
import com.hjq.bar.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderManageActivity extends MyActivity {
    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private int type=0;
    private ArrayList<String> mTitle;
    private ArrayList<Fragment> mFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_manage;
    }

    @Override
    protected void initView() {
        type=getIntent().getIntExtra("type",0);
        mTitle = new ArrayList<>();
        mTitle.add("全部订单");
        mTitle.add("已付款");
        mFragment = new ArrayList<>();
        mFragment.add(new OrderManagerFragment().instance(0));
        mFragment.add(new OrderManagerFragment().instance(1));

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //在这里可以设置选中状态下  tab字体显示样式
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(type);
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
