object Versions {
    const val ktlint = "0.45.2"
}

object Libs {

    object BuildPlugins {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:2.45"
        const val hiltAndroidPlugin = "com.google.dagger.hilt.android"
        const val androidApplication = "com.android.application"
        const val androidLibrary = "com.android.library"
        const val jetbrainsKotlinAndroid = "org.jetbrains.kotlin.android"
        const val kotlinAndroid = "kotlin-android"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"

    }

    object Kotlin {

    }


    const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"
    const val kotlinxCoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4"
    const val kotlinxCoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4"

    const val hilt = "com.google.dagger:hilt-android:2.44"
    const val hiltCompiler ="com.google.dagger:hilt-compiler:2.44"
    const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:2.44"

    object PlayServices {
        const val authApiPhone = "com.google.android.gms:play-services-auth-api-phone:18.0.1"
    }

    object Accompanist{
        const val systemUiController = "com.google.accompanist:accompanist-systemuicontroller:0.28.0"
        const val permissions =  "com.google.accompanist:accompanist-permissions:0.28.0"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val ktxCore = "androidx.core:core-ktx:1.10.0"
        const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.6.1"
        const val viewmodelLifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"
        const val roomRuntime = "androidx.room:room-runtime:2.5.1"
        const val roomKtx = "androidx.room:room-ktx:2.5.1"
        const val roomCompiler = "androidx.room:room-compiler:2.5.1"

        object Compose {
            const val bom = "androidx.compose:compose-bom:2022.10.00"
            const val activity = "androidx.activity:activity-compose:1.7.1"
            const val ui = "androidx.compose.ui:ui"
            const val uiGraphics = "androidx.compose.ui:ui-graphics"
            const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:1.4.2"
            const val uiTooling = "androidx.compose.ui:ui-tooling:1.4.3"
            const val material3 = "androidx.compose.material3:material3:1.1.0"
            const val material3WindowSize = "androidx.compose.material3:material3-window-size-class:1.1.0"
            const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"


            const val navigation = "androidx.navigation:navigation-compose:2.5.3"
            const val kotlinxCoroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4"
            const val kotlinxCoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4"

        }
    }

    object TestLibraries {
        const val junit4 = "junit:junit:4.13.2"
        const val junitExt = "androidx.test.ext:junit:1.1.5"
        const val composeBom = "androidx.compose:compose-bom:2022.10.00"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.5.1"
        const val junit4ComposeUI = "androidx.compose.ui:ui-test-junit4"
        const val composeUiTooling = "androidx.compose.ui:ui-tooling"
        const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest"

    }
}