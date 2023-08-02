plugins {
    id(Libs.BuildPlugins.androidLibrary)
    kotlin("android")
    kotlin("kapt")
    id(Libs.BuildPlugins.hiltAndroidPlugin)
}

android {
    namespace = "com.smithjilks.mpesaexpensetracker.feature"
    compileSdk = ConfigurationData.compileSdk

    defaultConfig {
        minSdk = ConfigurationData.minSdk
        targetSdk = ConfigurationData.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }

    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(Libs.AndroidX.ktxCore)
    implementation(Libs.AndroidX.lifecycleRuntimeKtx)
    implementation(Libs.AndroidX.Compose.material3)
    implementation(Libs.AndroidX.Compose.material3WindowSize)
    implementation(Libs.AndroidX.Compose.navigation)
    implementation(Libs.AndroidX.Compose.uiGraphics)
    implementation(Libs.AndroidX.Compose.uiToolingPreview)

    debugImplementation(Libs.AndroidX.Compose.uiTooling)

    implementation(Libs.AndroidX.roomKtx)
    annotationProcessor(Libs.AndroidX.roomCompiler)
    implementation(Libs.AndroidX.roomRuntime)

    implementation(Libs.hilt)
    kapt(Libs.hiltCompiler)
    kapt(Libs.hiltAndroidCompiler)
    implementation(Libs.AndroidX.Compose.hiltNavigation)

    implementation(Libs.Accompanist.systemUiController)
    implementation(Libs.Accompanist.permissions)



    implementation(project(":core"))


    testImplementation(Libs.TestLibraries.junit4)
    androidTestImplementation(Libs.TestLibraries.junitExt)
    androidTestImplementation(Libs.TestLibraries.espressoCore)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}