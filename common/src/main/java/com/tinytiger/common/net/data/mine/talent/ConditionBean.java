package com.tinytiger.common.net.data.mine.talent;


import java.io.Serializable;

public class ConditionBean implements Serializable {


    public int works;
    public int get_like;
    public int follow_num;
    public int cond_day;

    public String key;



       /*  "works": 1,  #作品数 （大于等于30篇属于符合条件） 符合条件该值 = 1 不满足 = 0
                "get_like": 1, #获赞数量 （大于等于100个属于符合条件） 符合条件该值 = 1 不满足 = 0
                "follow_num": 1, #关注我的用户数 （大于等于50个属于符合条件） 符合条件该值 = 1 不满足 = 0
                "cond_day": 1,  #连续登录的天数 （大于等于7天属于符合条件） 符合条件该值= 1 不满足 = 0
                "key": "6c508273cb600e79cbcf447ec9a159ba"*/

/*"examine_info": {
            "id": 16,           # 申请达人的id
            "status": 0,        # 申请达人状态  0 审核中 1 已通过  2拒绝
            "apply_type": 2     # 1 站内达人 2 站外达人
        },
                "condition": []*/
/*
        "apple_status": 3,  #0 审核中 1 已通过  2拒绝 3可以发起申请
        "apply_button": 0,  #1 代表可以提交申请  0代表不满足条件，无法申请
        "works": 0,         #作品数 大于等于30条属于符合条件 符合条件该值 = 1 不满足 = 0
                "get_like": 0,      #获赞数量 大于等于100条属于符合条件 符合条件该值 = 1 不满足 = 0
                "follow_num": 0,    #关注我的用户数 大于等于50条属于符合条件 符合条件该值 = 1 不满足 = 0
                "key": ""           ##用于 /User/submitMasterApply 接口的参数 防止非法请求*/
}
