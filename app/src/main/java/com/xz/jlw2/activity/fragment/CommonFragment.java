package com.xz.jlw2.activity.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseFragment;
import com.xz.base.OnItemClickListener;
import com.xz.jlw2.R;
import com.xz.jlw2.adapter.ClassifyAdapter;
import com.xz.jlw2.adapter.CommonAdapter;
import com.xz.jlw2.adapter.HotWordAdapter;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.ClassifyEntity;
import com.xz.jlw2.entity.CommEntity;
import com.xz.jlw2.utils.SignMD5Util;
import com.xz.utils.SpacesItemDecorationVertical;
import com.xz.utils.network.OkHttpClientManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonFragment extends BaseFragment {

    private List<String> banner_path = new ArrayList<>();
    private List<String> banner_title = new ArrayList<>();
    private CommonAdapter commonAdapter;
    private List<CommEntity> commList = new ArrayList<>();
    private ClassifyAdapter classifyAdapter;
    private List<ClassifyEntity> classifyList = new ArrayList<>();
    private HotWordAdapter hotWordAdapter;
    private List<String> hotList = new ArrayList<>();
    private int page = 1;//默认商品页数
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case Local.CODE_1:
                        //加载商品数据
                        netGetCommon(++page);
                        break;
                }
            }
        };
        commonAdapter = new CommonAdapter(mContext);
        commonAdapter.setHandler(handler);
        classifyAdapter = new ClassifyAdapter(mContext);
        hotWordAdapter = new HotWordAdapter(mContext);
        classifyAdapter.setOnItemClickListener(new OnItemClickListener<ClassifyEntity>() {
            @Override
            public void onItemClick(View view, int position, ClassifyEntity model) {
                sToast("测试功能:" + model.getName());
            }

            @Override
            public void onItemLongClick(View view, int position, ClassifyEntity model) {
                sToast("长按测试功能:" + model.getName());

            }
        });

        commonAdapter.setOnItemClickListener(new OnItemClickListener<CommEntity>() {
            @Override
            public void onItemClick(View view, int position, CommEntity model) {
                sToast("商品点击");
            }

            @Override
            public void onItemLongClick(View view, int position, CommEntity model) {
                sToast("长按商品");

            }
        });



        /*
            只加载一次数据，除非退出否则不再刷新数据
         */
        //获取分类图标
        GetClassify();
        //获取搜索热词
        netGetHotWord();
        //加载商品数据
        netGetCommon(page);
        //加载banner
        netGetBanner();

    }


    @Override
    protected int getLayout() {
        return R.layout.fragment_common;
    }

    @Override
    protected void initView(View rootView) {
        Banner banner = rootView.findViewById(R.id.banner);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setImageLoader(new MyLoader());
        banner.setImages(banner_path);
        banner.setBannerTitles(banner_title);
        banner.setDelayTime(3500);
        banner.setBannerAnimation(Transformer.Default);
        banner.isAutoPlay(true);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
        //热词列表
        RecyclerView hotReycler = rootView.findViewById(R.id.hot_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hotReycler.setLayoutManager(linearLayoutManager);
        hotReycler.setAdapter(hotWordAdapter);
        hotReycler.setNestedScrollingEnabled(true);//滑动没有惯性
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(hotReycler);


        //分类列表
        RecyclerView classRecycler = rootView.findViewById(R.id.class_recycler);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        classRecycler.setLayoutManager(gridLayoutManager);
        classRecycler.setAdapter(classifyAdapter);
        //SnapHelper snapHelper = new LinearSnapHelper();
        //snapHelper.attachToRecyclerView(classRecycler);

        //商品列表
        RecyclerView commonRecycler = rootView.findViewById(R.id.common_recycler);
        commonRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        commonRecycler.setAdapter(commonAdapter);
        commonRecycler.addItemDecoration(new SpacesItemDecorationVertical(10));
        commonRecycler.setItemViewCacheSize(20);

    }

    @Override
    protected void initDate(Context mContext) {

    }

    /**
     * 获取网络搜索热词
     */
    private void netGetHotWord() {
        Map<String, Object> params = new HashMap<>();
        params.put("appKey", Local.APIKEY_DTK);
        params.put("version", Local.VERSION_DTK);
        params.put("sign", SignMD5Util.getSignStr(params, Local.APP_SECRET_DTK));
        OkHttpClientManager.getAsyn(mContext, Local.HOTWORD, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.e("请求失败：" + request.url());
                sToast("网络请求失败，请稍后重试0x002");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("code").equals("0")) {
                        JSONArray obj2 = obj.getJSONObject("data").getJSONArray("hotWords");
                        for (int i = 0; i < obj2.length(); i++) {
                            hotList.add(obj2.getString(i));
                        }
                        hotWordAdapter.refresh(hotList);
                    } else {
                        sToast("错误代码:" + obj.getString("code"));
                    }

                } catch (JSONException e) {
                    sToast("列表解析失败，请稍后重试0x002");
                    e.printStackTrace();
                }
            }

        }, params, true);
    }


    /**
     * 获取banner图片资源
     */
    private void netGetBanner() {
        banner_path.add("https://s2.ax1x.com/2019/11/29/QApcpd.jpg");
        banner_path.add("https://s2.ax1x.com/2019/11/29/QAp26I.jpg");
        banner_path.add("https://s2.ax1x.com/2019/11/29/QApste.jpg");
        banner_path.add("https://s2.ax1x.com/2019/11/29/QApyfH.jpg");
        banner_title.add("双十一狂欢");
        banner_title.add("黑色星期五促销");
        banner_title.add("1212购物狂欢");
        banner_title.add("圣诞节促销");
    }


    /**
     * 获取分类图标及名称
     */
    private void GetClassify() {
        classifyList.add(new ClassifyEntity("天猫新品", R.drawable.ic_1));
        classifyList.add(new ClassifyEntity("今日爆款", R.drawable.ic_2));
        classifyList.add(new ClassifyEntity("天猫国际", R.drawable.ic_3));
        classifyList.add(new ClassifyEntity("饿了么", R.drawable.ic_4));
        classifyList.add(new ClassifyEntity("天猫超市", R.drawable.ic_5));
        classifyList.add(new ClassifyEntity("充值中心", R.drawable.ic_6));
        classifyList.add(new ClassifyEntity("机票酒店", R.drawable.ic_7));
        classifyList.add(new ClassifyEntity("阿里拍卖", R.drawable.ic_8));
        classifyList.add(new ClassifyEntity("淘宝吃货", R.drawable.ic_9));
        classifyList.add(new ClassifyEntity("拍拍", R.drawable.ic_10));
        classifyList.add(new ClassifyEntity("咸鱼优品", R.drawable.ic_11));
        classifyList.add(new ClassifyEntity("美团外卖", R.drawable.ic_12));
        classifyList.add(new ClassifyEntity("电影票", R.drawable.ic_13));
        classifyList.add(new ClassifyEntity("新品促销", R.drawable.ic_14));
        classifyAdapter.refresh(classifyList);
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    /**
     * 获取普通商品数据
     */
    private void netGetCommon(int page) {

        Map<String, Object> params = new HashMap<>();
        params.put("appkey", Local.APIKEY);
        params.put("page", page);
        params.put("pagesize", 50);
        params.put("sort", 7);
        OkHttpClientManager.getAsyn(mContext, Local.BASE_URL + Local.INDEX, new OkHttpClientManager.ResultCallback<String>() {
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
                        commList.clear();
                        for (int i = 0; i < obj2.length(); i++) {
                            commList.add(gson.fromJson(obj2.getJSONObject(i).toString(), CommEntity.class));
                        }
                        commonAdapter.refresh(commList);

                    } else {
                        sToast("列表获取失败，请稍后重试0x001");
                    }

                } catch (JSONException e) {
                    sToast("列表解析失败，请稍后重试0x001");
                    e.printStackTrace();
                }

            }

        }, params, true);
    }

}
