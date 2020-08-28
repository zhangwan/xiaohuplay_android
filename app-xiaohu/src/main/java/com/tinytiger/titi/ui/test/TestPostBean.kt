package com.tinytiger.titi.ui.test

import com.tinytiger.common.http.response.BaseResp
import com.tinytiger.common.http.response.PageVo
import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.net.data.circle.PostBean
import java.util.*

/*
 * @author zwy
 * create at 2020/6/5 0005
 * description:
 */
class TestPostResp : BaseResp<PageVo<TestPostBean>>()
class TestPostBean {
    var id: String? = null
    // # 回答id -1:动态贴, 0:问答帖(未采纳) 大于0:问答帖(已采纳的id)
    var answer_id = 0
    // #该帖子属于的模块id （用于跳转）
    var modular_id: String? = null
    var create_time: String? = null

    var content: String? = null
    var view_num = 0
    var is_like = 0
    var img_url: ArrayList<String>? = null

    var view_user_num = 0
    var like_num = 0
    var comment_num = 0
    var comment_user_num: String? = null

    var share_num: String? = null
    var collect_num: String? = null
    var user_id: String? = null
    var avatar = ""
    var master_type = 0

    var nickname: String? = null
    var medal_image: String? = null


    var is_mutual = 0
    var modular_name: String? = null
    var is_post_collect: String? = null

    var is_post_like: String? = null
    var circle_list: ArrayList<CircleBean>? = null

    var circle_post: ArrayList<CircleBean>? = null
    //主圈名称
    var circle_name: String? = null

    var participate_num = 0
    var isSelected = false
    var total_hots = 0
    var share_url: String? = null
    var collect_time: String? = null
    var circle_id: String? = null

    // #该帖子属于的模块类型 1精华 2问答 3综合 （用于跳转）
    var fixed_modular_type: String? = null
}
