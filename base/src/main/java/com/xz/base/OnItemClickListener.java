package com.xz.base;

import android.view.View;

interface OnItemClickListener<T> {
    void onItemClick(View view, int position, T model);

    void onItemLongClick(View view, int position, T model);
}
