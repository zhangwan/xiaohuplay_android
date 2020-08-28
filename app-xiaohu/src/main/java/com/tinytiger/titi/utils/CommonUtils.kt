package com.tinytiger.titi.utils



/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description: 
*/
object CommonUtils {

    fun getGenderString(gender: Int): String {
        return if (gender == 1) "男" else "女"
    }

    fun getGenderNum(gender: String): String {
        return if (gender == "男") "1" else "2"
    }

    fun isLimitedCity(prov: String): Boolean {

        return when (prov) {
            "北京", "天津", "上海", "重庆", "香港", "澳门", "台湾" -> true
            else -> false
        }

    }
}