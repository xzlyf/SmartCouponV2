package com.xz.jlw2.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SqlUtil {

    private static final String TAG = "SqlUtil.class";//数据库名称
    private static final String DB_NAME = "user.db";//数据库名称
    private static final int DB_VERSION = 1;//数据版本
    private static SqlUtil mInstance;
    private static SQLiteDatabase db_write;
    private static SQLiteDatabase db_read;

    private SqlUtil(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        db_write = dbHelper.getWritableDatabase();
        db_read = dbHelper.getReadableDatabase();
    }

    private static SqlUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (SqlUtil.class) {
                if (mInstance == null) {
                    mInstance = new SqlUtil(context);
                }
            }

        }
        return mInstance;
    }

    /**
     * 插入数据
     *
     * @return 新插入行的行ID，如果发生错误，则为-1
     */
    private long _insert(String table, ContentValues v) {
        return db_write.insert(table, null, v);
    }

    /**
     * 更新数据
     *
     * @return 受影响的行数
     */
    private int _update(String table, ContentValues v, String whereClause, String[] whereArgs) {
        return db_write.update(table, v, whereClause, whereArgs);
    }

    /**
     * 删除数据
     *
     * @return 如果传入一个whereClause，则影响的行数，否则为0。
     * 删除所有行并获得一个count pass“1”作为whereClause。
     */
    private int _delete(String table, String whereClause, String[] whereArgs) {
        return db_write.delete(table, whereClause, whereArgs);
    }

    /**
     * ===========================================公开方法===========================================
     */
    public static long insert(Context context, String table, ContentValues v) {
        return getInstance(context)._insert(table, v);
    }

    public static int update(Context context, String table, ContentValues values, String whereClause, String[] whereArgs) {
        return getInstance(context)._update(table, values, whereClause, whereArgs);
    }

    public static int delete(Context context, String table, String whereClause, String[] whereArgs) {
        return getInstance(context)._delete(table, whereClause, whereArgs);
    }

}
