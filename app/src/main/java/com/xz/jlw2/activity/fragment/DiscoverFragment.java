package com.xz.jlw2.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseFragment;
import com.xz.base.OnItemClickListener;
import com.xz.jlw2.R;
import com.xz.jlw2.activity.DetailActivity;
import com.xz.jlw2.adapter.DiscoverAdapter;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.DiscoverEntity;
import com.xz.utils.SpacesItemDecorationVertical;
import com.xz.utils.network.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefresh;
    private DiscoverAdapter discoverAdapter;
    private List<DiscoverEntity> discoverList = new ArrayList<>();
    private long lastRefreshTime = 0;//上次刷新时间
    private int time = 15000;//刷新时间限制

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case Local.CODE_1:
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                    break;
            }


        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        discoverAdapter = new DiscoverAdapter(context);
        discoverAdapter.setHandler(handler);
        discoverAdapter.setOnItemClickListener(new OnItemClickListener<DiscoverEntity>() {
            @Override
            public void onItemClick(View view, int position, DiscoverEntity model) {
                mContext.startActivity(new Intent(mContext, DetailActivity.class)
                        .putExtra("goodsid", model.getGoodsId()));
            }

            @Override
            public void onItemLongClick(View view, int position, DiscoverEntity model) {

            }
        });

        new ReadDataThread(context).start();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initView(View rootView) {
        TextView tvTitle = rootView.findViewById(R.id.tv_title);
        tvTitle.setText("发现");

        //商品列表
        RecyclerView commonRecycler = rootView.findViewById(R.id.common_recycler);
        commonRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        commonRecycler.addItemDecoration(new SpacesItemDecorationVertical(20));
        commonRecycler.setItemViewCacheSize(20);
        commonRecycler.setAdapter(discoverAdapter);


        swipeRefresh = rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);
    }

    @Override
    protected void initDate(Context mContext) {
        //每次加载页面时刷新更新时间
        lastRefreshTime = System.currentTimeMillis();
    }

    @Override
    public void onRefresh() {
        long now = System.currentTimeMillis();
        if (now - lastRefreshTime < time) {
            sToast("不要频繁刷新人家啦~");
            swipeRefresh.setRefreshing(false);
            return;
        }
        lastRefreshTime = now;
        new ReadDataThread(mContext).start();

    }


    class ReadDataThread extends Thread {
        private Context context;

        ReadDataThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            super.run();

            Map<String, Object> params = new HashMap<>();
            params.put("appkey", Local.APIKEY);
            OkHttpClientManager.getAsyn(context, Local.BASE_URL + Local.PYQGOODS, new OkHttpClientManager.ResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                    Logger.e("请求失败：" + request.url());
                    sToast("网络请求失败，请稍后重试0x001");
                }

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        if (obj.getString("error").equals("0")) {

                            JSONArray obj2 = obj.getJSONArray("data");
                            Gson gson = new Gson();
                            for (int i = 0; i < obj2.length(); i++) {
                                discoverList.add(gson.fromJson(obj2.getJSONObject(i).toString(), DiscoverEntity.class));
                            }

                            discoverAdapter.refresh(discoverList);
                            if (swipeRefresh != null) {
                                swipeRefresh.setRefreshing(false);
                            }

                        } else {
                            sToast("列表获取失败，请稍后重试");
                        }

                    } catch (JSONException e) {
                        sToast("列表解析失败，请稍后重试");
                        e.printStackTrace();
                    }

                }

            }, params, true);

        }
    }
}
