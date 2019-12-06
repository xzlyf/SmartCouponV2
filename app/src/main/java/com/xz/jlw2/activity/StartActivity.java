package com.xz.jlw2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseActivity;
import com.xz.jlw2.R;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.DailyStringEntity;
import com.xz.utils.SharedPreferencesUtil;
import com.xz.utils.TimeUtil;
import com.xz.utils.network.OkHttpClientManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends BaseActivity {

    private int time = 3000;

    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.main_pic)
    ImageView mainPic;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean homeAsUpEnabled() {
        return false;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_start;
    }

    @Override
    public void initData() {

        sayHello();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        }, time);

    }

    private void sayHello() {


        //尝试获取本地已缓存的数据
        String json = SharedPreferencesUtil.getString(mContext, "data", "start", "null");
        if (!json.equals("null")) {
            Gson gson = new Gson();
            DailyStringEntity entity = gson.fromJson(json, DailyStringEntity.class);
            String date = entity.getDateline();//2019-12-06
            String nowDate = TimeUtil.getSimMilliDate("yyyy-MM-dd", System.currentTimeMillis());
            //如果现在时间跟存储时间一致则显示本地的
            if (date.equals(nowDate)) {
                showData(entity);
                return;
            }
        }


        Map<String, Object> params = new HashMap<>();
        OkHttpClientManager.getAsyn(mContext, Local.JINSHAN_DAYLIS, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.e("每日一句获取失败:" + request.url());
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);
                    //保存当天每日一句内容
                    SharedPreferencesUtil.saveString(mContext, "data", "start", obj.toString());
                    Gson gson = new Gson();

                    showData(gson.fromJson(obj.toString(), DailyStringEntity.class));
                } catch (JSONException e) {
                    Logger.e("每日一句解析出了问题：" + e.getMessage());
                    e.printStackTrace();
                }
            }

        }, params, false);

    }

    /**
     * 展示数据
     *
     * @param fromJson
     */
    private void showData(DailyStringEntity fromJson) {

        content.setText(fromJson.getContent());
        note.setText(fromJson.getNote());

    }


}
