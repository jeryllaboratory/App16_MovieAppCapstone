plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.dynamic.feature) apply false
    // If you're using `ksp` at the module level, you can apply it there.
    // id("com.google.devtools.ksp") version "1.9.0-1.0.11" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Version for Navigation Safe Args
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.0")
        // This should be applied at the module level, so no need to include KSP here.
        // classpath("com.google.devtools.ksp:symbol-processing-gradle-plugin:1.9.0-1.0.11")
    }
}

