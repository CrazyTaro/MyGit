# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/xuhaolin/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

##指定代码的压缩级别
#-optimizationpasses 5
##包明不混合大小写
#-dontusemixedcaseclassnames
##不去忽略非公共的库类
#-dontskipnonpubliclibraryclasses
# #优化  不优化输入的类文件
#-dontoptimize
# #预校验
#-dontpreverify
# #混淆时是否记录日志
#-verbose
# # 混淆时所采用的算法
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*



#保护注解
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.view.View


#不混淆某个包下的类
-keep class us.bestapp.henrytaro.draw.interfaces.** {*;}

-keep class us.bestapp.henrytaro.draw.params.**{
    public <fields>;
    public <methods>;
}

-keepclasseswithmembers class us.bestapp.henrytaro.view.ISeatChooseEvent{
    public <fields>;
    public <methods>;
}

-keepclasseswithmembers class us.bestapp.henrytaro.view.ISeatViewInterface{
    public <fields>;
    public <methods>;
}

-keep class us.bestapp.henrytaro.view.ISeatViewInterface$*{
#    public <fields>;
#    public <methods>;
*;
}

-keepclasseswithmembers class us.bestapp.henrytaro.draw.params.ExportParams{
    public <fields>;
    public <methods>;
}

-keepclasseswithmembers class us.bestapp.henrytaro.draw.utils.AbsTouchEventHandle{
    public <fields>;
    public <methods>;
}

-keep class us.bestapp.henrytaro.draw.utils.AbsTouchEventHandle$*{
#    public <fields>;
#    public <methods>;
*;
}

-keep class us.bestapp.henrytaro.draw.utils.SeatDrawUtils{
    public <fields>;
    public <methods>;
    protected <methods>;
}


#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

