package com.xz.jlw2.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseActivity;
import com.xz.jlw2.R;
import com.xz.jlw2.activity.fragment.CommonFragment;
import com.xz.jlw2.adapter.CommonAdapter;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.CommEntity;
import com.xz.utils.SpacesItemDecorationVertical;
import com.xz.utils.network.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private Fragment[] fragments;
    @BindView(R.id.home_container)
    FrameLayout homeContainer;
    @BindView(R.id.tabs)
    TabLayout tabs;

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
        fragments = DataGenerator.getFragments();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    View view = tabs.getTabAt(i).getCustomView();
                    ImageView icon = view.findViewById(R.id.tab_content_image);
                    TextView text = view.findViewById(R.id.tab_content_text);
                    if (i == tab.getPosition()) {
                        // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    } else {
                        // 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
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

        // 提供自定义的布局添加Tab
        for (int i = 0; i < 4; i++) {
            tabs.addTab(tabs.newTab().setCustomView(DataGenerator.getTabView(this, i)));
        }

    }

    private void onTabItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = fragments[0];
                break;
            case 1:
                fragment = fragments[1];
                break;
            case 2:
                fragment = fragments[2];
                break;
            case 3:
                fragment = fragments[3];
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container, fragment).commit();
        }
    }


    static class DataGenerator {
        static final int[] mTabRes = new int[]{
                R.mipmap.ic_home,
                R.mipmap.ic_news,
                R.mipmap.ic_car,
                R.mipmap.ic_my};
        static final int[] mTabResPressed = new int[]{
                R.mipmap.ic_home_pre,
                R.mipmap.ic_news_pre,
                R.mipmap.ic_car_pre,
                R.mipmap.ic_my_pre};
        static final String[] mTabTitle = new String[]{"首页", "发现", "购物车", "我的"};

        static Fragment[] getFragments() {
            Fragment fragments[] = new Fragment[4];
            fragments[0] = new CommonFragment();
            fragments[1] = new CommonFragment();
            fragments[2] = new CommonFragment();
            fragments[3] = new CommonFragment();
            return fragments;
        }

        /**
         * 获取Tab 显示的内容
         *
         * @param context
         * @param position
         * @return
         */
        static View getTabView(Context context, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.home_tab_content, null);
            ImageView tabIcon = view.findViewById(R.id.tab_content_image);
            tabIcon.setImageResource(DataGenerator.mTabRes[position]);
            TextView tabText = view.findViewById(R.id.tab_content_text);
            tabText.setText(mTabTitle[position]);
            return view;
        }
    }
}
