package com.xz.jlw2.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.xz.base.BaseRecyclerAdapter;
import com.xz.base.BaseRecyclerViewHolder;
import com.xz.jlw2.R;
import com.xz.jlw2.entity.CommEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonAdapter extends BaseRecyclerAdapter<CommEntity> {
    public CommonAdapter(Context context) {
        super(context);
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CommEntity entity = mList.get(position);
        //加载主图
        Glide.with(mContext).load(entity.getImgUrl()).into(viewHolder.mainPic);
        viewHolder.goodsQuan.setText("领"+entity.getActMoney()+"元券");
        viewHolder.goodsTitle.setText(entity.getGoodsName());
        viewHolder.goodsBefore.setText(entity.getGoodsPrice() + "￥");
        viewHolder.goodsBefore.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.goodsNew.setText(entity.getLastPrice() + "￥");
    }

    @Override
    protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_common, parent, false));
    }


    static class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.main_pic)
        ImageView mainPic;
        @BindView(R.id.goods_title)
        TextView goodsTitle;
        @BindView(R.id.goods_before)
        TextView goodsBefore;
        @BindView(R.id.goods_new)
        TextView goodsNew;
        @BindView(R.id.goods_quan)
        TextView goodsQuan;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}