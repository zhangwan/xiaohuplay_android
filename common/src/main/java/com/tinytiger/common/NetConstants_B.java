package com.tinytiger.common;


/**
 * @author luke
 * @date 2018/6/30 10:34
 * @doc base接口信息
 */
public class NetConstants_B {

    /**
     * 1正式
     * 2预发布
     * 3测试外网
     * 其他 测试
     */
    private static int appType = 2;


    /**
     * s是否打印log开关
     * false 关闭
     * true 开启
     */
    public static boolean LOG_TYPE = true;

    public static String getHttp() {
        String url = "";
        switch (appType) {
            case 1:
                url = "https://app.tinytiger.cn/xhdj/tinytiger_app/public/index.php/";
                break;
            case 2:
                url = "http://tinytiger_app.titipa.cn/xhdj/tinytiger_app/public/index.php/";
                break;
            case 3:
                url = "http://aladdinapi.titipa.cn/tinytiger_app/public/index.php/";
                break;
            default:
                url = "http://192.168.1.241/tinytiger_app/public/index.php/";
        }
        return url;
    }
}