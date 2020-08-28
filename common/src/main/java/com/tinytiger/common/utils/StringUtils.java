package com.tinytiger.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zhw_luke
 * @date 2019/6/11 0011 下午 5:56
 * @Copyright 小虎互联科技
 * @doc 字符串操作工具包
 * @since 4.3.0
 */
public class StringUtils {


    /**
     * 用户昵称
     *
     * @param name 昵称字段
     * @return 修改后昵称
     */
    public static String stringName(String name) {
        if (name == null) {
            return "";
        }
        String nickname = "该用户已注销";
        if (name != null) {
            nickname = name;
        }
        if (nickname.length() > 8) {
            nickname = nickname.substring(0, 8) + "...";
        }

        return nickname;
    }


    /**
     * 单位换算
     * x》9999，k
     */
    public static String sizeToString(int dis) {
        if (dis < 0) {
            return "0";
        }

        if (dis < 10000) {
            return "" + dis;
        } else {
            double a = dis / 100;
            a = a / 100;
            double s = Math.ceil(a * 10) / 10;
            try {
                return new DecimalFormat("0.0").format(s) + "w";
            } catch (Exception e) {
                return s + "w";
            }
        }
    }


    public static String getString(String s) {
        return s == null ? "" : s;
    }


    /**
     * 是否存在汉字
     * [u4e00-u9fa5]
     *
     * @param str
     * @return
     */
    public static Boolean isGB2312(String str) {
        for (int i = 0; i < str.length(); i++) {
            String bb = str.substring(i, i + 1);
// 生成一个Pattern,同时编译一个正则表达式,其中的u4E00("一"的unicode编码)-\u9FA5("龥"的unicode编码)
            boolean cc = Pattern.matches("[\u4E00-\u9FA5]", bb);
            if (cc == false) {
                return cc;
            }
        }
        return true;
    }


    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 截取富文本中的图片链接
     *
     * @param content
     * @return
     */
    public static String[] getImageUrlsFromHtml(String content) {
        if (content == null) {
            return null;
        }
        List<String> imageSrcList = new ArrayList<String>();
        String htmlCode = content;
        Pattern p = Pattern.compile("<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic|\\b)\\b)[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlCode);
        String quote = null;
        String src = null;
        while (m.find()) {
            quote = m.group(1);
            src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("//s+")[0] : m.group(2);
            imageSrcList.add(src);
        }
        if (imageSrcList.size() == 0) {
            //Log.e("huangxiaoguo", "资讯中未匹配到图片链接");
            //  Log.e("xiaohu","详情中未匹配到图片链接");
            return null;
        }

        return imageSrcList.toArray(new String[imageSrcList.size()]);
    }


    /**
     * 获取Html纯文本
     *
     * @param html
     * @return
     */
    public static String toPlainText(String html) {

        if (html == null) {
            return "";
        }
        //剔出<html>的标签
        String txtcontent = html.replaceAll("</?[^>]+>", "");
        //去除字符串中的空格,回车,换行符,制表符
        // txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
        txtcontent = txtcontent.replaceAll("\\uff0c", ",").replaceAll("&nbsp;", " ");

        return txtcontent;
    }

    /**
     * 获取Html纯文本
     *
     * @param html
     * @return
     */
    public static String toPlainText1(String html) {

        if (html == null) {
            return "";
        }

        //剔出<html>的标签
        String txtcontent = html.replaceAll("<img\\s[^>]+/>", "").replaceAll("</?[^>]+>", "");
        txtcontent = txtcontent.replaceAll("\\uff0c", ",").replaceAll("&nbsp;", " ");
        return txtcontent;
    }


    public static String Html2Text(String inputString) {
        if (inputString == null) {
            return "";
        }
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        //剔除空格行
        textStr = textStr.replaceAll("[ ]+", " ");
        textStr = textStr.replaceAll("&nbsp;", " ");
        textStr = textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return textStr;// 返回文本字符串
    }

    @SuppressLint("DefaultLocale")
    public static String formatMemorySize(String memorySize) {
        if (memorySize == null || memorySize.isEmpty()) {
            return "M";
        }

        float fileSize = 0;
        try {
            fileSize = Float.parseFloat(memorySize);
            if (fileSize <= 0) {
                return "M";
            } else if (fileSize < 1000) {
                return String.format("%dM", (int) fileSize);
            } else {
                return String.format("%.2fG", fileSize / 1000);
            }
        } catch (Exception e) {
            try {
                //是否包含字母，若有则去掉
                String regex = ".*[a-zA-Z]+.*";
                if (Pattern.compile(regex).matcher(memorySize).matches()) {
                    fileSize = Float.parseFloat(memorySize.replaceAll("[a-zA-Z]", ""));
                    if (fileSize <= 0) {
                        return "M";
                    } else if (fileSize < 1000) {
                        return String.format("%dM", (int) fileSize);
                    } else {
                        return String.format("%.2fG", fileSize / 1000);
                    }
                }
            } catch (Exception e2) {
            }
            return "M";
        }
    }

    /**
     * 处理文本，将文本位数限制为maxLen，中文两个字符，英文一个字符
     *
     * @param content 要处理的文本
     * @param maxLen  限制文本字符数，中文两个字符，英文一个字符。例如：a啊b吧，则maxLen为6
     * @return
     */
    public static String limitText(String content, int maxLen) {
        if (TextUtils.isEmpty(content)) {
            return content;
        }
        int count = 0;
        int endIndex = 0;
        for (int i = 0; i < content.length(); i++) {
            char item = content.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if (maxLen == count || (item >= 128 && maxLen + 1 == count)) {
                endIndex = i;
            }
        }
        if (count <= maxLen) {
            return content;
        } else {
            //return content.substring(0, endIndex) + "...";//末尾添加省略号
            return content.substring(0, endIndex + 1);//末尾不添加省略号
        }
    }

}
