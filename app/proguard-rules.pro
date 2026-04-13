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

-keep class **.R$* {
 *;
}
-keepclassmembers class com.paxus.pay.poslinkui.demo.event.** {
}

-assumenosideeffects class android.util.Log {
public static boolean isLoggable(java.lang.String, int);
public static int v(...);
public static int i(...);
public static int w(...);
public static int d(...);
public static int e(...);
#public static int println(...);
}

# --- Release R8: keep only what must stay stable for manifest / host / reflection ---
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault,*Annotation*,Signature,InnerClasses,EnclosingMethod

-keep class com.paxus.pay.poslinkui.demo.DemoApplication { *; }
-keep class com.paxus.pay.poslinkui.demo.MainActivity { *; }
-keep class com.paxus.pay.poslinkui.demo.entry.EntryActivity { *; }

# TransactionPresentation uses Class.forName on these androidx helpers.
-keep class androidx.lifecycle.ViewTreeLifecycleOwner { *; }
-keep class androidx.savedstate.ViewTreeSavedStateRegistryOwner { *; }
-keep class androidx.lifecycle.ViewTreeViewModelStoreOwner { *; }

-keep class kotlin.Metadata { *; }
-dontwarn kotlinx.coroutines.**
-keep class com.google.zxing.** { *; }
-keep class com.google.zxing.common.** { *; }