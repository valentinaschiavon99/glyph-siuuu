plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.valentina.siuuu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.valentina.siuuu"
        // The Glyph Matrix only exists on Nothing OS 3.5+ devices.
        minSdk = 34
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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

dependencies {
    // Nothing's Glyph Matrix SDK. Not redistributable, so it is not committed:
    // run ./tools/fetch-sdk.sh (or drop the .aar into app/libs yourself).
    implementation(fileTree("libs") { include("*.aar") })

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
}

tasks.named("preBuild") {
    doFirst {
        if (fileTree("libs").matching { include("*.aar") }.isEmpty) {
            throw GradleException(
                "Glyph Matrix SDK not found in app/libs. Run ./tools/fetch-sdk.sh first."
            )
        }
    }
}
