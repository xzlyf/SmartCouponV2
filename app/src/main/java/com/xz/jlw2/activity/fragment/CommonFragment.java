package com.xz.jlw2.activity.fragment;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseFragment;
import com.xz.jlw2.R;
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

public class CommonFragment extends BaseFragment {

    private CommonAdapter commonAdapter;
    private List<CommEntity> list = new ArrayList<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        commonAdapter = new CommonAdapter(mContext);
        //只加载一次数据，除非退出否则不再刷新数据
        netGetCommon();
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_common;
    }

    @Override
    protected void initView(View rootView) {
        RecyclerView commonRecycler = rootView.findViewById(R.id.common_recycler);
        commonRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        commonRecycler.setAdapter(commonAdapter);
        commonRecycler.addItemDecoration(new SpacesItemDecorationVertical(10));

    }

    @Override
    protected void initDate(Context mContext) {
    }


    /**
     * 获取普通商品数据
     */
    private void netGetCommon() {

        Map<String, Object> params = new HashMap<>();
        params.put("appkey", Local.APIKEY);
        params.put("page", 1);
        params.put("pagesize", 20);
        params.put("sort", 7);
        OkHttpClientManager.getAsyn(mContext, Local.BASE_URL + Local.INDEX, new OkHttpClientManager.ResultCallback<String>() {
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
                        Gson gson = new Gson();
                        list.clear();
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
