package com.xz.jlw2.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseActivity;
import com.xz.base.OnItemClickListener;
import com.xz.jlw2.R;
import com.xz.jlw2.adapter.CommonAdapter;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.CommEntity;
import com.xz.jlw2.utils.SignMD5Util;
import com.xz.utils.SpacesItemDecorationVertical;
import com.xz.utils.network.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.common_recycler)
    RecyclerView commonRecycler;
    private int page = 1;//默认页码
    private CommonAdapter commonAdapter;
    private int type = 0;//0刷新模式，1搜索模式

    @OnClick(R.id.submit)
    public void submit() {
        type = 1;
        netGet(etSearch.getText().toString());
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Local.CODE_1:
                    type = 0;
                    page++;
                    netGet(etSearch.getText().toString());
                    break;
            }
        }
    };

    @Override
    public boolean homeAsUpEnabled() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_search;
    }

    @Override
    public void initData() {
        commonAdapter = new CommonAdapter(mContext);
        commonAdapter.setHandler(handler);
        commonAdapter.setMode(Local.MODE_SEARCH);
        commonRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        commonRecycler.setAdapter(commonAdapter);
        commonRecycler.addItemDecoration(new SpacesItemDecorationVertical(10));
        commonRecycler.setItemViewCacheSize(20);
        commonAdapter.setOnItemClickListener(new OnItemClickListener<CommEntity>() {
            @Override
            public void onItemClick(View view, int position, CommEntity model) {
                startActivity(
                        new Intent(SearchActivity.this, DetailActivity.class)
                                .putExtra("goodsid", model.getGoodsId()));
            }

            @Override
            public void onItemLongClick(View view, int position, CommEntity model) {

            }
        });

    }


    /**
     * 商品查找
     *
     * @param word
     */
    private void netGet(String word) {

        if (etSearch.getText().toString().equals("")) {
            return;
        }
        showLoading("请稍后...");
        //注意大淘客接口验签需要使用TreeMap来装参数
        Map<String, Object> params = new TreeMap<>();
        params.put("appKey", Local.APIKEY_DTK);
        params.put("version", Local.VERSION_DTK_SEARCH);
        params.put("type", 0);
        params.put("pageId", page);
        params.put("pageSize", 20);
        params.put("keyWords", word);
        params.put("sign", SignMD5Util.getSignStr(params, Local.APP_SECRET_DTK));
        OkHttpClientManager.getAsyn(mContext, Local.SEARCH, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                disLoading();
                Logger.e("请求失败：" + request.url());
                sToast("网络请求失败，请稍后重试");
            }

            @Override
            public void onResponse(String response) {
                disLoading();

                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("code").equals("0")) {

                        JSONArray obj2 = obj.getJSONObject("data").getJSONArray("list");
                        JSONObject obj3;
                        CommEntity entity;
                        List<CommEntity> commList = new ArrayList<>();

                        for (int i = 0; i < obj2.length(); i++) {
                            obj3 = obj2.getJSONObject(i);
                            entity = new CommEntity();
                            entity.setID(obj3.getString("id"));
                            entity.setGoodsId(obj3.getString("goodsId"));
                            entity.setGoodsName(obj3.getString("dtitle"));
                            entity.setGoodsLink(obj3.getString("itemLink"));
                            entity.setActLink(obj3.getString("couponLink"));
                            entity.setImgUrl(obj3.getString("mainPic"));
                            entity.setActMoney(obj3.getString("couponPrice"));
                            entity.setGoodsPrice(obj3.getString("originalPrice"));
                            entity.setLastPrice(obj3.getString("actualPrice"));
                            commList.add(entity);
                        }

                        if (type == 0) {
                            //刷新模式不清空list
                            commonAdapter.refresh(commList);
                        } else if (type == 1) {
                            //搜索模式清空list
                            commonAdapter.superRefresh(commList);
                        }


                    } else {
                        sToast("错误DT:" + obj.getString("code") + "&" + obj.getString("msg"));
                    }
                } catch (JSONException e) {
                    sToast("列表解析失败，请稍后重试0x002");
                    e.printStackTrace();
                }

            }

        }, params, true);

    }

}
