// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.ksp) apply false
    id("com.android.library") version "8.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

buildscript {
    dependencies {
//        classpath(libs.hilt.android.gradle.plugin)
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.24")
    }
}