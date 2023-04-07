buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(libs.gradle)
        classpath(libs.kotlin)
        classpath(libs.androidx.navigation.safe.args)
        classpath(libs.hilt.android.gradle.plugin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}