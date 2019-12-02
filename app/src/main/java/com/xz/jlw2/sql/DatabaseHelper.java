package com.xz.jlw2.sql;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.orhanobut.logger.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String CREATE_CART = "create table cart (" +
            "id text," +
            "goodsid text," +
            "goodsname text," +
            "goodslink text," +
            "actlink text," +
            "imgurl text," +
            "actmoney text," +
            "previos text," +
            "later text)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    /**
     * 初始化工作
     * 初始化表，没有就重新创建一张
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
