# Network Speed Overlay - ProGuard Rules

# Keep service classes
-keep class com.networkspeed.overlay.NetworkSpeedOverlayService { *; }
-keep class com.networkspeed.overlay.NetworkSpeedAccessibilityService { *; }

# Keep positioning helper
-keep class com.networkspeed.overlay.PositioningHelper { *; }

# Keep main activity
-keep class com.networkspeed.overlay.MainActivity { *; }

# Keep R class
-keep class com.networkspeed.overlay.R { *; }
-keep class com.networkspeed.overlay.R$* { *; }

# Keep view binding
-keep class com.networkspeed.overlay.databinding.** { *; }

# Keep notification classes
-keep class * extends android.app.NotificationChannel { *; }
-keep class * extends android.app.Notification { *; }

# Keep system overlay classes
-keep class android.view.WindowManager { *; }
-keep class android.view.WindowManager$LayoutParams { *; }

# Keep network monitoring classes
-keep class android.net.TrafficStats { *; }
-keep class android.net.ConnectivityManager { *; }
-keep class android.net.NetworkCapabilities { *; }

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# Maximum optimization for size and performance
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable
-optimizationpasses 10
-allowaccessmodification
-dontpreverify

# Remove unused code more aggressively
-dontwarn **
-ignorewarnings
-keepattributes *Annotation*

# Remove debug information
-renamesourcefileattribute SourceFile

# Keep line numbers for debugging
-keepattributes SourceFile,LineNumberTable
