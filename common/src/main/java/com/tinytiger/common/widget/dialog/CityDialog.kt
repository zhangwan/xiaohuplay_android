package com.tinytiger.common.widget.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tinytiger.common.widget.dialog.base.BaseBottomDialog
import kotlinx.android.synthetic.main.dialog_bottom_gender.tv_cancel
import kotlinx.android.synthetic.main.dialog_bottom_gender.tv_sure
import com.tinytiger.common.view.picker.bean.CityEntity
import com.tinytiger.common.view.picker.OptionsPickerView
import com.tinytiger.common.view.picker.listener.OnOptionsSelectedListener
import com.zyyoona7.wheel.WheelView
import com.tinytiger.common.view.picker.listener.OnPickerScrollStateChangedListener
import com.tinytiger.common.R
import com.tinytiger.common.net.data.mine.CityBean
import com.tinytiger.common.view.picker.util.ParseHelper


/*
* @author Tamas
* create at 2019/11/17 0017
* Email: ljw_163mail@163.com
* description: 
*/
class CityDialog : BaseBottomDialog() {


    private var mOptionsPickerView: OptionsPickerView<CityEntity>? = null

    private var mOptionsSelectedListener: OnOptionsSelectedListener<CityEntity>? = null


    companion object {
        fun create(manager: FragmentManager): CityDialog {
            val dialog = CityDialog()
            dialog.setFragmentManager(manager)
            return dialog
        }
    }

    private var mFragmentManager: FragmentManager? = null

    private fun setFragmentManager(manager: FragmentManager): CityDialog {
        mFragmentManager = manager
        return this
    }

    override fun getLayoutRes(): Int = R.layout.dialog_bottom_city

    override fun bindView(v: View?) {
        tv_cancel.setOnClickListener {
            dismiss()
        }
        tv_sure.setOnClickListener {
                mOptionsSelectedListener?.onOptionsSelected(
                    mOptionsPickerView!!.opt1SelectedPosition,
                    mOptionsPickerView!!.opt1SelectedData,
                    mOptionsPickerView!!.opt2SelectedPosition,
                    mOptionsPickerView!!.opt2SelectedData,
                    mOptionsPickerView!!.opt3SelectedPosition,
                    mOptionsPickerView!!.opt3SelectedData
                )

            dismiss()
        }
        mOptionsPickerView = view!!.findViewById(R.id.opv_city)
        mOptionsPickerView!!.setLineSpacing(15f, true)

        mOptionsPickerView?.onOptionsSelectedListener =
            OnOptionsSelectedListener { _, _, _, _, _, _ ->
            }


        mOptionsPickerView?.setOnPickerScrollStateChangedListener(OnPickerScrollStateChangedListener { state ->
            tv_sure.setEnabled(
                state == WheelView.SCROLL_STATE_IDLE
            )
        })


    }


    private var provcn="广东"
    private var citycn="深圳"


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(cityList == null){
            return
        }

        val p3List = ArrayList<CityEntity>(1)
        val c3List = ArrayList<List<CityEntity>>(1)
        ParseHelper.initTwoLevelCityList(cityList, p3List, c3List)

        mOptionsPickerView!!.setLinkageData(p3List, c3List)


        for ((index,prov) in p3List.withIndex()){
            if(prov.name.indexOf(provcn) != -1){
                for((i,city) in p3List[index].districts.withIndex()){
                    if(city.name.indexOf(citycn) != -1){

                        mOptionsPickerView!!.opt1SelectedPosition=index
                        mOptionsPickerView!!.opt2SelectedPosition=i
                        return
                    }
                }

            }
        }


    }



    fun show(): BaseBottomDialog {

        show(mFragmentManager)
        return this
    }

    fun setOnSelectedListener(listener: OnOptionsSelectedListener<CityEntity>): CityDialog {
        this.mOptionsSelectedListener = listener
        return this
    }

    fun setCity(provcn:String?,citycn:String?): CityDialog {
        if(provcn!=null && provcn.isNotEmpty()){
            this.provcn = provcn
        }

        if(citycn!=null  && citycn.isNotEmpty()){
            this.citycn = citycn
        }

        return this
    }

    private var cityList:List<CityBean.ProvinceBean>?=null

    fun setCityList(bean:List<CityBean.ProvinceBean>): CityDialog {
        cityList = bean

        return this
    }
}