package com.tinytiger.titi.widget.dialog


import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.widget.dialog.base.BaseDialog
import com.tinytiger.titi.R
import kotlinx.android.synthetic.main.dialog_game_screen.*
import java.lang.StringBuilder


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description:  游戏详情 筛选 dialog
*/
class GameScreenDialog : BaseDialog() {


    private var mFragmentManager: FragmentManager? = null


    companion object {
        fun create(manager: FragmentManager): GameScreenDialog {
            val dialog = GameScreenDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }


    }


    override fun getLayoutRes(): Int = R.layout.dialog_game_screen


    override fun bindView(v: View?) {

        if (curStars.isNotEmpty()) {
            val stars = curStars.split(",")
            for (index in stars) {
                when (index) {
                    "1" -> star_1.isChecked = true
                    "2" -> star_2.isChecked = true
                    "3" -> star_3.isChecked = true
                    "4" -> star_4.isChecked = true
                    "5" -> star_5.isChecked = true
                }
            }
        }


        tv_sure.setOnClickListener {

            val str = StringBuilder()
            if (star_1.isChecked) {
                str.append("1").append(",")
            }
            if (star_2.isChecked) {
                str.append("2").append(",")
            }
            if (star_3.isChecked) {
                str.append("3").append(",")
            }
            if (star_4.isChecked) {
                str.append("4").append(",")
            }
            if (star_5.isChecked) {
                str.append("5").append(",")
            }
            if (str.isEmpty()) {
                Toast.makeText(context, "请选择星级~", Toast.LENGTH_SHORT).show()
            } else {
                str.deleteCharAt(str.length - 1)
                mViewListener?.click(str.toString())
            }


            dismiss()
        }

    }


    fun setFragmentManager(manager: FragmentManager): GameScreenDialog {
        mFragmentManager = manager
        return this
    }


    fun show(): BaseDialog {
        show(mFragmentManager)
        return this
    }

    fun setViewListener(listener: ViewListener): GameScreenDialog {
        this.mViewListener = listener
        return this
    }

    private var curStars = ""

    fun setStars(selectStars: String): GameScreenDialog {
        curStars = selectStars
        return this
    }


    private var mViewListener: ViewListener? = null

    interface ViewListener {
        fun click(string: String)
    }


}
