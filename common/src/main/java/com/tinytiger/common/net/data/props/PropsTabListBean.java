package com.tinytiger.common.net.data.props;


import com.tinytiger.common.net.data.BaseBean;

import java.util.ArrayList;


/**
 *
 * @Author luke
 * @Date 2020-02-04 13:06
 * @Copyright 小虎互联科技
 * @since 1.1.0
 * @Des 道具商城 分类
 *
 */
public class PropsTabListBean extends BaseBean {
    public ArrayList<PropsTab>  data;

    public class PropsTab{
        public String id;
        public String name;
//"id": 1,		#类型id
//            "name": "挂件"	#类型名称

    }


}
