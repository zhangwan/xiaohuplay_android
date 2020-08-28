package com.tinytiger.titi.sql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tinytiger.common.net.data.center.MineGameBean;

import java.util.ArrayList;

public class SQLiteDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "apk_download_db";

    /**
     * 数据库版本
     */
    public static final int VERSION = 2;

    /**
     * 数据库
     */
    private static SQLiteDB sqliteDB;

    private SQLiteDatabase db;

    private SQLiteDB(Context context) {
        /** 初始化数据库 */
        OpenHelper dbHelper = new OpenHelper(context, DB_NAME, null, VERSION);
        /** 获取db */
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取SqliteDB实例
     *
     * @param context
     */
    public synchronized static SQLiteDB getInstance(Context context) {
        if (sqliteDB == null) {
            sqliteDB = new SQLiteDB(context);
        }
        return sqliteDB;
    }


    /**
     * 添加数据
     *
     * @param item FollowGameBean
     */
    private void insert(MineGameBean item) {
//实例化常量值
        SQLiteDatabase db = sqliteDB.db;
        ContentValues cValue = new ContentValues();
        cValue.put("id", item.id);
        cValue.put("name", item.name);
        cValue.put("url", item.download_url);
        cValue.put("package", item.package_name_android);
        cValue.put("version", item.version);
        cValue.put("logo", item.logo);
        cValue.put("time", System.currentTimeMillis());
        db.insert(DB_NAME, null, cValue);
    }

    /**
     * 更新数据
     *
     * @param item FollowGameBean
     */
    private void update(MineGameBean item) {
        ContentValues values = new ContentValues();
        values.put("url", item.download_url);
        values.put("version", item.version);
        values.put("logo", item.logo);
        String whereClause = "id=?";
        String[] whereArgs = {item.id};
        db.update(DB_NAME, values, whereClause, whereArgs);
    }


    /**
     * 查询apk信息,存在更新信息,不存在创建apk
     * @param item
     */
    public void addApk(MineGameBean item){
        String whereClause = "id=?";
        String[] whereArgs = {item.id};
        Cursor cursor =db.query(DB_NAME, null,  whereClause, whereArgs, null, null, null);
        if (cursor.moveToFirst()) {
            update(item);
        }else {
            insert(item);
        }
    }

    /**
     * 查询所有数据
     */
    public ArrayList<MineGameBean> queryAllApk() {

        Cursor cursor1 = db.rawQuery("select * from  apk_download_db", null);
        ArrayList<MineGameBean> list = new ArrayList<MineGameBean>();
//查询获得游标
        Cursor cursor = db.query(DB_NAME, null, null, null, null, null, null);
//判断游标是否为空
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MineGameBean bean = new MineGameBean();
                bean.id= cursor.getString(cursor.getColumnIndex("id"));
                bean.name= cursor.getString(cursor.getColumnIndex("name"));
                bean.download_url= cursor.getString(cursor.getColumnIndex("url"));
                bean.package_name_android= cursor.getString(cursor.getColumnIndex("package"));
                bean.version= cursor.getString(cursor.getColumnIndex("version"));
                bean.logo= cursor.getString(cursor.getColumnIndex("logo"));
                bean.time=cursor.getLong(cursor.getColumnIndex("time"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }




    /**
     * 删除数据
     */
    public void delete(MineGameBean item) {
//删除条件
        String whereClause = "id=?";
//删除条件参数
        String[] whereArgs = {String.valueOf(item.id)};
//执行删除
        db.delete(DB_NAME,whereClause,whereArgs);
    }

    /**
     * 删除数据
     */
    public void delete(String id) {
//删除条件
        String whereClause = "id=?";
//删除条件参数
        String[] whereArgs = {String.valueOf(id)};
//执行删除
        db.delete(DB_NAME,whereClause,whereArgs);
    }
}