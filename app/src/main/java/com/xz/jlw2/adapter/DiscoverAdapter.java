package com.xz.jlw2.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xz.base.BaseRecyclerAdapter;
import com.xz.base.BaseRecyclerViewHolder;
import com.xz.base.utils.ToastUtil;
import com.xz.jlw2.R;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.CommEntity;
import com.xz.jlw2.entity.DiscoverEntity;
import com.xz.jlw2.sql.SqlManager;

import butterknife.BindView;

public class DiscoverAdapter extends BaseRecyclerAdapter<DiscoverEntity> {
    //普通布局的type
    private static final int TYPE_ITEM = 0;
    //脚布局
    private static final int TYPE_FOOTER = 1;

    private Handler handler;

    public DiscoverAdapter(Context context) {
        super(context);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            DiscoverEntity entity = mList.get(position);
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.shopName.setText(entity.getShopName());
            viewHolder.createTime.setText(entity.getCreateDateTime());
            viewHolder.content.setText(entity.getCopyRemark());
            viewHolder.goodsTitle.setText(entity.getGoodsName());
            viewHolder.previousPrice.setText(entity.getGoodsPrice());
            viewHolder.previousPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.lastPrice.setText(entity.getLastPrice());
            Glide.with(mContext).load(entity.getImgUrl()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.mainPic);
            Glide.with(mContext).load(entity.getRealImg()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.pic1);
            Glide.with(mContext).load(entity.getRealImg2()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.pic2);
            Glide.with(mContext).load(entity.getRealImg3()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.pic3);
            Glide.with(mContext).load(entity.getRealImg4()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.pic4);
            Glide.with(mContext).load(entity.getRealImg5()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.pic5);
            Glide.with(mContext).load(entity.getRealImg6()).thumbnail(0.3f).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.pic6);
        } else if (holder instanceof FooterHolder) {

            if (mList.size() > 1) {
                ((FooterHolder) holder).refreshTips.setText("加载更多...");
                ((FooterHolder) holder).itemView.setEnabled(true);
            }

        }
    }

    @Override
    protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ViewHolder(mInflater.inflate(R.layout.item_discover, parent, false));
        } else if (viewType == TYPE_FOOTER) {
            return new FooterHolder(mInflater.inflate(R.layout.item_footer_more, parent, false));
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    /**
     * Add or Remove
     * 收藏或移除操作
     *
     * @param mode 0 收藏 1移除
     */
    private void AOR(int position, int mode) {
        DiscoverEntity entity = mList.get(position);
        switch (mode) {
            case 0:
                //收藏
                ContentValues values = new ContentValues();
                values.put("id", entity.getID());
                values.put("goodsid", entity.getGoodsId());
                values.put("goodsname", entity.getGoodsName());
                values.put("goodslink", entity.getGoodsLink());
                values.put("actlink", entity.getActLink());
                values.put("imgurl", entity.getImgUrl());
                values.put("actmoney", entity.getActMoney());
                values.put("previos", entity.getGoodsPrice());
                values.put("later", entity.getLastPrice());
                long request = SqlManager.insert(mContext, "cart", values);//插入数据
                if (request == -1) {
                    ToastUtil.Shows(mContext, "收藏失败");
                } else {
                    ToastUtil.Shows(mContext, "已加入购物车");
                    Local.cartChange = true;
                }
                break;
            case 1:
                //移除
                SqlManager.delete(mContext, "cart", "id = ?", new String[]{entity.getID()});
                //刷新购物车
                mList.remove(position);
                notifyDataSetChanged();
                break;
        }

    }

    class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.shop_name)
        TextView shopName;
        @BindView(R.id.create_time)
        TextView createTime;
        @BindView(R.id.collect)
        Button collect;
        @BindView(R.id.content)
        TextView content;
        @BindView(R.id.pic_1)
        ImageView pic1;
        @BindView(R.id.pic_2)
        ImageView pic2;
        @BindView(R.id.pic_3)
        ImageView pic3;
        @BindView(R.id.pic_4)
        ImageView pic4;
        @BindView(R.id.pic_5)
        ImageView pic5;
        @BindView(R.id.pic_6)
        ImageView pic6;
        @BindView(R.id.img_flock)
        GridLayout imgFlock;
        @BindView(R.id.main_pic)
        ImageView mainPic;
        @BindView(R.id.goods_title)
        TextView goodsTitle;
        @BindView(R.id.previous_price)
        TextView previousPrice;
        @BindView(R.id.last_price)
        TextView lastPrice;
        @BindView(R.id.goods)
        ConstraintLayout goodsLayout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            collect.setOnClickListener(this);
            goodsLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.collect:
                    if (collect.getText().equals("收藏")) {
                        collect.setText("已收藏");
                        AOR(getLayoutPosition(), 0);
                    } else if (collect.getText().equals("已收藏")) {
                        collect.setText("收藏");
                        AOR(getLayoutPosition(), 1);
                    }
                    break;
                case R.id.goods:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, getLayoutPosition(), mList.get(getLayoutPosition()));
                    }
                    break;
            }
        }
    }


    class FooterHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.refresh_tips)
        TextView refreshTips;

        FooterHolder(@NonNull View itemView) {
            super(itemView);
            // 上拉刷新点击事件
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            refreshTips.setText("正在加载...");
            itemView.setEnabled(false);

            if (handler != null) {
                Message message = handler.obtainMessage();
                message.what = Local.CODE_1;
                handler.sendMessage(message);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshTips.setText("暂无更多");
                        itemView.setEnabled(true);

                    }
                }, 1000);

            }
        }
    }
}