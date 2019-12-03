package com.xz.jlw2.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.xz.base.BaseRecyclerAdapter;
import com.xz.base.BaseRecyclerViewHolder;
import com.xz.base.utils.ToastUtil;
import com.xz.jlw2.R;
import com.xz.jlw2.activity.MainActivity;
import com.xz.jlw2.constant.Local;
import com.xz.jlw2.entity.CommEntity;
import com.xz.jlw2.sql.SqlUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonAdapter extends BaseRecyclerAdapter<CommEntity> {
    private ViewHolder lastViewHolder;
    //普通布局的type
    static final int TYPE_ITEM = 0;
    //脚布局
    static final int TYPE_FOOTER = 1;
    private Handler handler;
    private int mode = 0;//当前模式  -0首页模式 -994购物车模式

    public CommonAdapter(Context context) {
        super(context);
        initAnim();
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }


    /**
     * 切换模式
     * 0 -首页模式
     * 994 -购物车模式
     *
     * @param mode
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            CommEntity entity = mList.get(position);
            //加载主图
            Glide.with(mContext)
                    .load(entity.getImgUrl())
                    .override(100,100)
                    .thumbnail(0.1f)//缩略图
                    .into(viewHolder.mainPic);
            viewHolder.goodsQuan.setText("领" + entity.getActMoney() + "元券");
            viewHolder.goodsTitle.setText(entity.getGoodsName());
            viewHolder.goodsBefore.setText(entity.getGoodsPrice() + "￥");
            viewHolder.goodsBefore.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.goodsNew.setText(entity.getLastPrice() + "￥");

        } else if (holder instanceof FooterHolder) {

            switch (mode) {
                case Local.MODE_CART:
                    ((FooterHolder) holder).itemView.setVisibility(View.GONE);
                    break;
                case 0:
                    if (mList.size() > 1) {
                        ((FooterHolder) holder).refreshTips.setText("加载更多...");
                        ((FooterHolder) holder).itemView.setEnabled(true);
                    }
                    break;
            }

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

    @Override
    protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ViewHolder(mInflater.inflate(R.layout.item_common, parent, false));
        } else if (viewType == TYPE_FOOTER) {
            return new FooterHolder(mInflater.inflate(R.layout.item_footer_more, parent, false));
        } else {
            return null;
        }

    }

    /**
     * Add or Remove
     * 收藏或移除操作
     */
    private void AOR(int position) {
        CommEntity entity = mList.get(position);
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
                long request = SqlUtil.insert(mContext, "cart", values);//插入数据
                if (request == -1) {
                    ToastUtil.Shows(mContext, "收藏失败");
                } else {
                    ToastUtil.Shows(mContext, "已加入购物车");
                    Local.cartChange = true;
                }

                break;
            case Local.MODE_CART:
                //移除
                SqlUtil.delete(mContext, "cart", "id = ?", new String[]{entity.getID()});
                //刷新购物车
                mList.remove(position);
                notifyDataSetChanged();

                break;
        }


    }


    private Animation showTranslateAnim;
    private Animation hideTranslateAnim;

    /**
     * 展示动画
     */
    private void initAnim() {

        showTranslateAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0f);
        showTranslateAnim.setInterpolator(new OvershootInterpolator());
        showTranslateAnim.setDuration(300);
        hideTranslateAnim = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, -1f,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0f);
        hideTranslateAnim.setInterpolator(new AccelerateInterpolator());
        hideTranslateAnim.setDuration(300);
    }

    class ViewHolder extends BaseRecyclerViewHolder implements View.OnLongClickListener, View.OnClickListener {
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
        @BindView(R.id.item_1)
        TextView item_1;//自定义功能
        @BindView(R.id.item_2)
        TextView item_2;//自定义功能
        @BindView(R.id.item_3)
        TextView item_3;//自定义功能
        @BindView(R.id.layout_menu)
        CardView layoutMenu;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            switch (mode) {
                case 0:
                    item_1.setOnClickListener(this);
                    item_2.setOnClickListener(this);
                    item_3.setOnClickListener(this);
                    break;
                case Local.MODE_CART:
                    item_1.setText("移除");
                    item_1.setOnClickListener(this);
                    item_2.setVisibility(View.GONE);
                    item_3.setVisibility(View.GONE);

            }
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.item_1:
                    AOR(getLayoutPosition());
                    break;
                case R.id.item_2:
                    ToastUtil.Shows(mContext, "测试功能:" + ((TextView) v).getText());
                    break;
                case R.id.item_3:
                    ToastUtil.Shows(mContext, "测试功能:" + ((TextView) v).getText());
                    break;
            }
            //动画
            if (lastViewHolder != null) {
                hideMenu();
                lastViewHolder = null;
                return;
            }
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getLayoutPosition(), mList.get(getLayoutPosition()));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            //自动关闭上一个打开的菜单
            if (lastViewHolder != null) {
                hideMenu();
                lastViewHolder = this;
            } else {
                lastViewHolder = this;
            }
            showMenu();
            return true;
        }

        private void showMenu() {
            layoutMenu.setVisibility(View.VISIBLE);
            layoutMenu.startAnimation(showTranslateAnim);
        }

        private void hideMenu() {
            lastViewHolder.layoutMenu.startAnimation(hideTranslateAnim);
            lastViewHolder.layoutMenu.setVisibility(View.GONE);
        }


    }

    class FooterHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.refresh_tips)
        TextView refreshTips;

        FooterHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            }
        }
    }

}