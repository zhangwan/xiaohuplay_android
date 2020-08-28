
package com.tinytiger.common.net


import com.tinytiger.common.net.data.BaseBean
import com.tinytiger.common.net.data.InitBean
import com.tinytiger.common.net.data.QiniuBean
import com.tinytiger.common.net.data.ad.AdListBean
import com.tinytiger.common.net.data.center.*
import com.tinytiger.common.net.data.circle.PostInfoBean
import com.tinytiger.common.net.data.gametools.*
import com.tinytiger.common.net.data.gametools.comment.CommentAddBean
import com.tinytiger.common.net.data.gametools.comment.CommentAssessInfo
import com.tinytiger.common.net.data.gametools.wiki.WikiModularList
import com.tinytiger.common.net.data.gametools.wiki.WikiModularOtherList
import com.tinytiger.common.net.data.home.*
import com.tinytiger.common.net.data.home2.AmwayWallListBean
import com.tinytiger.common.net.data.home2.ContentListBean
import com.tinytiger.common.net.data.home2.GameCategoryBean
import com.tinytiger.common.net.data.login.AgreementBean
import com.tinytiger.common.net.data.login.ForgetBean
import com.tinytiger.common.net.data.login.LoginBean
import com.tinytiger.common.net.data.mine.*
import com.tinytiger.common.net.data.msg.AddCommentBean
import com.tinytiger.common.net.data.msg.IndexMsgCommentListBean
import com.tinytiger.common.net.data.msg.ReplyListBean
import com.tinytiger.common.net.data.msg.UserKitBean
import com.tinytiger.common.net.data.props.*
import com.tinytiger.common.net.data.search.*
import com.tinytiger.common.net.data.user.FollowUserBean
import com.tinytiger.common.net.data.user.FriendListBean
import com.tinytiger.common.net.data.user.LikeBean
import com.tinytiger.common.net.data.video.CommentDetailListBean
import com.tinytiger.common.net.data.video.CommentListBean
import com.tinytiger.common.net.data.video.ContentInfoBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 *
 * @author zhw_luke
 * @date 2019/10/22 0022 下午 3:01
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc api
 */
interface ApiService {

    /**
     * app配置信息 上传类型 1=图片（默认） 2=视频
     */
    @GET("Common/getQiniuToken?")
    fun getQiniuToken(@Query("type") type: Int = 1): Observable<QiniuBean>

    @GET("Common/getAreaList?")
    fun getAreaList(): Observable<CityBean>

    @GET("Common/startApp?")
    fun startApp(): Observable<InitBean>

    @FormUrlEncoded
    @POST("Content/addShare?")
    fun addShare(@Field("content_id") content_id: String, @Field("share_channel_id") share_channel_id: Int): Observable<BaseBean>

    @FormUrlEncoded
    @POST("GameAssess/addShare?")
    fun addShareGameAssess(@Field("assess_id") assess_id: String, @Field("share_channel_id") share_channel_id: Int): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Post/addShare?")
    fun addSharePost(@Field("post_id") post_id: String, @Field("share_channel_id") share_channel_id: Int): Observable<BaseBean>





    @FormUrlEncoded
    @POST("Content/changeViewLength?")
    fun changeViewLength(@Field("view_log_id") view_log_id: String, @Field("view_time") view_time: Int,@Field("view_video_time") view_video_time: Int): Observable<BaseBean>


    @FormUrlEncoded
    @POST("AdClickLog/clickAdRecord?")
    fun clickAdRecord(@Field("ad_id") ad_id: String,@Field("user_id") user_id: String,@Field("position_id") position_id: String): Observable<BaseBean>

    /**
     * *******************首页*******************************************************************
     */

    @GET("recommend/getAdList?")
    fun getAdList(): Observable<AdListBean>

    @GET("recommend/getRecommendContentList?")
    fun getRecommendContentList(): Observable<RecommendListBean>

    @GET("game/getCategoryList?")
    fun getCategoryList(): Observable<CategoryListBean>

    @GET("game/getGameContentList?")
    fun getGameContentList(@Query("category_id") category_id: Int): Observable<GameListBean>


    @GET("Content/indexContent?")
    fun indexContent(@Query("page") page: Int): Observable<HeadLineListBean>

    /**
     * *******************搜索*******************************************************************
     */
    @GET("search/getKeyword?")
    fun getKeyword(@Query("title") title: String): Observable<SearchKeywordBean>

    @GET("search/searchAll?")
    fun searchAll(@Query("title") title: String): Observable<SearchAllBean>

    @GET("search/searchGame?")
    fun searchGame(@Query("title") title: String, @Query("page") page: Int): Observable<SearchGameBean>

    @GET("search/searchUser?")
    fun searchUser(@Query("title") title: String, @Query("page") page: Int): Observable<SearchUserBean>

    @GET("search/searchContent?")
    fun searchContent(@Query("title") title: String, @Query("page") page: Int): Observable<SearchNewsBean>


    @GET("search/getHotKeyword?")
    fun getHotKeyword(): Observable<HotKeyBean>


    @GET("search/gameCollection?")
    fun gameCollection(@Query("game_name") game_name: String): Observable<BaseBean>

    /**
     * *******************登录*******************************************************************
     */


    @GET("login/getAgreement?")
    fun getAgreement(): Observable<AgreementBean>

    @FormUrlEncoded
    @POST("Login/sendVerificationCode?")
    fun sendVerificationCode(@Field("phone") phone: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Login/verificationCodeLogin?")
    fun verificationCodeLogin(@Field("phone") phone: String, @Field("verification_code") verification_code: String): Observable<LoginBean>

    @FormUrlEncoded
    @POST("UserOperation/verificationPhone?")
    fun verificationPhone(@Field("verification_code") verification_code: String, @Field("password") password: String): Observable<BaseBean>
    @FormUrlEncoded
    @POST("UserOperation/verificationPhone?")
    fun verificationPhone(@Field("verification_code") verification_code: String): Observable<BaseBean>



    @FormUrlEncoded
    @POST("UserOperation/changePhone?")
    fun changePhone(@Field("phone") phone: String, @Field("verification_code") verification_code: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Login/passwordLogin?")
    fun passwordLogin(@Field("phone") phone: String, @Field("password") password: String): Observable<LoginBean>

    @FormUrlEncoded
    @POST("Login/recoverPassword?")
    fun recoverPassword(@Field("phone") phone: String, @Field("verification_code") verification_code: String): Observable<ForgetBean>

    @FormUrlEncoded
    @POST("Login/wxLogin?")
    fun wxLogin(
        @Field("unionid") unionid: String, @Field("nickname") nickname: String, @Field("avatar") avatar: String, @Field(
            "gender"
        ) gender: String
    ): Observable<LoginBean>

    @FormUrlEncoded
    @POST("Login/qqLogin?")
    fun qqLogin(
        @Field("qq_unionid") qq_unionid: String, @Field("nickname") nickname: String, @Field("avatar") avatar: String, @Field(
            "gender"
        ) gender: String
    ): Observable<LoginBean>


    @FormUrlEncoded
    @POST("Login/bindPhone?")
    fun bindPhone(
        @Field("bind_type") bind_type: String,
        @Field("bind_unionid") bind_unionid: String,
        @Field("nickname") nickname: String,
        @Field("avatar") avatar: String,
        @Field("gender") gender: String,
        @Field("phone") phone: String,
        @Field("verification_code") verification_code: String
    ): Observable<LoginBean>


    @FormUrlEncoded
    @POST("UserOperation/modifyPassword?")
    fun modifyPassword(@Field("password") password: String): Observable<BaseBean>


    /**
     * *******************用户*******************************************************************
     */

    @FormUrlEncoded
    @POST("Fans/doFollow?")
    fun doFollow(@Field("user_id") user_id: String): Observable<FollowUserBean>

    @FormUrlEncoded
    @POST("Fans/cancelFans?")
    fun cancelFans(@Field("user_id") user_id: String): Observable<FollowUserBean>

    /**
     * 举报文章
     */
    @FormUrlEncoded
    @POST("Content/Report?")
    fun report(
        @Field("content_id") content_id: String, @Field("report_reason") report_reason: Int,
        @Field("supplement") supplement: String, @Field("images_url_1") images_url_1: String,
        @Field("images_url_2") images_url_2: String
    ): Observable<BaseBean>

    /**
     * 举报评论
     */
    @FormUrlEncoded
    @POST("Content/Report?")
    fun report2(@Field("comment_id") comment_id:String, @Field("report_reason") report_reason: Int,
        @Field("supplement") supplement: String, @Field("images_url_1") images_url_1: String,
        @Field("images_url_2") images_url_2: String): Observable<BaseBean>
    /**
     * 举报用户
     */
    @FormUrlEncoded
    @POST("Content/Report?")
    fun reportUser(
        @Field("user_id") user_id: String, @Field("report_reason") report_reason: Int,
        @Field("supplement") supplement: String, @Field("images_url_1") images_url_1: String,
        @Field("images_url_2") images_url_2: String): Observable<BaseBean>
    /**
     * 举报评价
     */
    @FormUrlEncoded
    @POST("Content/Report?")
    fun report5(@Field("assess_id") assess_id:String, @Field("report_reason") report_reason: Int,
                @Field("supplement") supplement: String, @Field("images_url_1") images_url_1: String,
                @Field("images_url_2") images_url_2: String): Observable<BaseBean>
    /**
     * 举报帖子
     */
    @FormUrlEncoded
    @POST("Content/Report?")
    fun reportPost(@Field("post_id") post_id:String, @Field("report_reason") report_reason: Int,
                @Field("supplement") supplement: String, @Field("images_url_1") images_url_1: String,
                @Field("images_url_2") images_url_2: String): Observable<BaseBean>


    /**
     * 意见反馈
     */
    @FormUrlEncoded
    @POST("User/feedback?")
    fun feedback(
        @Field("feedback_type") feedback_type: Int, @Field("problem_desc") problem_desc: String
    ): Observable<BaseBean>

    /**
     * *******************用户中心*******************************************************************
     */

    @GET("User/getUserCenter?")
    fun getUserCenter(): Observable<UserCenterBean>

    @GET("User/getAccountSecurityData?")
    fun getAccountSecurityData(): Observable<MyAccountInfoBean>

    @FormUrlEncoded
    @POST("User/viewHistory?")
    fun viewHistory(@Field("page") page: Int): Observable<MyContentListBean>

    @FormUrlEncoded
    @POST("User/getUserGameAmwayViewHistory?")
    fun getUserGameAmwayViewHistory(@Field("page") page: Int): Observable<UserGameAmwayList>

    @FormUrlEncoded
    @POST("User/delViewHistory?")
    fun delViewHistory(@Field("id") id: Int): Observable<BaseBean>

    @FormUrlEncoded
    @POST("User/clearViewHistory?")
    fun clearViewHistory(@Field("clear_type") clear_type: Int): Observable<BaseBean>


    @FormUrlEncoded
    @POST("User/collectList?")
    fun collectList(@Field("page") page: Int): Observable<MyContentListBean>


    @FormUrlEncoded
    @POST("User/getFocusUserWorks?")
    fun getFocusUserWorks(@Field("page") page: Int): Observable<MyContentListBean>


    @GET("User/shareApp?")
    fun shareApp(): Observable<ShareAppBean>


    @FormUrlEncoded
    @POST("Fans/indexFollow?")
    fun indexFollow(@Field("page") page: Int, @Field("nickname") nickname: String): Observable<FriendListBean>

    @FormUrlEncoded
    @POST("Fans/indexFollow?")
    fun indexFollow(@Field("user_id") user_id: String, @Field("page") page: Int, @Field("nickname") keyWords: String): Observable<FriendListBean>

    @FormUrlEncoded
    @POST("Fans/indexFans?")
    fun indexFans(@Field("page") page: Int, @Field("nickname") nickname: String): Observable<FriendListBean>

    @FormUrlEncoded
    @POST("Fans/indexFans?")
    fun indexFans(@Field("user_id") user_id: String, @Field("page") page: Int, @Field("nickname") nickname: String): Observable<FriendListBean>

    /**
     * *******************用户主页*******************************************************************
     */

    @FormUrlEncoded
    @POST("UserHomepage/index?")
    fun getHomepageInfo(@Field("user_id") user_id: String): Observable<UserCenterBean>


    @FormUrlEncoded
    @POST("UserHomepage/changeBackgroundImg?")
    fun changeBackgroundImg(@Field("background_img") background_img: String): Observable<BaseBean>


    @FormUrlEncoded
    @POST("UserHomepage/indexWorks?")
    fun getHomepageWorks(@Field("user_id") user_id: String, @Field("page") page: Int, @Field("per_page") per_page: Int): Observable<MyContentListBean>

    @FormUrlEncoded
    @POST("UserHomepage/indexWorks?")
    fun indexWorks(@Field("page") page: Int): Observable<MyContentListBean>


    @FormUrlEncoded
    @POST("UserHomepage/indexLike?")
    fun getHomepageLike(@Field("user_id") user_id: String, @Field("page") page: Int, @Field("per_page") per_page: Int): Observable<MyContentListBean>


    /**
     * 隐私设置
     */
    @FormUrlEncoded
    @POST("UserOperation/modifyPrivateConfig?")
    fun modifyPrivateConfig(
        @Field("private_type") private_type: Int, @Field("modify_val") modify_val: Int
    ): Observable<BaseBean>

    @GET("User/getUserPrivateConfig?")
    fun getUserPrivateConfig(): Observable<UserPrivacyBean>

    /**
     * *******************设置******************** Operation ***********************************************
     */
    @GET("User/getUserInfo?")
    fun getUserInfo(): Observable<MyUserInfoBean>

    @FormUrlEncoded
    @POST("UserOperation/resetPassword?")
    fun resetPassword(@Field("password") password: String, @Field("confirm_password") confirm_password: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/setNoviceUserProfile?")
    fun setNoviceUserProfile(
        @Field("avatar") avatar: String?, @Field("nickname") nickname: String?, @Field(
            "gender"
        ) gender: String?
    ): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/modifyAvatar?")
    fun modifyAvatar(@Field("avatar") avatar: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/modifyGender?")
    fun modifyGender(@Field("gender") gender: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/modifyNickname?")
    fun modifyNickname(@Field("nickname") nickname: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/modifyBirthday?")
    fun modifyBirthday(@Field("birthday") avatar: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/modifyCity?")
    fun modifyCity(@Field("district") district: String): Observable<BaseBean>


    @FormUrlEncoded
    @POST("UserOperation/modifyResume?")
    fun modifyResume(@Field("resume") resume: String): Observable<BaseBean>


    @FormUrlEncoded
    @POST("UserOperation/bindSocialPlatform?")
    fun bindSocialPlatform(
        @Field("platform_type") platform_type: String,
        @Field("platform_unionid") platform_unionid: String, @Field("platform_nickname") platform_nickname: String
    ): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/unBindSocialPlatform?")
    fun unBindSocialPlatform(@Field("platform_type") platform_type: Int): Observable<BaseBean>

    /**
     * 黑名单
     */
    @FormUrlEncoded
    @POST("User/getUserBlackList?")
    fun getUserBlackList(@Field("page") page: Int): Observable<UserBlackBean>

    @FormUrlEncoded
    @POST("UserOperation/cancelBlack?")
    fun cancelBlack(@Field("user_id") user_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserOperation/addBlack?")
    fun addBlack(@Field("user_id") user_id: String): Observable<BaseBean>

    /**
     * *******************IM消息*******************************************************************
     */
    @GET("message/getReplyList?")
    fun getReplyList(@Query("page") page: Int): Observable<ReplyListBean>

    @GET("message/getLikeList?")
    fun getLike(@Query("page") page: Int): Observable<ReplyListBean>


    @GET("User/getUserRelation?")
    fun getUserRelation(@Query("netease_id") netease_id: String): Observable<UserKitBean>


    /**
     * *******************视频,文章 内容*******************************************************************
     */

    /**
     * 内容点赞
     */
    @FormUrlEncoded
    @POST("Content/addLike?")
    fun addContentLike(@Field("content_id") content_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Content/cancelLike?")
    fun cancelContentLike(@Field("content_id") content_id: String): Observable<BaseBean>


    /**
     * 内容收藏
     */
    @FormUrlEncoded
    @POST("Content/addCollect?")
    fun addContentCollect(@Field("content_id") content_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Content/cancelCollect")
    fun cancelContentCollect(@Field("content_id") content_id: String): Observable<BaseBean>


    /**
     * 文章内容
     */
    @FormUrlEncoded
    @POST("Content/getContentInfo")
    fun getNewsContentInfo(@Field("content_id") content_id: String): Observable<NewsListBean>


    /**
     * 发布评论内容
     */
    @FormUrlEncoded
    @POST("Comment/addComment")
    fun addNewsComment(@Field("content_id") content_id: String, @Field("content") content: String): Observable<BaseBean>


    /**
     * Comment点赞
     */
    @FormUrlEncoded
    @POST("Comment/addLike?")
    fun addCommentLike(@Field("content_id") content_id: String, @Field("comment_id") comment_id: Int): Observable<BaseBean>

    /**
     * Comment删除
     */
    @FormUrlEncoded
    @POST("Comment/delComment")
    fun delComment(@Field("content_id") content_id: String, @Field("comment_id") comment_id: Int): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Comment/cancelLike?")
    fun cancelCommentLike(@Field("content_id") content_id: String, @Field("comment_id") comment_id: Int): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Message/editReplyRead?")
    fun editReplyRead(@Field("id") id: Int): Observable<BaseBean>

    @FormUrlEncoded
    @POST("Message/updateLikeRead?")
    fun editLikeRead(@Field("id") id: Int,@Field("read_type") read_type: Int): Observable<BaseBean>

    /**
     * =========================视频================================================================
     */


    @FormUrlEncoded
    @POST("Content/getContentInfo?")
    fun getContent(@Field("content_id") content_id: String): Observable<ContentInfoBean>

    @FormUrlEncoded
    @POST("Comment/indexComment?")
    fun indexComment(@Field("content_id") content_id: String,
        @Field("page") page: Int,
        @Field("per_page") per_page: Int,
        //排序类型 0: 原有排序, 1: 最新排序, 2: 最早排序
        @Field("comment_type") comment_type: Int = 0): Observable<CommentListBean>

    @FormUrlEncoded
    @POST("Comment/indexComment")
    fun indexComment(@Field("content_id") content_id: String,
        @Field("comment_id") comment_id: Int,
        @Field("page") page: Int,
        @Field("per_page") per_page: Int,
        @Field("comment_type") comment_type: Int = 0): Observable<CommentListBean>

    @FormUrlEncoded
    @POST("Comment/indexComment?")
    fun indexCommentDetail(
        @Field("content_id") content_id: String,
        @Field("comment_id") comment_id: Int,
        @Field("page") page: Int,
        @Field("per_page") per_page: Int
    ): Observable<CommentDetailListBean>


    @FormUrlEncoded
    @POST("message/indexMsgComment?")
    fun indexMsgComment(@Field("content_id") content_id: String,
                        @Field("comment_id") comment_id: Int,
                        @Field("type") type: Int,
                        @Field("game_id") game_id: String,
                        @Field("page") page: Int,
                        @Field("gt_or_lt") gt_or_lt: Int): Observable<IndexMsgCommentListBean>

    @FormUrlEncoded
    @POST("Comment/addComment?")
    fun addComment(
        @Field("content_id") content_id: String, @Field("comment_id") comment_id: Int, @Field("content") content: String
    ): Observable<AddCommentBean>

    @FormUrlEncoded
    @POST("recommend/getIntroContentList?")
    fun getIntroContentList(@Field("content_id") content_id: String): Observable<RecommendListBean>


//    @POST("Opinion/getCatList?")
    @POST("User/getFeedbackType?")
    fun getCatList(): Observable<CatListBean>

//    @GET("User/getProposalList?")
    @GET("User/getFeedbackList?")
    fun getProposalList(@Query("feedback_type_id") feedback_type_id: Int,@Query("page") page: Int): Observable<FeedbackListBean>


    @FormUrlEncoded
    @POST("User/feedback?")
    fun addProposal(@Field("feedback_type_id") feedback_type_id: Int,@Field("problem_desc") problem_desc: String): Observable<BaseBean>

    @POST("User/cancellationAccount?")
    fun cancellationAccount(): Observable<BaseBean>


    /**
     * =========================道具商城================================================================
     */

    @GET("Shop/indexToolsCate?")
    fun indexGoodsCate(@Query("need_group") need_group: Int): Observable<PropsTabListBean>

    @FormUrlEncoded
    @POST("Shop/indexTools?")
    fun indexGoods(@Field("cate_id") cate_id: String, @Field("page") page: Int): Observable<PropsListBean>

    @FormUrlEncoded
    @POST("Shop/searchGoodsTool?")
    fun searchGoodsTool(@Field("title") title: String, @Field("page") page: Int): Observable<PropsListBean>

    @FormUrlEncoded
    @POST("User/getMyMallprops?")
    fun getMyMallprops(@Field("cate_id") title: String, @Field("page") page: Int): Observable<PropsListBean>

    @FormUrlEncoded
    @POST("User/getMyMallExchangeCode?")
    fun getMyMallExchangeCode(@Field("page") page: Int): Observable<PropsListBean>

    @FormUrlEncoded
    @POST("User/wearProps?")
    fun wearProps(@Field("tool_id") tool_id: String,@Field("cate_id") cate_id: String): Observable<BaseBean>



    @FormUrlEncoded
    @POST("Shop/getPropsInfo?")
    fun getPropsInfo(@Field("tool_id") tool_id: String): Observable<PropsInfoListBean>


    @FormUrlEncoded
    @POST("Shop/buyTool?")
    fun buyTool(@Field("tool_id") tool_id: String, @Field("num") num: Int): Observable<BaseBean>


    @FormUrlEncoded
    @POST("ShopExchange/exchangeTools?")
    fun exchangeTools(@Field("tool_id") tool_id: String, @Field("tool_number") tool_number: Int): Observable<PropsExchangeBean>


    @FormUrlEncoded
    @POST("ShopExchange/getExchangeInfo?")
    fun getExchangeInfo(@Field("id") id: String): Observable<PropsExchangeBean>



    @FormUrlEncoded
    @POST("ShopExchange/submitExchangePreview?")
    fun submitExchangePreview(@Field("exchange_code_json") exchange_code_json: String): Observable<PropsExchangeListBean>

    @FormUrlEncoded
    @POST("ShopExchange/submitExchange?")
    fun submitExchange(@Field("exchange_code_json") exchange_code_json: String): Observable<BaseBean>


    /**
     * =========================首页================================================================
     */


    @GET("game/getUserViewCategoryList?")
    fun getUserViewCategoryList(): Observable<GameCategoryBean>


    @GET("game/getContentList?")
    fun getContentList(): Observable<ContentListBean>

    @GET("recommend/myGame?")
    fun myGame(): Observable<com.tinytiger.common.net.data.home2.GameListBean>

    @GET("recommend/newGame?")
    fun newGame(): Observable<com.tinytiger.common.net.data.home2.GameListBean>


    @GET("recommend/amwayWall?")
    fun amwayWall(): Observable<AmwayWallListBean>

    /**
     * =========================评价================================================================
     */

    @FormUrlEncoded
    @POST("GameAssess/getAssessInfo?")
    fun getAssessInfo(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String): Observable<GameAssessBean>

    @FormUrlEncoded
    @POST("GameAssessComment/indexComment?")
    fun indexAssessComment(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String,@Field("page") page: Int,@Field("type") type: Int): Observable<CommentAssessInfo>

    @FormUrlEncoded
    @POST("GameAssessComment/indexComment?")
    fun indexAssessComment2(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String,@Field("page") page: Int,@Field("comment_id") comment_id: String): Observable<CommentAssessInfo>


    @FormUrlEncoded
    @POST("GameAssess/likeAssessOrTag?")
    fun likeAssessOrTag(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String,@Field("tag_id") tag_id: String): Observable<LikeBean>

    @FormUrlEncoded
    @POST("GameAssessComment/handleLike?")
    fun handleLike(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String,@Field("comment_id") comment_id: String): Observable<LikeBean>

    @FormUrlEncoded
    @POST("GameAssess/collectAssess?")
    fun collectAssess(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String): Observable<LikeBean>

    @FormUrlEncoded
    @POST("GameAssessComment/delComment?")
    fun assessDelComment(@Field("comment_id") comment_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST(" GameAssessComment/addComment?")
    fun assessaddComment(@Field("game_id") game_id: String,@Field("assess_id") assess_id: String,
                   @Field("comment_id") comment_id: String,
                   @Field("content") content: String): Observable<CommentAddBean>

    /**
     * =========================game================================================================
     */
    @FormUrlEncoded
    @POST("game/getGameInfo?")
    fun getGameInfo(@Field("game_id") game_id: String,@Field("user_id") user_id: String): Observable<GameInfoDetailBean>

    @FormUrlEncoded
    @POST("game/getGameInfoCommentList?")
    fun getGameInfoCommentList(@Field("game_id") game_id: String,@Field("user_id") user_id: String,@Field("star_class") star_class: String,@Field("comment_list_status") comment_list_status: Int,@Field("page") page: Int): Observable<GameCommentList>


    @FormUrlEncoded
    @POST("game/getGameWikiList?")
    fun getGameWikiList(@Field("game_id") game_id: String): Observable<GameWikiListBean>

    @FormUrlEncoded
    @POST("game/getGameWikiDetail?")
    fun getGameWikiDetail(@Field("game_id") game_id: String,@Field("wiki_id") wiki_id: String): Observable<GameWikiDetailBean>


    @FormUrlEncoded
    @POST("GameWiki/modularInfo?")
    fun modularInfo(@Field("game_id") game_id: String): Observable<WikiModularList>

    @FormUrlEncoded
    @POST("GameWiki/otherModularInfo?")
    fun otherModularInfo(@Field("submod_id") submod_id: String): Observable<WikiModularOtherList>

    @FormUrlEncoded
    @POST("GameWiki/bannerClick?")
    fun bannerClick(@Field("banner_id") banner_id: String): Observable<BaseBean>


    @FormUrlEncoded
    @POST("GameAssess/likeAssessOrTag?")
    fun likeAssessOrTag(@Field("game_id") game_id: String,@Field("assess_id") wiki_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("GameWiki/collectGameWiki?")
    fun collectGameWiki(@Field("wiki_id") wiki_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("GameWiki/cancelCollectGameWiki?")
    fun cancelCollectGameWiki(@Field("wiki_id") wiki_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("game/getGameDetailContentData?")
    fun getGameDetailContentData(@Field("game_id") game_id: String,@Field("page") page: Int): Observable<GameContentListBean>


    @GET("game/ranking?")
    fun getGameRankingList(@Query("type") type: Int): Observable<GameLibListBean>


    @GET("game/addAppointment?")
    fun addAppointment(@Query("game_id") game_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("game/GameFollow?")
    fun GameFollow(@Field("game_id") game_id: String,@Field("cancel") cancel: String): Observable<GameWikiListBean>



    @FormUrlEncoded
    @POST("ApplyAdmin/addApplyAdmin?")
    fun addApplyAdmin(@Field("game_id") game_id: String,
                      @Field("about_game") about_game: String,
                      @Field("connect_info") connect_info: String,
                      @Field("has_experience") has_experience: String,
                      @Field("play_game_time") play_game_time: String,
                      @Field("week_time") week_time: String,
                      @Field("the_age") the_age: String,
                      @Field("office_hours") office_hours: String,
                      @Field("other_game") other_game: String,
                      @Field("other_game_name") other_game_name: String): Observable<GameWikiListBean>




    /**
     * =========================center================================================================
     */
    @FormUrlEncoded
    @POST("User/getUserFollowGameList?")
    fun getUserFollowGameList(@Field("page") page: Int): Observable<MineGameListBean>


    @FormUrlEncoded
    @POST("User/getUserGameAmwayList?")
    fun getUserGameAmwayList(@Field("page") page: Int): Observable<UserGameAmwayList>

    @FormUrlEncoded
    @POST("User/delUserGameAmwayList?")
    fun delUserGameAmwayList(@Field("ids") ids: String): Observable<BaseBean>


    @FormUrlEncoded
    @POST("User/getUserWikiCollectList?")
    fun getUserWikiCollectList(@Field("page") page: Int): Observable<UserCollectWikiList>

    @FormUrlEncoded
    @POST("User/getUserGameAmwayCollectList?")
    fun getUserGameAmwayCollectList(@Field("page") page: Int): Observable<UserGameAmwayList>

    @GET("User/batchCancelCollectGameAssess?")
    fun batchCancelCollectGameAssess(@Query("ids") ids: String): Observable<BaseBean>
    @FormUrlEncoded
    @POST("UserHomepage/getDynamic?")
    fun getDynamic(@Field("user_id") user_id: String,@Field("page") page: Int): Observable<HomeDynamicList>


    @FormUrlEncoded
    @POST("UserHomepage/getGame?")
    fun getGame(@Field("user_id") user_id: String,@Field("page") page: Int): Observable<MineGameListBean>

    @GET("User/batchCancelCollectNews?")
    fun batchCancelCollectNews(@Query("ids") ids: String): Observable<BaseBean>

    @GET("User/batchCancelCollectGameWiki?")
    fun batchCancelCollectGameWiki(@Query("ids") ids: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("User/getMedalList?")
    fun getMedalList(@Field("page") page: Int,@Field("pagination") pagination: Int): Observable<UserMedalList>

    @FormUrlEncoded
    @POST("User/wearMedal?")
    fun wearMedal(@Field("medal_id") medal_id: String): Observable<BaseBean>

    @FormUrlEncoded
    @POST("UserFavorites/getPostList?")
    fun getPostList(@Field("page") page: Int): Observable<PostInfoBean>

    @FormUrlEncoded
    @POST("UserFavorites/del?")
    fun batchCancelCollectNode(@Field("ids") ids: String): Observable<BaseBean>

    @GET("MyWork/trends?")
    fun getDynamicList(@Query("page") page: Int): Observable<PostInfoBean>

    @GET("MyWork/answer?")
    fun getAnswerList(@Query("page") page: Int): Observable<PostInfoBean>

    @FormUrlEncoded
    @POST("MyWork/comment?")
    fun getMineCommentList(@Field("page") page: Int): Observable<UserGameAmwayList>

}