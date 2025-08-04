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

# GSON Proguard
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.okre.oreummark.model.OreumData
-keep class com.okre.oreummark.model.ResultSummary
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

#OkHttp Proguard
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-dontwarn okio.**
-dontwarn okhttp3.**

# retrofit2
# closingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions**

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# JSON Model
# -keep class com.bookreview.ewp.json_model.** { *; }

-dontwarn android.support.**
# androidX proguard rule
-dontwarn com.google.android.material.**
-keep class com.google.android.material.** { *; }

-keep public class * implements androidx.versionedparcelable.VersionedParcelable
-dontwarn androidx.**
-keep class androidx.** { *; }
-keep interface androidx.** { *; }

# firebase proguard https://firebase.google.com/docs/database/android/start
# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models.
# Modify this rule to fit the structure of your app.
## keepclassmembers class com.yourcompany.models.**
-keepclassmembers class com.okre.oreummark.** {
  *;
}
-keep public class com.google.firebase.analytics.FirebaseAnalytics {
    public *;
}

# kakaoamp proguard
-keep class net.daum.**
