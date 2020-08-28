package com.tinytiger.common.view.picker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;
import com.tinytiger.common.view.picker.base.BaseDatePickerView;
import com.zyyoona7.wheel.WheelView;

/*
 * @author Tamas
 * create at 2019/11/17 0017
 * Email: ljw_163mail@163.com
 * description: 日期 选择View
 */
public class DatePickerView  extends BaseDatePickerView {

    private TextView mYearTv;
    private TextView mMonthTv;
    private TextView mDayTv;

    public DatePickerView(Context context) {
        this(context, null);
    }

    public DatePickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mYearTv = findViewById(R.id.tv_year);
        mMonthTv = findViewById(R.id.tv_month);
        mDayTv = findViewById(R.id.tv_day);
    }

    @Override
    protected int getDatePickerViewLayoutId() {
        return R.layout.layout_date_picker_view;
    }

    @Override
    protected int getYearWheelViewId() {
        return R.id.wv_year;
    }

    @Override
    protected int getMonthWheelViewId() {
        return R.id.wv_month;
    }

    @Override
    protected int getDayWheelViewId() {
        return R.id.wv_day;
    }

    /**
     * 设置标签字体大小
     *
     * @param textSize 字体大小
     */
    public void setLabelTextSize(float textSize) {
        mYearTv.setTextSize(textSize);
        mMonthTv.setTextSize(textSize);
        mDayTv.setTextSize(textSize);
    }

    /**
     * 设置标签字体大小
     *
     * @param unit     单位
     * @param textSize 字体大小
     */
    public void setLabelTextSize(int unit, float textSize) {
        mYearTv.setTextSize(unit, textSize);
        mMonthTv.setTextSize(unit, textSize);
        mDayTv.setTextSize(unit, textSize);
    }

    /**
     * 设置标签字体颜色
     *
     * @param textColorRes 颜色资源
     */
    public void setLabelTextColorRes(@ColorRes int textColorRes) {
        setLabelTextColor(ContextCompat.getColor(getContext(), textColorRes));
    }

    /**
     * 设置标签字体颜色
     *
     * @param textColor 颜色值
     */
    public void setLabelTextColor(@ColorInt int textColor) {
        mYearTv.setTextColor(textColor);
        mMonthTv.setTextColor(textColor);
        mDayTv.setTextColor(textColor);
    }

    /**
     * 设置是否显示标签
     *
     * @param isShowLabel 是否显示标签
     */
    public void setShowLabel(boolean isShowLabel) {
        if (isShowLabel) {
            setLabelVisibility(VISIBLE);
        } else {
            setLabelVisibility(GONE);
        }
    }

    /**
     * 统一设置标签可见性
     *
     * @param visibility 可见性值
     */
    private void setLabelVisibility(int visibility) {
        mYearTv.setVisibility(visibility);
        mMonthTv.setVisibility(visibility);
        mDayTv.setVisibility(visibility);
    }

    /**
     * 隐藏 日
     */
    public void hideDayItem() {
        setItemVisibility(GONE, mDayWv, mDayTv);
    }

    /**
     * 显示 日
     */
    public void showDayItem() {
        setItemVisibility(VISIBLE, mDayWv, mDayTv);
    }

    /**
     * 隐藏 月
     */
    public void hideMonthItem() {
        setItemVisibility(GONE, mMonthWv, mMonthTv);
    }

    /**
     * 显示 月
     */
    public void showMonthItem() {
        setItemVisibility(VISIBLE, mMonthWv, mMonthTv);
    }

    /**
     * 隐藏 年
     */
    public void hideYearItem() {
        setItemVisibility(GONE, mYearWv, mYearTv);
    }

    /**
     * 显示 年
     */
    public void showYearItem() {
        setItemVisibility(VISIBLE, mYearWv, mYearTv);
    }

    /**
     * 设置 item可见性
     *
     * @param visibility 可见性值
     * @param wheelView  WheelView
     * @param textView   labelView
     */
    private void setItemVisibility(int visibility, WheelView wheelView, TextView textView) {
        if (wheelView != null) {
            wheelView.setVisibility(visibility);
        }
        if (textView != null) {
            textView.setVisibility(visibility);
        }
    }

    /**
     * 设置年份范围
     *
     * @param start 起始年
     * @param end   结束年
     */
    public void setYearRange(int start, int end) {
        mYearWv.setYearRange(start, end);
    }

    /**
     * 获取选中的年份
     *
     * @return 选中的年份
     */
    public int getSelectedYear() {
        return mYearWv.getSelectedYear();
    }

    /**
     * 设置选中的年份
     *
     * @param year 年
     */
    public void setSelectedYear(int year) {
        setSelectedYear(year, false);
    }

    /**
     * 设置选中的年份
     *
     * @param year           年
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setSelectedYear(int year, boolean isSmoothScroll) {
        setSelectedYear(year, isSmoothScroll, 0);
    }

    /**
     * 设置选中的年份
     *
     * @param year           年
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setSelectedYear(int year, boolean isSmoothScroll, int smoothDuration) {
        mYearWv.setSelectedYear(year, isSmoothScroll, smoothDuration);
    }

    /**
     * 获取选中的月份
     *
     * @return 选中的月份
     */
    public int getSelectedMonth() {
        return mMonthWv.getSelectedMonth();
    }

    /**
     * 设置选中的月份
     *
     * @param month 月
     */
    public void setSelectedMonth(int month) {
        mMonthWv.setSelectedMonth(month, false);
    }

    /**
     * 设置选中的月份
     *
     * @param month          月
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setSelectedMonth(int month, boolean isSmoothScroll) {
        mMonthWv.setSelectedMonth(month, isSmoothScroll, 0);
    }

    /**
     * 设置选中的月份
     *
     * @param month          月
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setSelectedMonth(int month, boolean isSmoothScroll, int smoothDuration) {
        mMonthWv.setSelectedMonth(month, isSmoothScroll, smoothDuration);
    }

    /**
     * 获取选中的日
     *
     * @return 选中的日
     */
    public int getSelectedDay() {
        return mDayWv.getSelectedDay();
    }

    /**
     * 设置选中的日
     *
     * @param day 日
     */
    public void setSelectedDay(int day) {
        mDayWv.setSelectedDay(day, false);
    }

    /**
     * 设置选中的日
     *
     * @param day            日
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setSelectedDay(int day, boolean isSmoothScroll) {
        mDayWv.setSelectedDay(day, isSmoothScroll, 0);
    }

    /**
     * 设置选中的日
     *
     * @param day            日
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setSelectedDay(int day, boolean isSmoothScroll, int smoothDuration) {
        mDayWv.setSelectedDay(day, isSmoothScroll, smoothDuration);
    }

    /**
     * 获取选中的日期
     *
     * @return 选中的日期 格式 2018-08-22
     */
    public String getSelectedDate() {
        return getSelectedYear() + "-" + getSelectedMonth() + "-" + getSelectedDay();
    }

    /**
     * 设置当数据变化时，是否重置选中下标到第一个
     *
     * @param isResetSelectedPosition 当数据变化时,是否重置选中下标到第一个
     */
    public void setResetSelectedPosition(boolean isResetSelectedPosition) {
        mYearWv.setResetSelectedPosition(isResetSelectedPosition);
        mMonthWv.setResetSelectedPosition(isResetSelectedPosition);
        mDayWv.setResetSelectedPosition(isResetSelectedPosition);
    }

    /**
     * 设置设置WheelView 可见item数
     *
     * @param visibleItems 可见item数
     */
    public void setVisibleItems(int visibleItems) {
        mYearWv.setVisibleItems(visibleItems);
        mMonthWv.setVisibleItems(visibleItems);
        mDayWv.setVisibleItems(visibleItems);
    }

    /**
     * 设置是否自动调整字体大小，以显示完全
     *
     * @param isAutoFitTextSize 是否自动调整字体大小
     */
    public void setAutoFitTextSize(boolean isAutoFitTextSize) {
        mYearWv.setAutoFitTextSize(isAutoFitTextSize);
        mMonthWv.setAutoFitTextSize(isAutoFitTextSize);
        mDayWv.setAutoFitTextSize(isAutoFitTextSize);
    }

    /**
     * 设置WheelView 字体大小
     *
     * @param textSize 字体大小
     */
    public void setTextSize(float textSize) {
        setTextSize(textSize, false);
    }

    /**
     * 设置WheelView 字体大小
     *
     * @param textSize 字体大小
     * @param isDp     单位是否是dp
     */
    public void setTextSize(float textSize, boolean isDp) {
        mYearWv.setTextSize(textSize, isDp);
        mMonthWv.setTextSize(textSize, isDp);
        mDayWv.setTextSize(textSize, isDp);
    }

    /**
     * 设置WheelView 字体
     *
     * @param tf 字体
     */
    public void setTypeface(Typeface tf) {
        mYearWv.setTypeface(tf);
        mMonthWv.setTypeface(tf);
        mDayWv.setTypeface(tf);
    }

    /**
     * 设置WheelView 文字距离边界的外边距
     *
     * @param textBoundaryMargin 外边距值
     * @param isDp               单位是否为 dp
     */
    public void setTextBoundaryMargin(float textBoundaryMargin, boolean isDp) {
        mYearWv.setTextBoundaryMargin(textBoundaryMargin, isDp);
        mMonthWv.setTextBoundaryMargin(textBoundaryMargin, isDp);
        mDayWv.setTextBoundaryMargin(textBoundaryMargin, isDp);
    }

    /**
     * 设置WheelView 未选中item文字颜色
     *
     * @param textColorRes 颜色资源
     */
    public void setNormalItemTextColorRes(@ColorRes int textColorRes) {
        setNormalItemTextColor(ContextCompat.getColor(getContext(), textColorRes));
    }

    /**
     * 设置WheelView 未选中item文字颜色
     *
     * @param textColor 颜色
     */
    public void setNormalItemTextColor(@ColorInt int textColor) {
        mYearWv.setNormalItemTextColor(textColor);
        mMonthWv.setNormalItemTextColor(textColor);
        mDayWv.setNormalItemTextColor(textColor);
    }

    /**
     * 设置WheelView 选中item文字颜色
     *
     * @param textColorRes 颜色资源
     */
    public void setSelectedItemTextColorRes(@ColorRes int textColorRes) {
        setSelectedItemTextColor(ContextCompat.getColor(getContext(), textColorRes));
    }

    /**
     * 设置WheelView 选中item文字颜色
     *
     * @param textColor 颜色
     */
    public void setSelectedItemTextColor(@ColorInt int textColor) {
        mYearWv.setSelectedItemTextColor(textColor);
        mMonthWv.setSelectedItemTextColor(textColor);
        mDayWv.setSelectedItemTextColor(textColor);
    }

    /**
     * 设置WheelView 是否循环滚动
     *
     * @param isCyclic 是否循环
     */
    public void setCyclic(boolean isCyclic) {
        mYearWv.setCyclic(isCyclic);
        mMonthWv.setCyclic(isCyclic);
        mDayWv.setCyclic(isCyclic);
    }

    /**
     * 设置WheelView 行间距
     *
     * @param lineSpacing 行间距
     */
    public void setLineSpacing(float lineSpacing) {
        setLineSpacing(lineSpacing, false);
    }

    /**
     * 设置WheelView 行间距
     *
     * @param lineSpacing 行间距
     * @param isDp        单位是否是dp
     */
    public void setLineSpacing(float lineSpacing, boolean isDp) {
        mYearWv.setLineSpacing(lineSpacing, isDp);
        mMonthWv.setLineSpacing(lineSpacing, isDp);
        mDayWv.setLineSpacing(lineSpacing, isDp);
    }

    /**
     * 设置WheelView 是否开启滚动音效
     *
     * @param isSoundEffect 是否开启滚动音效
     */
    public void setSoundEffect(boolean isSoundEffect) {
        mYearWv.setSoundEffect(isSoundEffect);
        mMonthWv.setSoundEffect(isSoundEffect);
        mDayWv.setSoundEffect(isSoundEffect);
    }

    /**
     * 设置WheelView 滚动音效资源
     *
     * @param rawRes 资源
     */
    public void setSoundEffectResource(@RawRes int rawRes) {
        mYearWv.setSoundEffectResource(rawRes);
        mMonthWv.setSoundEffectResource(rawRes);
        mDayWv.setSoundEffectResource(rawRes);
    }

    /**
     * 设置WheelView 滚动音效播放音量
     *
     * @param playVolume 播放音量
     */
    public void setPlayVolume(@FloatRange(from = 0.0, to = 1.0) float playVolume) {
        mYearWv.setPlayVolume(playVolume);
        mMonthWv.setPlayVolume(playVolume);
        mDayWv.setPlayVolume(playVolume);
    }

    /**
     * 设置WheelView 是否显示分割线
     *
     * @param isShowDivider 是否显示分割线
     */
    public void setShowDivider(boolean isShowDivider) {
        mYearWv.setShowDivider(isShowDivider);
        mMonthWv.setShowDivider(isShowDivider);
        mDayWv.setShowDivider(isShowDivider);
    }

    /**
     * 设置WheelView 分割线颜色
     *
     * @param dividerColorRes 颜色资源
     */
    public void setDividerColorRes(@ColorRes int dividerColorRes) {
        setDividerColor(ContextCompat.getColor(getContext(), dividerColorRes));
    }

    /**
     * 设置WheelView 分割线颜色
     *
     * @param dividerColor 颜色值
     */
    public void setDividerColor(@ColorInt int dividerColor) {
        mYearWv.setDividerColor(dividerColor);
        mMonthWv.setDividerColor(dividerColor);
        mDayWv.setDividerColor(dividerColor);
    }

    /**
     * 设置WheelView 分割线高度
     *
     * @param dividerHeight 分割线高度
     */
    public void setDividerHeight(float dividerHeight) {
        setDividerHeight(dividerHeight, false);
    }

    /**
     * 设置WheelView 分割线高度
     *
     * @param dividerHeight 分割线高度
     * @param isDp          单位是否是dp
     */
    public void setDividerHeight(float dividerHeight, boolean isDp) {
        mYearWv.setDividerHeight(dividerHeight, isDp);
        mMonthWv.setDividerHeight(dividerHeight, isDp);
        mDayWv.setDividerHeight(dividerHeight, isDp);
    }

    /**
     * 设置WheelView 分割线类型
     *
     * @param dividerType 分割线类型
     */
    public void setDividerType(@WheelView.DividerType int dividerType) {
        mYearWv.setDividerType(dividerType);
        mMonthWv.setDividerType(dividerType);
        mDayWv.setDividerType(dividerType);
    }

    /**
     * 设置WheelView 自适应分割线类型时的分割线内边距
     *
     * @param dividerPaddingForWrap 分割线内边距
     * @param isDp                  单位是否是dp
     */
    public void setDividerPaddingForWrap(float dividerPaddingForWrap, boolean isDp) {
        mYearWv.setDividerPaddingForWrap(dividerPaddingForWrap, isDp);
        mMonthWv.setDividerPaddingForWrap(dividerPaddingForWrap, isDp);
        mDayWv.setDividerPaddingForWrap(dividerPaddingForWrap, isDp);
    }

    /**
     * 设置WheelView 是否绘制选中区域
     *
     * @param isDrawSelectedRect 是否绘制选中区域
     */
    public void setDrawSelectedRect(boolean isDrawSelectedRect) {
        mYearWv.setDrawSelectedRect(isDrawSelectedRect);
        mMonthWv.setDrawSelectedRect(isDrawSelectedRect);
        mDayWv.setDrawSelectedRect(isDrawSelectedRect);
    }

    /**
     * 设置WheelView 选中区域颜色
     *
     * @param selectedRectColorRes 颜色资源
     */
    public void setSelectedRectColorRes(@ColorRes int selectedRectColorRes) {
        setSelectedRectColor(ContextCompat.getColor(getContext(), selectedRectColorRes));
    }

    /**
     * 设置WheelView 选中区域颜色
     *
     * @param selectedRectColor 颜色值
     */
    public void setSelectedRectColor(@ColorInt int selectedRectColor) {
        mYearWv.setSelectedRectColor(selectedRectColor);
        mMonthWv.setSelectedRectColor(selectedRectColor);
        mDayWv.setSelectedRectColor(selectedRectColor);
    }

    /**
     * 设置WheelView 是否开启弯曲效果
     *
     * @param isCurved 是否开启
     */
    public void setCurved(boolean isCurved) {
        mYearWv.setCurved(isCurved);
        mMonthWv.setCurved(isCurved);
        mDayWv.setCurved(isCurved);
    }

    /**
     * 设置弯曲（3D）效果左右圆弧效果方向
     *
     * @param curvedArcDirection 左右圆弧效果方向
     *                           {@link WheelView#CURVED_ARC_DIRECTION_LEFT}
     *                           {@link WheelView#CURVED_ARC_DIRECTION_CENTER}
     *                           {@link WheelView#CURVED_ARC_DIRECTION_RIGHT}
     */
    public void setCurvedArcDirection(@WheelView.CurvedArcDirection int curvedArcDirection) {
        mYearWv.setCurvedArcDirection(curvedArcDirection);
        mMonthWv.setCurvedArcDirection(curvedArcDirection);
        mDayWv.setCurvedArcDirection(curvedArcDirection);
    }

    /**
     * 设置弯曲（3D）效果左右圆弧偏移效果方向系数
     *
     * @param curvedArcDirectionFactor 左右圆弧偏移效果方向系数
     *                                 range 0.0-1.0 越大越明显
     */
    public void setCurvedArcDirectionFactor(@FloatRange(from = 0.0f, to = 1.0f) float curvedArcDirectionFactor) {
        mYearWv.setCurvedArcDirectionFactor(curvedArcDirectionFactor);
        mMonthWv.setCurvedArcDirectionFactor(curvedArcDirectionFactor);
        mDayWv.setCurvedArcDirectionFactor(curvedArcDirectionFactor);
    }

    /**
     * 设置选中条目折射偏移比例
     *
     * @param curvedRefractRatio 折射偏移比例 range 0.0-1.0
     */
    @Deprecated
    public void setCurvedRefractRatio(@FloatRange(from = 0.0f, to = 1.0f) float curvedRefractRatio) {
        setRefractRatio(curvedRefractRatio);
    }

    /**
     * 设置选中条目折射偏移比例
     *
     * @param curvedRefractRatio 折射偏移比例 range 0.0-1.0
     */
    public void setRefractRatio(@FloatRange(from = 0.0f, to = 1.0f) float curvedRefractRatio) {
        mYearWv.setRefractRatio(curvedRefractRatio);
        mMonthWv.setRefractRatio(curvedRefractRatio);
        mDayWv.setRefractRatio(curvedRefractRatio);
    }

    /**
     * 获取年份 TextView
     *
     * @return 年份 TextView
     */
    public TextView getYearTv() {
        return mYearTv;
    }

    /**
     * 获取月份 TextView
     *
     * @return 月份 TextView
     */
    public TextView getMonthTv() {
        return mMonthTv;
    }

    /**
     * 获取日 TextView
     *
     * @return 日 TextView
     */
    public TextView getDayTv() {
        return mDayTv;
    }

}