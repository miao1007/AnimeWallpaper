# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/leon/Documents/sdk/tools/proguard/proguard-android.txt
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

#see https://github.com/krschultz/android-proguard-snippets/tree/master/libraries


#picasso okhttp3 okhttp2 okio proguard
-dontwarn com.squareup.okhttp.**
-dontnote com.squareup.okhttp.internal.Platform
-dontwarn okhttp3.**
-dontnote okhttp3.internal.Platform
-dontwarn okio.**

# gson
# https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg
# just keep your dto with "Expose" and "SerializedName"

#see http://square.github.io/retrofit/
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#butterknife has it own aar
#see http://jakewharton.github.io/butterknife/

#rxjava/android/bind will use https://github.com/artem-zinnatullin/RxJavaProGuardRules


##photoview
-dontwarn uk.co.senab.photoview.**