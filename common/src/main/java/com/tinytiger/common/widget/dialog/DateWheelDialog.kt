package com.tinytiger.common.widget.dialog


import android.view.*
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.R
import com.tinytiger.common.widget.base.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_date_wheel.*
import top.defaults.view.DateTimePickerView
import java.util.*


/**
 * @author: Tamas
 * @date: On 2018/11/12 0026
 * @desc:  dialog date wheel
 */
class DateWheelDialog : BaseBottomDialog() {


    private var mFragmentManager: FragmentManager? = null

    companion object {
        fun create(manager: FragmentManager): DateWheelDialog {
            val dialog = DateWheelDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }


    override fun getLayoutRes(): Int = R.layout.dialog_date_wheel

    override fun bindView(v: View?) {
        initDate()

        tv_cancel.setOnClickListener {
            dismiss()
        }

        tv_sure.setOnClickListener {
            mViewListener?.click(
                String.format(
                    Locale.getDefault(),
                    "%d-%02d-%02d",
                    mYear,
                    mMonth,
                    mDay
                )
            )
            dismiss()
        }
    }

    /**
     * 选中时间
     */
    var mYear: Int = 2000
    var mMonth: Int = 1
    var mDay: Int = 1


    private fun initDate() {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)-70)
        calendar.set(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_YEAR, 1)
        datePicker.setStartDate(calendar)

        val cale = Calendar.getInstance()

        cale.set(Calendar.YEAR, cale.get(Calendar.YEAR) - 6)
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH))
        cale.set(Calendar.DAY_OF_YEAR, cale.get(Calendar.DAY_OF_YEAR))
        datePicker.setEndDate(cale)

        datePicker.selectedDate = GregorianCalendar(mYear, mMonth - 1, mDay)

        datePicker.setOnSelectedDateChangedListener(object : DateTimePickerView.OnSelectedDateChangedListener {
            override fun onSelectedDateChanged(date: Calendar) {
                mYear = date.get(Calendar.YEAR)
                mMonth = date.get(Calendar.MONTH) + 1
                mDay = date.get(Calendar.DAY_OF_MONTH)
            }
        })
    }

    private fun setFragmentManager(manager: FragmentManager): DateWheelDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): BaseBottomDialog {
        show(mFragmentManager)
        return this
    }

    fun setViewListener(listener: ViewListener): DateWheelDialog {
        this.mViewListener = listener
        return this
    }


    fun setViewDate(date: String): DateWheelDialog {
        if (date.isNotEmpty() && date.length == 11) {
            mYear = date.substring(0, 4).toInt()
            mMonth = date.substring(5, 7).toInt()
            mDay = date.substring(8, 10).toInt()
        }
        return this
    }

    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(date: String)
    }

}
