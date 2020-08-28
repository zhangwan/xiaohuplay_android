package com.tinytiger.titi.ui.test.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.basis.BasisFragment
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.ui.test.TestPostBean
import com.tinytiger.titi.ui.test.vp.TestFragmentPresenter
import kotlinx.android.synthetic.main.activity_base_recycler.*

/*
 * @author zwy
 * create at 2020/6/9 0009
 * description:
 */
class Testfragment : BasisFragment<TestFragmentPresenter>() {
    private val mAdapter by lazy { CommonPostNodeAdapter(activity!!,ArrayList()) }
    override fun getLayoutId(): Int {
        return R.layout.release_fragment_dynamic
    }

    override fun initView() {
        //评论详情
        recycler_view.layoutManager = LinearLayoutManager(activity!!)
        recycler_view.adapter=mAdapter
        basePresenter?.getPostList(1)

    }

    fun showAnswersNodeList(list: PageVo<TestPostBean>?) {
        var data=list?.data
        if (data != null) {
            for(item in data){

            }
        }
    }

}