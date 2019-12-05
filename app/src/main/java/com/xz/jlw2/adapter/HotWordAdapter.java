package com.xz.jlw2.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.xz.base.BaseRecyclerAdapter;
import com.xz.base.BaseRecyclerViewHolder;
import com.xz.jlw2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotWordAdapter extends BaseRecyclerAdapter<String> {
    public HotWordAdapter(Context context) {
        super(context);
    }

    @Override
    protected void showViewHolder(BaseRecyclerViewHolder holder, int position) {
        String hot = mList.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.hotText.setText(hot);
    }

    @Override
    protected BaseRecyclerViewHolder createNewViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_hot_word, parent, false));
    }

    class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.hot_text)
        TextView hotText;

        ViewHolder(View view) {
            super(view);
            hotText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Logger.w("执行");
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getLayoutPosition(), mList.get(getLayoutPosition()));
            }
        }
    }
}
