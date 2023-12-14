plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.menutodaytest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.menutodaytest"
        minSdk = 21
        targetSdk = 28
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jsoup:jsoup:1.13.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.code.gson:gson:2.8.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}