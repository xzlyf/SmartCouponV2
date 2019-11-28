package com.xz.jlw2.adapter;

import android.content.Context;
import android.graphics.Paint;
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

import com.bumptech.glide.Glide;
import com.xz.base.BaseRecyclerAdapter;
import com.xz.base.BaseRecyclerViewHolder;
import com.xz.base.utils.ToastUtil;
import com.xz.jlw2.R;
import com.xz.jlw2.entity.CommEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonAdapter extends BaseRecyclerAdapter<CommEntity> {
    private final String TAG = getClass().getName();
    private ViewHolder lastViewHolder;

    public CommonAdapter(Context context) {
        super(context);
        initAnim();
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CommEntity entity = mList.get(position);
        //加载主图
        Glide.with(mContext).load(entity.getImgUrl()).into(viewHolder.mainPic);
        viewHolder.goodsQuan.setText("领" + entity.getActMoney() + "元券");
        viewHolder.goodsTitle.setText(entity.getGoodsName());
        viewHolder.goodsBefore.setText(entity.getGoodsPrice() + "￥");
        viewHolder.goodsBefore.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.goodsNew.setText(entity.getLastPrice() + "￥");

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(viewHolder.itemView, position, entity);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(viewHolder.itemView, position, entity);
                    return false;
                }
            });
        }
    }

    @Override
    protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_common, parent, false));
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
        FrameLayout layoutMenu;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            item_1.setOnClickListener(this);
            item_2.setOnClickListener(this);
            item_3.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (lastViewHolder != null) {
                hideMenu();
                lastViewHolder = null;
            }
            if (v instanceof TextView) {
                ToastUtil.Shows(mContext, "测试功能:" + ((TextView) v).getText());
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

}