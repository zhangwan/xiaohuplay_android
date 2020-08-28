package com.tinytiger.titi.ui.test


import androidx.recyclerview.widget.LinearLayoutManager
import com.tinytiger.common.basis.BasisActivity
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.titi.R
import com.tinytiger.titi.adapter.post.CommonPostNodeAdapter
import com.tinytiger.titi.ui.test.vp.TestPresenter
import kotlinx.android.synthetic.main.activity_base_recycler.*

/*
 * @author zwy
 * create at 2020/6/5 0005
 * description:
 */
class TestActivity :BasisActivity<TestPresenter>(){
    private val mAdapter by lazy { CommonPostNodeAdapter(this,ArrayList()) }
    override fun layoutId(): Int = R.layout.release_fragment_dynamic

    override fun initData() {

    }

    override fun initView() {
        //评论详情
        recycler_view.layoutManager = LinearLayoutManager(this)
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