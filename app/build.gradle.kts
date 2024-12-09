plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.absencesessect"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.absencesessect"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)                  // From libs.versions.toml
    implementation(libs.material)                  // From libs.versions.toml
    implementation(libs.activity)                  // From libs.versions.toml
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)          // From libs.versions.toml

    testImplementation(libs.junit)                 // From libs.versions.toml
    androidTestImplementation(libs.ext.junit)      // From libs.versions.toml
    androidTestImplementation(libs.espresso.core)  // From libs.versions.toml

}
