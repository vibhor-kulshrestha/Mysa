plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "ai.mysmartassistant.mysa"
    compileSdk = 36

    defaultConfig {
        applicationId = "ai.mysmartassistant.mysa"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            isShrinkResources = false

        }
    }
    flavorDimensions += "environment"

    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "Mysa Dev")
            buildConfigField("String", "BASE_URL", "\"https://test-api.mysmartassistant.ai/pa/api/\"")
            buildConfigField("String", "WEB_SOCKET_BASE_URL", "\"ws://3.109.123.79:8080/ws/chat\"")
            buildConfigField("String", "MS_CONFIG_PATH", "\"assets/configs/ms_config_dev.json\"")
            buildConfigField("String", "MS_REDIRECT_URI", "\"msauth://ai.mysmartassistant.mysa.dev/5%2FwGQIjZatneTrKxIproYYMiAjo%3D\"")
            buildConfigField("String", "MS_OAUTH_REDIRECT_URI", "\"mysa://ai.mysmartassistant.mysa/outlook\"")
            buildConfigField("String", "MS_CLIENT_ID", "\"9e189c7c-4774-463d-9897-2208e101e5ed\"")
        }
        create("prod") {
            dimension = "environment"
            resValue("string", "app_name", "Mysa")
            buildConfigField("String", "BASE_URL", "\"https://api.mysmartassistant.ai/pa/api/\"")
            buildConfigField("String", "WEB_SOCKET_BASE_URL", "\"ws://3.109.123.79:8080/ws/chat\"")
            buildConfigField("String", "MS_CONFIG_PATH", "\"assets/configs/ms_config_dev.json\"")
            buildConfigField("String", "MS_REDIRECT_URI", "\"msauth://ai.mysmartassistant.mysa.dev/5%2FwGQIjZatneTrKxIproYYMiAjo%3D\"")
            buildConfigField("String", "MS_OAUTH_REDIRECT_URI", "\"mysa://ai.mysmartassistant.mysa/outlook\"")
            buildConfigField("String", "MS_CLIENT_ID", "\"9e189c7c-4774-463d-9897-2208e101e5ed\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.jks")
            storePassword = "android"
            keyAlias = "mysa"
            keyPassword = "android"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.window.size.class1)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.room.runtime)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    ksp(libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.play.services.auth.api.phone)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.truecaller.sdk)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}