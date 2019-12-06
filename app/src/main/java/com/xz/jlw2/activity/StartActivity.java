package com.xz.jlw2.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xz.base.BaseActivity;
import com.xz.jlw2.R;

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    @Override
    public boolean homeAsUpEnabled() {
        return false;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_start;
    }

    @Override
    public void initData() {

    }
}
