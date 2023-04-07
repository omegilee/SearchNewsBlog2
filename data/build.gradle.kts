plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.searchnewsblog.data"
    compileSdk = libs.versions.sdk.compile.map { it.toInt() }.get()

    defaultConfig {
        minSdk = libs.versions.sdk.min.map { it.toInt() }.get()
        targetSdk = libs.versions.sdk.target.map { it.toInt() }.get()
        testInstrumentationRunner = "com.example.searchnewsblog.data.CustomTestRunner"
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
        buildTypes.forEach { buildType ->
            buildType.buildConfigField(
                "String",
                "BASE_URL",
                project.properties["${buildType.name}.url"].toString()
            )
            buildType.buildConfigField(
                "String",
                "API_TOKEN",
                project.properties["${buildType.name}.token"].toString()
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
}

dependencies {
    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    androidTestImplementation(libs.hilt.android)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.0.9")
    androidTestImplementation(libs.hilt.android)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)

    //retrofit
    implementation(libs.retrofit) {
        exclude(module = "okhttp")
    }
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.bundles.okhttp)
    implementation(libs.okhttp.log)

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    //coreroutin
    implementation(libs.coroutines.android)
    implementation(libs.timber)

    //gson
    implementation(libs.gson)

    //test
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

kapt {
    correctErrorTypes = true
}