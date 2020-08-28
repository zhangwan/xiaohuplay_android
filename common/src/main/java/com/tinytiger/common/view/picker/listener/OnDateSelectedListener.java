package com.tinytiger.common.view.picker.listener;

import androidx.annotation.Nullable;

import com.tinytiger.common.view.picker.base.BaseDatePickerView;

import java.util.Date;

/*
 * @author Tamas
 * create at 2019/11/17 0017
 * Email: ljw_163mail@163.com
 * description: 日期选中监听器
 */
public interface OnDateSelectedListener {

    /**
     * @param datePickerView BaseDatePickerView
     * @param year           选中的年份
     * @param month          选中的月份
     * @param day            选中的天
     * @param date           选中的日期
     */
    void onDateSelected(BaseDatePickerView datePickerView, int year, int month,
                        int day, @Nullable Date date);
}