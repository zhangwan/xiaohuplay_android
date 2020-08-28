package com.tinytiger.titi.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.util.logging.Logger;

/**
 * Created by admin on 2018/6/11.
 */
public class OpenHelper extends SQLiteOpenHelper {

    //建表语句（创建用户表）
    public static final String CREATE_DOWNLOAD = "create table apk_download_db ("
            + "id integer primary key autoincrement, "
            + "name text, "
            + "url text, "
            + "package text, "
            + "version text, "
            + "logo text,"
            + "time Long )";

    /**
     * 构造方法
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    /**
     * 初次创建
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建用户表
        db.execSQL(CREATE_DOWNLOAD);
    }

    /**
     * 当数据库版本出现改变时
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        addColumn(db,new String[]{"time"},"apk_download_db");

    }
    private void addColumn(SQLiteDatabase db, String[] newColumnArr,
                           String oldTableName){

        if (db == null || newColumnArr == null || newColumnArr.length < 1
                || TextUtils.isEmpty(oldTableName)) {
            // 数据库为空，新字段个数为0，添加字段后的字段数组个数为0，旧表名为空
            return;
        }
        // 拿到旧表中所有的数据
        Cursor cursor = db.rawQuery("select * from " + oldTableName, null);
        if (cursor == null) {
            return;
        }
        // 拿到原来的表中所有的字段名
        String[] oldColumnNames = cursor.getColumnNames();
        // 更改原表名为临时表
        String tempTableName = "_temp_" + oldTableName;
        db.execSQL("alter table " + oldTableName + " rename to " + tempTableName);
        // 创建新表
        if (oldColumnNames.length < 1) {
            // 如果原来的表中字段个数为0
            return;
        }
        // 创建一个线程安全的字符串缓冲对象，防止用conn多线程访问数据库时造成线程安全问题
        StringBuffer createNewTableStr = new StringBuffer();
        createNewTableStr
                .append("create table if not exists " + oldTableName + "(");
        for (int i = 0; i < oldColumnNames.length; i++) {
            if (i == 0) {
                createNewTableStr.append(oldColumnNames[i]
                        + " integer primary key autoincrement,");
            } else {
                createNewTableStr.append(oldColumnNames[i] + ",");
            }
            if(newColumnArr[0].equals(oldColumnNames[i])){
                return;
            }
        }

        for (int i = 0; i < newColumnArr.length; i++) {
            if (i == newColumnArr.length - 1) {
                // 最后一个
                createNewTableStr.append(newColumnArr[i] + " Long)");
            } else {
                // 不是最后一个
                createNewTableStr.append(newColumnArr[i] + " Long,");
            }
        }
        db.execSQL(createNewTableStr.toString());
        // 复制旧表数据到新表
        StringBuffer copySQLStr = new StringBuffer();
        copySQLStr.append("insert into " + oldTableName + " select *,");
        // 有多少个新的字段，就要预留多少个' '空值给新字段
        for (int i = 0; i < newColumnArr.length; i++) {
            if (i == newColumnArr.length - 1) {
                // 最后一个
                copySQLStr.append("' ' from " + tempTableName);
            } else {
                // 不是最后一个
                copySQLStr.append("' ',");
            }
        }

        db.execSQL(copySQLStr.toString());
        // 删除旧表
        db.execSQL("drop table " + tempTableName);

        // 关闭游标
        cursor.close();
    }

}
