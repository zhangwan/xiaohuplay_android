package com.tinytiger.common.net.data.props;

/**
 * 道具詳情
 */
public class PropsInfoBean {

    public String id;
  //  public String tool_id;

    public String name;
    public String cate_id;
    public String image;
    public double price;


    public int status;
    public int num;//#购买后的件数



    /**
     * 道具詳情
     */
    public int stock;//#库存
    public int is_has;//#是否购买 1:是 0:否
    public int is_wear;//#购买后是否佩戴  1:是 0:否
    public int shop_privilege = 0;//#是否核心用户 1=核心用户 0=普通用户
    public String cate_name;//#道具分类
    public int p_coin;//#账户余额


    public String shop_tool_share_url;

}
