plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.searchnewsblog.domain"
    compileSdk = libs.versions.sdk.compile.map { it.toInt() }.get()

    defaultConfig {
        minSdk = libs.versions.sdk.min.map { it.toInt() }.get()
        targetSdk = libs.versions.sdk.target.map { it.toInt() }.get()
        testInstrumentationRunner = "com.example.searchnewsblog.domain.CustomTestRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":data"))
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.9")
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android)
    //coreroutin
    implementation(libs.coroutines.android)
    implementation(libs.timber)

    //gson
    implementation(libs.gson)

    //paging
    implementation(libs.androidx.paging.runtime.ktx)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}