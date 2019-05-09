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
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontskipnonpubliclibraryclassmembers
-dontwarn
-ignorewarnings


-libraryjars <java.home>/lib/rt.jar(java/**,javax/**)


-keep class android.** {*; }
-keep public class * extends android.view
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService




-keepclassmembers class com.goldnet.mobile.activity.InfoDetailActivity {
   public *;
}
-keepclassmembers   class com.goldnet.mobile.activity.InfoDetailActivity$*{
    *;
}

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*




-keep class sun.misc.Unsafe { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**





-keepclassmembers class * implements java.io.Serializable {
    static final long serialVemrsionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}


-dontshrink
-dontwarn javax.xml.stream.events.**
-dontwarn rx.**
-dontwarn org.apache.lang.**
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.appengine.UrlFetchClient
-dontwarn org.springframework.**
-dontwarn org.androidannotations.**
-dontwarn android.support.v4.**



-keepattributes *Annotation*
-keepattributes Signature



-keepclasseswithmembers class * {
@retrofit.http.* <methods>; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keep class **.R$* {
 *;
}


#-------------------------------------------------------------------

-keepattributes Exceptions,InnerClasses,...

-keep class com.xinyan.facecheck.listener.OnXYFaceListener{*;}
-keep class com.xinyan.facecheck.XinYanFaceSDK{*;}
-keep class com.xinyan.facecheck.bean.**{*;}

-keep class com.xinyan.action.**{*;}
-keep class com.xinyan.common.**{*;}
-keep class com.xinyan.agrement.** {*;}
-keep class com.xinyan.facecheck.lib** {*; }