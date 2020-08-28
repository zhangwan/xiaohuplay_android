# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#----------------------------json
-keep class com.google.gson.stream.** { *; }

 #使用GSON、fastjson等框架时，所写的JSON对象类不混淆，否则无法将JSON解析成对应的对象
-keepclassmembers class * {
     public <init>(org.json.JSONObject);
}
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn org.json.**
-keep class org.json.**{*;}
#json数据不能被混淆
-keep class com.tinytiger.common.net.data.**{*;}

-keep class com.tinytiger.common.http.response.**{*;}




-keep class com.tinytiger.common.view.**{*;}
#权限类
-dontwarn com.yanzhenjie.permission.**

#BaseQuickAdapter混淆
-keep class com.chad.library.adapter.** {*;}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}
#alibaba
-dontwarn com.alibaba.**
-keep class com.alibaba.fastjson.** {*;}

#微信
-keep class com.tencent.** { *; }
-keep class com.switfpass.pay.**{*;}
-keep  class com.google.**{*;}

#七牛云储存
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

-keepclassmembers class ** {
    private javax.net.ssl.SSLSocketFactory delegate;
}

#2D地图
-dontwarn com.amap.**
-keep class com.amap.**{*;}

 #定位
-keep class com.autonavi.aps.amapapi.model.**{*;}

#3D 地图 V5.0.0之后：
-keep   class com.autonavi.**{*;}

#云信==============================================
-dontwarn com.netease.**
-keep class com.netease.** {*;}
-dontshrink
-dontoptimize

#Https混淆
-keepclassmembers class ** {
    private javax.net.ssl.SSLSocketFactory delegate;
}
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn org.xmlpull.v1.**
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.**

#banner
-keep class com.ms.banner.** {*;}

#数据
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**

-keep class com.bun.miitmdid.core.** {*;}

#友盟
-keep class com.umeng.** {*;}
-dontwarn com.umeng.**
-keep class com.uc.** {*;}
-keep public class com.tinytiger.titi.R$*{
    public static final int *;
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}



#图片选中
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}

#云游戏
-dontwarn org.codehaus.**
-keep class org.codehaus.** {*;}
-keep interface com.haima.hmcp.listeners.*{*;}
-keep class com.haima.hmcp.beans.*{*;}
-keep enum com.haima.hmcp.enums.*{*;}
-keep class com.haima.hmcp.**{*;}
-keep enum com.haima.hmcp.websocket.WebSocketCloseNotification{*;}
-keep interface com.haima.hmcp.websocket.WebSocket{*;}
-keep interface com.haima.hmcp.websocket.WebSocketConnectionObserver{*;}
-keep class com.haima.hmcp.websocket.WebSocketConnection{public <methods>;}
-keep class com.haima.hmcp.websocket.WebSocketOptions{public <methods>;}
-keep class com.haima.hmcp.websocket.WebSocketException{*;}
-keep interface com.hmcp.saas.sdk.listeners.*{*;}
-keep class com.hmcp.saas.sdk.beans.*{*;}
-keep class com.hmcp.saas.sdk.enums.*{*;}
-keep class com.hmcp.saas.sdk.SaasSDK{public <methods>;}
-keep class de.tavendo.autobahn.**{*;}
-keep class tv.haima.ijk.media.player.** { *; }
-keep interface tv.haima.ijk.media.player.listeners.*{*;}
-keep interface tv.haima.ijk.media.player.IMediaPlayer{*;}
-keep class com.netease.LDNetDiagnoService.LDNetDiagnoService{public <methods>;}
-keep interface com.netease.LDNetDiagnoService.LDNetDiagnoListener{public<methods>;}
-keep class com.netease.LDNetDiagnoService.LDNetTraceRoute { *; }
-dontwarn org.openudid.**
-keep class org.openudid.**{*;}