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
#支付宝混淆
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}
#支付宝混淆

#微信混淆
-keep class com.tencent.mm.opensdk.** {*;}
-keep class com.tencent.wxop.** {*;}
-keep class com.tencent.mm.sdk.** {*;}
#微信混淆

#混淆ButterKnife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#-dontshrink
#-dontoptimize
-dontpreverify
# 文件名大小写敏感,windows一定要加
-dontusemixedcaseclassnames
-dontwarn android.webkit.WebView

-flattenpackagehierarchy
-allowaccessmodification


-optimizationpasses 7
# verbose混淆后会生成映射文件
-verbose
# 指定混淆后映射关系的文件名
-printmapping map.txt

-keepattributes Exceptions,InnerClasses
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-ignorewarnings

#gson
#如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }

#support_v4
#-dontwarn android.support.v4.** #//对于v4中找不到相应的类和方法,在编译时不警告
#-keep class android.support.v4.** { *; } #//对于v4中的类不进行代码混淆.
#-keep class android.support.v4.view.** { *; } #//对于v4中的类不进行代码混淆.
#-keep interface android.support.v4.app.** { *; } #//对于v4中的接口不进行代码混淆
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment


-dontwarn android.support.v4.**
-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

##recycleview
#-libraryjars ..\\recycleview
-keep class android.support.v7.** { *; }

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保留了继承自Activity、Application这些类的子类，因为这些子类，都有可能被外部调用
#比如说，第一行就保证了所有Activity的子类不要被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#自定义控件不要混淆
-keep public class * extends android.view.View {*;}
-keep public class * extends android.view.ViewGroup {*;}

#数据适配器adapter不要混淆
-keep public class * extends android.widget.BaseAdapter {*;}

# 保持枚举 enum 类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#图片加载Gilde混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

# retrofit
-dontnote retrofit2.Platform
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

#rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn okio.**
-keep class okio.**{*;}

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses


# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}


#Agentweb
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**
#Agentweb

#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class [com.huatu.android].R$*{
public static final int *;
}
#友盟

#透明状态栏
 -keep class com.gyf.barlibrary.* {*;}
 #透明状态栏

 #保持自定义实体类不被混淆
 -keep class com.huatu.android.bean.** {*;}
 -keep public class * implements com.huatu.android.base.mvp.BaseView
 -keep public class * implements com.huatu.android.base.mvp.BaseModel
 -keep public class * extends com.huatu.android.base.mvp.BasePresenter
  #保持自定义实体类不被混淆

  #webview交互不混淆
  -keepclassmembernames class com.huatu.android.ui.activity.webview.WebViewFragment {*;}
   #webview交互不混淆



   #摩羯座混淆
   -keepattributes *Annotation*
   -keepclassmembers class ** {
       @org.greenrobot.eventbus.Subscribe <methods>;
   }
   -keep enum org.greenrobot.eventbus.ThreadMode { *; }
   #
   ## Only required if you use AsyncExecutor
   -keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
       <init>(java.lang.Throwable);
   }

   -keep @com.proguard.annotation.NotProguard class * {*;}
   -keep class * {
       @com.proguard.annotation <fields>;
       @android.webkit.JavascriptInterface <fields>;
   }
   -keepclassmembers class * {
       @com.proguard.annotation <fields>;
       @android.webkit.JavascriptInterface <fields>;
   }

   -keepclassmembers class **.R$* {
       public static <fields>;
   }

   #@proguard_debug_start
   # ------------------ Keep LineNumbers and properties ---------------- #
   -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
   #@proguard_debug_end

   -optimizationpasses 5
   -dontusemixedcaseclassnames
   -dontskipnonpubliclibraryclasses
   -dontpreverify
   -verbose
   -ignorewarnings
   -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
   -dontwarn dalvik.**

   # --------------------------------------------------------------------------
   # Addidional for x5.sdk classes for apps
   -keep class com.tencent.smtt.export.external.**{
       *;
   }

   -keep class com.tencent.tbs.video.interfaces.IUserStateChangedListener {
   	*;
   }

   -keep class com.tencent.smtt.sdk.CacheManager {
   	public *;
   }

   -keep class com.tencent.smtt.sdk.CookieManager {
   	public *;
   }

   -keep class com.tencent.smtt.sdk.WebHistoryItem {
   	public *;
   }

   -keep class com.tencent.smtt.sdk.WebViewDatabase {
   	public *;
   }

   -keep class com.tencent.smtt.sdk.WebBackForwardList {
   	public *;
   }

   -keep public class com.tencent.smtt.sdk.WebView {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebView$HitTestResult {
   	public static final <fields>;
   	public java.lang.String getExtra();
   	public int getType();
   }

   -keep public class com.tencent.smtt.sdk.WebView$WebViewTransport {
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebView$PictureListener {
   	public <fields>;
   	public <methods>;
   }


   -keepattributes InnerClasses

   -keep public enum com.tencent.smtt.sdk.WebSettings$** {
       *;
   }

   -keep public enum com.tencent.smtt.sdk.QbSdk$** {
       *;
   }

   -keep public class com.tencent.smtt.sdk.WebSettings {
       public *;
   }


   -keepattributes Signature
   -keep public class com.tencent.smtt.sdk.ValueCallback {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebViewClient {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.DownloadListener {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebChromeClient {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebChromeClient$FileChooserParams {
   	public <fields>;
   	public <methods>;
   }

   -keep class com.tencent.smtt.sdk.SystemWebChromeClient{
   	public *;
   }
   # 1. extension interfaces should be apparent
   -keep public class com.tencent.smtt.export.external.extension.interfaces.* {
   	public protected *;
   }

   # 2. interfaces should be apparent
   -keep public class com.tencent.smtt.export.external.interfaces.* {
   	public protected *;
   }

   -keep public class com.tencent.smtt.sdk.WebViewCallbackClient {
   	public protected *;
   }

   -keep public class com.tencent.smtt.sdk.WebStorage$QuotaUpdater {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebIconDatabase {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.WebStorage {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.DownloadListener {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.QbSdk {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.QbSdk$PreInitCallback {
   	public <fields>;
   	public <methods>;
   }
   -keep public class com.tencent.smtt.sdk.CookieSyncManager {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.Tbs* {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.utils.LogFileUtils {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.utils.TbsLog {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.utils.TbsLogClient {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.CookieSyncManager {
   	public <fields>;
   	public <methods>;
   }

   # Added for game demos
   -keep public class com.tencent.smtt.sdk.TBSGamePlayer {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.TBSGamePlayerClient* {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.TBSGamePlayerClientExtension {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.TBSGamePlayerService* {
   	public <fields>;
   	public <methods>;
   }

   -keep public class com.tencent.smtt.utils.Apn {
   	public <fields>;
   	public <methods>;
   }
   # end


   -keep public class com.tencent.smtt.export.external.extension.proxy.ProxyWebViewClientExtension {
   	public <fields>;
   	public <methods>;
   }

   -keep class MTT.ThirdAppInfoNew {
   	*;
   }

   -keep class com.tencent.mtt.MttTraceEvent {
   	*;
   }

   # Game related
   -keep public class com.tencent.smtt.gamesdk.* {
   	public protected *;
   }

   -keep public class com.tencent.smtt.sdk.TBSGameBooter {
           public <fields>;
           public <methods>;
   }

   -keep public class com.tencent.smtt.sdk.TBSGameBaseActivity {
   	public protected *;
   }

   -keep public class com.tencent.smtt.sdk.TBSGameBaseActivityProxy {
   	public protected *;
   }

   -keep public class com.tencent.smtt.gamesdk.internal.TBSGameServiceClient {
   	public *;
   }

   #------------------  下方是android平台自带的排除项，这里不要动         ----------------
   -keep public class * extends android.app.Activity
   -keep public class * extends android.app.Application
   -keep public class * extends android.app.Service
   -keep public class * extends android.content.BroadcastReceiver
   -keep public class * extends android.content.ContentProvider
   -keep public class com.android.vending.licensing.ILicensingService
   -keep public class * extends android.app.backup.BackupAgentHelper
   -keep public class * extends android.preference.Preference

   -keepclasseswithmembers class * { native <methods>; }
   -keepclasseswithmembers class * { public <init>(android.content.Context); }
   -keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet); }
   -keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet, int); }

   -keepclasseswithmembers class * extends android.view.View
   -keepclasseswithmembers class * extends android.view.ViewGroup

   -keepattributes *Annotation*
   -keepattributes Signature
   -keepattributes SourceFile,LineNumberTable
   -keepattributes InnerClasses
   -keepattributes EnclosingMethod

   -keepclasseswithmembers class * {
   	public <init>(android.content.Context, android.util.AttributeSet);
   }

   -keepclasseswithmembers class * {
   	public <init>(android.content.Context, android.util.AttributeSet, int);
   }

   -keepclasseswithmembernames class *{
   	native <methods>;
   }

   -keepclassmembers enum * {
       public static **[] values();
       public static ** valueOf(java.lang.String);
   }

   -keepclassmembers class * extends android.app.Activity {
       public void * (android.view.View);
   }

   -keep class * implements android.os.Parcelable {
     public static final android.os.Parcelable$Creator *;
   }


   #------------------  下方是共性的排除项目         ----------------
   # 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
   # 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

   -keepclasseswithmembers class * {
       ... *JNI*(...);
   }

   -keepclasseswithmembernames class * {
   	... *JRI*(...);
   }

   -keep class **JNI* {*;}

   #摩羯座混淆完成=====

   #百度OCR混淆
   -keep class com.baidu.ocr.sdk.**{*;}
   -dontwarn com.baidu.ocr.**
    #百度OCR混淆

   #百度活体检测混淆
   -keep class com.baidu.aip.face.stat.**{*;}
   -dontwarn com.baidu.aip.face.**
    -keep class com.baidu.idl.facesdk.**{*;}
    -keep class com.baidu.idl.authority.**{*;}
    -keep class com.baidu.idl.face.license.**{*;}
    -dontwarn com.baidu.idl.**
    #百度活体检测混淆


# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

 #PictureSelector 2.0
 -keep class com.luck.picture.lib.** { *; }

 -dontwarn com.yalantis.ucrop**
 -keep class com.yalantis.ucrop** { *; }
 -keep interface com.yalantis.ucrop** { *; }

 #rxandroid
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 #BaseRecyclerViewAdapterHelper
  -keep class com.chad.library.adapter.** {
  *;}
  -keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
  -keep public class * extends com.chad.library.adapter.base.BaseViewHolder
  -keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
       <init>(...);
  }
  #BaseRecyclerViewAdapterHelper

#友盟推送混淆
-dontwarn com.umeng.**
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.meizu.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.meizu.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}
#友盟推送混淆
