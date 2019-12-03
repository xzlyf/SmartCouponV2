package com.xz.jlw2.activity.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orhanobut.logger.Logger;
import com.xz.base.BaseFragment;
import com.xz.base.OnItemClickListener;
import com.xz.jlw2.R;
import com.xz.jlw2.activity.DetailActivity;
import com.xz.jlw2.activity.MainActivity;
import com.xz.jlw2.adapter.CommonAdapter;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.CommEntity;
import com.xz.jlw2.sql.DatabaseHelper;
import com.xz.jlw2.sql.SqlUtil;
import com.xz.utils.SpacesItemDecorationVertical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TABLE_CART = "cart";//表名
    private List<CommEntity> commList;
    private CommonAdapter commonAdapter;
    private SwipeRefreshLayout swipeRefresh;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Local.CODE_5:
                    commonAdapter.superRefresh(commList);
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
        commonAdapter = new CommonAdapter(context);
        commonAdapter.setHandler(handler);
        commonAdapter.setMode(Local.MODE_CART);//切换至购物车模式
        commonAdapter.setOnItemClickListener(new OnItemClickListener<CommEntity>() {
            @Override
            public void onItemClick(View view, int position, CommEntity model) {
                mContext.startActivity(new Intent(mContext, DetailActivity.class)
                        .putExtra("goodsid", model.getGoodsId()));
            }

            @Override
            public void onItemLongClick(View view, int position, CommEntity model) {

            }
        });
        //获取购物车数据
        new ReadDataThread(context).start();

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initView(View rootView) {
        //商品列表
        RecyclerView commonRecycler = rootView.findViewById(R.id.common_recycler);
        commonRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        commonRecycler.setAdapter(commonAdapter);
        commonRecycler.addItemDecoration(new SpacesItemDecorationVertical(10));
        commonRecycler.setItemViewCacheSize(20);


        swipeRefresh = rootView.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);

    }

    @Override
    protected void initDate(Context mContext) {

        if (Local.cartChange) {
            //购物车发生改变再次刷新
            new ReadDataThread(mContext).start();
            //还原标识
            Local.cartChange = false;
        }

    }

    /**
     * 下拉刷新回调
     */
    @Override
    public void onRefresh() {
        //刷新购物车
        new ReadDataThread(mContext).start();
    }


    /**
     * 异步读取数据
     */
    class ReadDataThread extends Thread {
        Context context;

        ReadDataThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            //读取本地数据库数据
            Cursor cursor = SqlUtil.queryAll(context, TABLE_CART);
            //如果游标为空则返回false
            if (cursor.moveToFirst()) {
                commList = new ArrayList<>();
                CommEntity entity;
                do {
                    entity = new CommEntity();
                    entity.setID(cursor.getString(cursor.getColumnIndex("id")));
                    entity.setGoodsId(cursor.getString(cursor.getColumnIndex("goodsid")));
                    entity.setGoodsName(cursor.getString(cursor.getColumnIndex("goodsname")));
                    entity.setGoodsLink(cursor.getString(cursor.getColumnIndex("goodslink")));
                    entity.setActLink(cursor.getString(cursor.getColumnIndex("actlink")));
                    entity.setImgUrl(cursor.getString(cursor.getColumnIndex("imgurl")));
                    entity.setActMoney(cursor.getString(cursor.getColumnIndex("actmoney")));
                    entity.setGoodsPrice(cursor.getString(cursor.getColumnIndex("previos")));
                    entity.setLastPrice(cursor.getString(cursor.getColumnIndex("later")));
                    commList.add(entity);

                } while (cursor.moveToNext());

                //反转数据
                Collections.reverse(commList); //时间新的在前
                //发送至handler显示数据
                Message message = handler.obtainMessage();
                message.what = Local.CODE_5;
                handler.sendMessage(message);
            } else {
                Logger.w("购物车空");
            }
            cursor.close();


        }
    }
}
