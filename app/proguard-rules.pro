# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Stripe
-keep class com.stripe.** { *; }
-keep class com.stripe.android.** { *; }

# Keep Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Keep Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Keep Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep data classes
-keepclassmembers class com.vito.app.data.** { *; }
-keepclassmembers class com.vito.app.domain.** { *; }
