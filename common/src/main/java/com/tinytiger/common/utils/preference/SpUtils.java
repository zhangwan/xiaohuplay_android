package com.tinytiger.common.utils.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.tinytiger.common.base.BaseApp;


/**
 *
 *@author luke
 *@date 2018/9/6 10:18
 *@doc sp基本设置
 *
 */
public class SpUtils {
    private static final String SP_NAM = "xh_info";


    public static void  saveSP(String key, Object value){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = null;
        if (value instanceof String){
            editor = sp.edit().putString(key,(String)value);
        }else if(value instanceof Integer){
            editor = sp.edit().putInt(key, (Integer) value);
        }else if(value instanceof Float){
            editor = sp.edit().putFloat(key,(Float)value);
        }else if(value instanceof Boolean){
            editor = sp.edit().putBoolean(key, (Boolean) value);
        }else if(value instanceof Long){
            editor = sp.edit().putLong(key, (Long) value);
        }else if(value instanceof Double){
            float value1=(float) value;
            editor = sp.edit().putFloat(key, value1);
        }else if(value instanceof Float){
            editor = sp.edit().putFloat(key, (float)value);
        }else{
            return;
        }
        editor.commit();
    }
    public static void  saveSP(int resId,Object value){
        saveSP( BaseApp.getContext().getString(resId),value);
    }

    /**
     * 获取类型String的值
     * @param key
     * @return
     */
    public static String getString(String key, String defaultValue){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        return  sp.getString(key,defaultValue);
    }

    /**
     * 获取类型String的值
     * @return
     */
    public static String getString(int resId, String defaultValue){
        String key = BaseApp.getContext().getString(resId);
        return getString(key,defaultValue);
    }

    /**
     * 获取类型int的值
     * @param key
     * @return
     */
    public static int getInt(String key, int defaultValue){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    /**
     * 获取类型int的值
     * @return
     */
    public static int getInt(int resId,int defaultValue){
        String key = BaseApp.getContext().getString(resId);
        return getInt(key,defaultValue);
    }


    /**
     * 获取类型float的值
     * @param key
     * @return
     */
    private static float getFloat(String key, float defaultValue){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        return sp.getFloat(key, defaultValue);
    }

    /**
     * 获取类型float的值
     * @return
     */
    public static float getFloat(int resId,float defaultValue){
        String key = BaseApp.getContext().getString(resId);
        return getFloat(key,defaultValue);
    }
    public static float getFloat(int resId){
        String key = BaseApp.getContext().getString(resId);
        return getFloat(key,0);
    }

    /**
     * 获取类型long的值
     * @param key
     * @return
     */
    public static Long getLong(String key, Long defaultValue){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        return sp.getLong(key, defaultValue);
    }

    /**
     * 获取类型long的值
     * @return
     */
    public static Long getLong(int resId, long defaultValue){
        String key = BaseApp.getContext().getString(resId);
        return getLong(key,defaultValue);
    }

    /**
     * 获取类型boolean的值
     * @param key
     * @return
     */
    public static boolean getBoolean(String key, boolean defaultValue){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }
    /**
     * 获取类型boolean的值
     * @return
     */
    public static boolean getBoolean(int resId,boolean defaultValue){
        String key = BaseApp.getContext().getString(resId);
        return getBoolean(key,defaultValue);
    }


    /**
     * 清空app所有数据
     */
    public static void clearSp(){
        SharedPreferences sp = BaseApp.getContext().getSharedPreferences(SP_NAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
