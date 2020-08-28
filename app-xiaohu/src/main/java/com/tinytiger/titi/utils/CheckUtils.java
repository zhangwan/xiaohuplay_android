package com.tinytiger.titi.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.LocalAntiSpamResult;

import java.util.List;


/**
 * @author zhw_luke
 * @date 2019/10/30 0030 下午 2:11
 * @Copyright 小虎互联科技
 * @doc 数据检测
 * @since 5.0.0
 */
public class CheckUtils {


    /**
     * 真为不含有表情
     *
     * @param str String
     * @return
     */
    public static boolean noContainsEmoji(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }


    private static boolean isEmojiCharacter(char codePoint) {
        return ((codePoint == 0x0) || (codePoint == 0x9) ||
                (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }


    /**
     * 判断是否有敏感词汇
     *
     * @param txt 词汇
     * @return
     */
    public static boolean checkLocalAntiSpam(String txt) {
        IMMessage message = MessageBuilder.createTextMessage("titixhdjd4642a41401216369e2a6b6", SessionTypeEnum.P2P, txt);
        LocalAntiSpamResult result = NIMClient.getService(MsgService.class).checkLocalAntiSpam(message.getContent(), "**");
        int operator = result == null ? 0 : result.getOperator();
        return operator != 0;
    }


    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static PackageInfo getInstalledPackage(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo.size() > 0) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return pinfo.get(i);
                }
            }
        }
        return null;
    }
}
