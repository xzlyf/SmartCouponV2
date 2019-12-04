package com.xz.jlw2.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xz.base.BaseActivity;
import com.xz.jlw2.R;
import com.xz.jlw2.activity.fragment.CartFragment;
import com.xz.jlw2.activity.fragment.CommonFragment;
import com.xz.jlw2.activity.fragment.DiscoverFragment;
import com.xz.jlw2.adapter.TitleFragmentPagerAdapter;
import com.xz.jlw2.custom.SlideViewPage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    private String[] titles = new String[]{
            "主页",
            "发现",
            "购物车",
            "我的"};
    private final int[] mTabRes = new int[]{
            R.mipmap.ic_home,
            R.mipmap.ic_news,
            R.mipmap.ic_car,
            R.mipmap.ic_my};
    private final int[] mTabResPressed = new int[]{
            R.mipmap.ic_home_pre,
            R.mipmap.ic_news_pre,
            R.mipmap.ic_car_pre,
            R.mipmap.ic_my_pre};
    @BindView(R.id.view_pager)
    SlideViewPage viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @Override
    public boolean homeAsUpEnabled() {
        return false;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        initTab();
    }

    /**
     * 初始化tab
     */
    private void initTab() {

        List<Fragment> fragments = new ArrayList<>();
        //加入布局
        fragments.add(new CommonFragment());
        fragments.add(new DiscoverFragment());
        fragments.add(new CartFragment());
        //待添加功能——清理Glide 缓存
        fragments.add(new Fragment());


        //设置适配器
        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.canSlide(false);//设置不能左右滑动
        tabLayout.setupWithViewPager(viewPager);
        //默认tab状态
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            switch (i) {
                case 0:
                    tabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabResPressed[0]));
                    break;
                case 1:
                    tabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabRes[1]));
                    break;
                case 2:
                    tabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabRes[2]));
                    break;
                case 3:
                    tabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabRes[3]));
                    break;
            }
        }

        //选中tab状态
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //改变Tab 状态
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    if (i == tab.getPosition()) {
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabResPressed[i]));
                    } else {
                        tabLayout.getTabAt(i).setIcon(getResources().getDrawable(mTabRes[i]));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
