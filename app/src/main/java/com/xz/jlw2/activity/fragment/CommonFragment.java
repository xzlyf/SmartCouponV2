package com.xz.jlw2.activity.fragment;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;
import com.xz.base.BaseFragment;
import com.xz.base.OnItemClickListener;
import com.xz.jlw2.R;
import com.xz.jlw2.adapter.ClassifyAdapter;
import com.xz.jlw2.adapter.CommonAdapter;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.ClassifyEntity;
import com.xz.jlw2.entity.CommEntity;
import com.xz.utils.SpacesItemDecorationHorizontal;
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
    private List<CommEntity> commList = new ArrayList<>();
    private ClassifyAdapter classifyAdapter;
    private List<ClassifyEntity> classifyList = new ArrayList<>();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        commonAdapter = new CommonAdapter(mContext);
        classifyAdapter = new ClassifyAdapter(mContext);
        //获取分类图标
        GetClassify();
        //只加载一次数据，除非退出否则不再刷新数据
        netGetCommon();
    }



    @Override
    protected int getLayout() {
        return R.layout.fragment_common;
    }

    @Override
    protected void initView(View rootView) {
        //分类列表
        RecyclerView classRecycler = rootView.findViewById(R.id.class_recycler);
        GridLayoutManager manager = new GridLayoutManager(mContext,2);
        manager.setOrientation(GridLayoutManager.HORIZONTAL);
        classRecycler.setLayoutManager(manager);
        classRecycler.setAdapter(classifyAdapter);
        //SnapHelper snapHelper = new LinearSnapHelper();
        //snapHelper.attachToRecyclerView(classRecycler);
        classifyAdapter.setOnItemClickListener(new OnItemClickListener<ClassifyEntity>() {
            @Override
            public void onItemClick(View view, int position, ClassifyEntity model) {
                sToast("测试功能:"+model.getName());
            }

            @Override
            public void onItemLongClick(View view, int position, ClassifyEntity model) {
                sToast("长按测试功能:"+model.getName());

            }
        });

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
                        commList.clear();
                        for (int i = 0; i < obj2.length(); i++) {
                            commList.add(gson.fromJson(obj2.getJSONObject(i).toString(), CommEntity.class));
                        }
                        commonAdapter.refresh(commList);

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

    /**
     * 获取分类图标及名称
     */
    private void GetClassify() {
        classifyList.add(new ClassifyEntity("天猫新品",R.drawable.ic_1));
        classifyList.add(new ClassifyEntity("今日爆款",R.drawable.ic_2));
        classifyList.add(new ClassifyEntity("天猫国际",R.drawable.ic_3));
        classifyList.add(new ClassifyEntity("饿了么",R.drawable.ic_4));
        classifyList.add(new ClassifyEntity("天猫超市",R.drawable.ic_5));
        classifyList.add(new ClassifyEntity("充值中心",R.drawable.ic_6));
        classifyList.add(new ClassifyEntity("机票酒店",R.drawable.ic_7));
        classifyList.add(new ClassifyEntity("阿里拍卖",R.drawable.ic_8));
        classifyList.add(new ClassifyEntity("淘宝吃货",R.drawable.ic_9));
        classifyList.add(new ClassifyEntity("拍拍",R.drawable.ic_10));
        classifyList.add(new ClassifyEntity("咸鱼优品",R.drawable.ic_11));
        classifyList.add(new ClassifyEntity("美团外卖",R.drawable.ic_12));
        classifyList.add(new ClassifyEntity("电影票",R.drawable.ic_13));
        classifyList.add(new ClassifyEntity("新品促销",R.drawable.ic_14));
        classifyAdapter.refresh(classifyList);
    }
}
