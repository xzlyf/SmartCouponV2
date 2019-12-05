package com.xz.jlw2.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseActivity;
import com.xz.jlw2.R;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.CommEntity;
import com.xz.jlw2.utils.TaobaoUtil;
import com.xz.utils.network.OkHttpClientManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.main_pic)
    ImageView mainPic;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.goods_title)
    TextView goodsTitle;
    @BindView(R.id.goods_remark)
    TextView goodsRemark;
    @BindView(R.id.submit)
    TextView submit;
    @BindView(R.id.jump)
    TextView jump;

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @Override
    public boolean homeAsUpEnabled() {
        return true;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    public void initData() {
        String goodsis = getIntent().getStringExtra("goodsid");

        netGetDetail(goodsis);
    }

    /**
     * 显示数据
     *
     * @param entity
     */
    private void showDetail(CommEntity entity) {

        Glide.with(this)
                .load(entity.getImgUrl())
                .error(R.drawable.svg_lose)
                .into(mainPic);

        goodsTitle.setText(entity.getGoodsName());
        goodsRemark.setText(entity.getTjRemark());
        submit.setText("立即领券");
        jump.setText("跳转至商品页面");
        submit.setOnClickListener(v -> {
            //跳转到淘宝领券界面
            TaobaoUtil.jump2TaobaoQuan(DetailActivity.this, entity.getActLink());
        });
        jump.setOnClickListener(v -> {
            //跳转到淘宝商品界面
            TaobaoUtil.jump2TaobaoQuan(DetailActivity.this, entity.getGoodsLink());
        });

    }


    /**
     * 获取详情页数据
     *
     * @param goodsis
     */
    private void netGetDetail(String goodsis) {

        Map<String, Object> params = new HashMap<>();
        params.put("appkey", Local.APIKEY);
        params.put("id", goodsis);
        OkHttpClientManager.getAsyn(this, Local.BASE_URL + Local.ITEM, new OkHttpClientManager.ResultCallback<String>() {
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
                        JSONObject obj3 = obj2.getJSONObject(0);
                        Gson gson = new Gson();
                        CommEntity entity = gson.fromJson(obj3.toString(), CommEntity.class);

                        showDetail(entity);
                    } else {
                        sToast("列表获取失败，请稍后重试0x007");
                    }

                } catch (JSONException e) {
                    sToast("列表解析失败，请稍后重试");
                    e.printStackTrace();
                }

            }

        }, params, true);

    }


}
