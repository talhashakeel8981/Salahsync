plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.gms.google-services")


}


android {
    namespace = "com.example.salahsync"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.salahsync"
        minSdk = 25 // Changed from 24 to 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Firebase / Google Services plugin
        classpath("com.google.gms:google-services:4.4.2")
    }
}

dependencies {
    implementation(libs.androidx.contentpager)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.core.i18n)
    implementation(libs.androidx.foundation.android)
    implementation(libs.firebase.database)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.material.icons.core.android)
    implementation(libs.androidx.material.icons.core.android)
    val room_version = "2.7.2"


    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Optional - Kotlin Extensions and Coroutines support
    implementation("androidx.room:room-ktx:$room_version")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.benchmark.traceprocessor.android)
    implementation(libs.androidx.navigation.compose.android)
    implementation(libs.androidx.foundation.layout.android)
    // Firebase BOM (Bill of Materials) - version handle karega
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

//for splash screen
    implementation ("androidx.core:core-splashscreen:1.0.0")

// Firestore
    implementation("com.google.firebase:firebase-firestore-ktx")

//Firebase Authentication

    implementation ("com.google.firebase:firebase-auth")


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}