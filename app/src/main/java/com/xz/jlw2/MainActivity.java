package com.xz.jlw2;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseActivity;
import com.xz.base.OnItemClickListener;
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


    @BindView(R.id.common_recycler)
    RecyclerView commonRecycler;

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
        netGetCommon();
    }

    private CommonAdapter commonAdapter;

    /**
     * 获取普通商品数据
     */
    private void netGetCommon() {
        commonAdapter = new CommonAdapter(this);
        commonRecycler.setLayoutManager(new LinearLayoutManager(this));
        commonRecycler.setAdapter(commonAdapter);
        commonRecycler.addItemDecoration(new SpacesItemDecorationVertical(10));
        Map<String, Object> params = new HashMap<>();
        params.put("appkey", Local.APIKEY);
        params.put("page", 1);
        params.put("pagesize", 20);
        params.put("sort", 7);
        OkHttpClientManager.getAsyn(this, Local.BASE_URL + Local.INDEX, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.e("请求失败：" + request.url());
                sToast("网络请求失败，请稍后重试");
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("error").equals("0")) {
                        JSONArray obj2 = obj.getJSONArray("data");
                        List<CommEntity> list = new ArrayList<>();
                        Gson gson = new Gson();
                        for (int i = 0; i < obj2.length(); i++) {
                            list.add(gson.fromJson(obj2.getJSONObject(i).toString(), CommEntity.class));
                        }
                        commonAdapter.refresh(list);

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
