package com.xz.jlw2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xz.base.BaseRecyclerAdapter;
import com.xz.base.BaseRecyclerViewHolder;
import com.xz.base.OnItemClickListener;
import com.xz.jlw2.R;
import com.xz.jlw2.entity.ClassifyEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClassifyAdapter extends BaseRecyclerAdapter<ClassifyEntity> {

    public ClassifyAdapter(Context context) {
        super(context);
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ClassifyEntity entity = mList.get(position);

        viewHolder.classIcon.setImageResource(entity.getSrc());
        viewHolder.className.setText(entity.getName());

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
                    return true;
                }
            });
        }
    }

    @Override
    protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_class, parent, false));
    }

    class ViewHolder extends BaseRecyclerViewHolder implements View.OnTouchListener {
        @BindView(R.id.class_icon)
        CircleImageView classIcon;
        @BindView(R.id.class_name)
        TextView className;

        ViewHolder(View view) {
            super(view);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setScaleX(0.9f);
                v.setScaleY(0.9f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);
            } else if (event.getAction() ==MotionEvent.ACTION_CANCEL){

                v.setScaleX(1.0f);
                v.setScaleY(1.0f);
            }
            return false;
        }
    }
}
