plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.searchnewsblog"
    compileSdk = libs.versions.sdk.compile.map { it.toInt() }.get()

    defaultConfig {
        applicationId = "com.example.searchnewsblog"
        minSdk = libs.versions.sdk.min.map { it.toInt() }.get()
        targetSdk = libs.versions.sdk.target.map { it.toInt() }.get()
        versionCode = libs.versions.version.code.map { it.toInt() }.get()
        versionName = libs.versions.version.name.get()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
//        coreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packagingOptions {
        resources.excludes += "META-INF/LICENSE*"
        resources.excludes += "META-INF/*.kotlin_module"
    }
}

kapt {
    correctErrorTypes = true
}

hilt {
    enableExperimentalClasspathAggregation = true
}

dependencies {
    implementation(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    // lifecycle
    implementation(libs.bundles.lifecycle)

    //navgation
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)

    //compose
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.constraintlayout.compose)

    //glide
    implementation(libs.glide.compose)

    //hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.androidx.hilt.work)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation(libs.timber)
    implementation(libs.gson)

    //accompanist
//    implementation(libs.accompanist.webview){}

//    //okhttp
//    implementation(libs.retrofit) {
//        exclude(module = "okhttp")
//    }

    //paging
    //implementation(libs.androidx.paging.runtime.ktx)
    implementation("androidx.paging:paging-compose:1.0.0-alpha17")

//    //room
//    implementation(libs.androidx.room.runtime)
//    kapt(libs.androidx.room.compiler)

    //test
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}