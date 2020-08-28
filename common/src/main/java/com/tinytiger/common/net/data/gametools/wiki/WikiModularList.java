package com.tinytiger.common.net.data.gametools.wiki;

import com.tinytiger.common.net.data.BaseBean;
import com.tinytiger.common.net.data.home2.NewsBean;

import java.util.List;

public class WikiModularList extends BaseBean {

        public List<WikiModular> data;

        public class WikiModular{
            public int id;
            public int template_id;

            public List<WikiChildBean> child_data;

                    //"id": 3,
                     //"template_id": 3 #模板id  1=横图banner模板 2=图标banner模板 3=上图下文分类模板 4=纯文字标题模板 5=左图右文分类模板 6=申请管理员模板

        }

}
