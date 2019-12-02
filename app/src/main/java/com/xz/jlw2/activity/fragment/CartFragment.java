package com.xz.jlw2.activity.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import androidx.annotation.NonNull;

import com.xz.base.BaseFragment;
import com.xz.jlw2.R;
import com.xz.jlw2.activity.MainActivity;
import com.xz.jlw2.sql.DatabaseHelper;
import com.xz.jlw2.sql.SqlUtil;

public class CartFragment extends BaseFragment {
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initDate(Context mContext) {
        //=============================增
        ContentValues values = new ContentValues();
        values.put("id", "906836");
        values.put("goodsid", "GoodsId");
        values.put("goodsname", "联想笔记本电脑通用键盘保护贴膜");
        values.put("goodslink", "https://detail.tmall.com/item.htm?id=530483020458");
        values.put("actlink", "https://uland.taobao.com/quan/detail?sellerId=2074630256&activityId=38ba9f89b2df4eac9b1d037951d2ef47");
        values.put("imgurl", "https://img.alicdn.com/imgextra/i3/2074630256/TB24QnynpXXXXavXpXXXXXXXXXX_!!2074630256.jpg");
        values.put("actmoney", "1");
        values.put("previos", "6.8");
        values.put("later", "5.8");
        //SqlUtil.insert(mContext,"cart",values);//插入数据


        //=============================改
        ContentValues values1 = new ContentValues();
        values.put("id", "666666");//需要更新的数据
        //更新数据，更新goodsname为“联想笔记本电脑通用键盘保护贴膜”行的id中的数据
        //SqlUtil.update(mContext, "cart", values, "goodsname = ?", new String[]{"联想笔记本电脑通用键盘保护贴膜"});


        //=============================删
        //删除id等于666666的行
        //SqlUtil.delete(mContext,"cart","id=?",new String[]{"666666"});



    }
}
