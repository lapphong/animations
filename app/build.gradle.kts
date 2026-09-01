import com.android.build.api.variant.impl.VariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

apply(from = "signingConfigs.gradle")

android {
    namespace = "com.animations"
    compileSdk = 36

    val versionPropsFile = file("version.properties")
    val versionProps = Properties()
    if (versionPropsFile.canRead()) {
        versionProps.load(versionPropsFile.inputStream())
    } else {
        versionProps["VERSION_CODE"] = "0"
    }

    var code = 1
    versionProps["VERSION_CODE"]?.let { code = it.toString().toInt() + 1 }
    versionProps["VERSION_CODE"] = code.toString()

    val name = "1.0.0"
    versionProps["VERSION_NAME"] = name
    versionProps.store(versionPropsFile.writer(), null)

    defaultConfig {
        minSdk = 26
        targetSdk = 36
        versionCode = code
        versionName = name
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86_64")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            resValue("string", "app_name", "animsapp")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
            resValue("string", "app_name", "animsapp-debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("profile") {
            initWith(getByName("debug"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        resValues = true
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    //noinspection WrongGradleMethod
    androidComponents {
        onVariants { variant ->
            variant.outputs
                .forEach { output ->
                    val variantOutput = output as VariantOutputImpl

                    val applicationId = variant.applicationId.get() // com.exampleFree.app
                    val versionName = variantOutput.versionName.get() // e.g 1.0.0
                    val versionCode = variantOutput.versionCode.get() // e.g 1
                    val flavorName = variant.flavorName ?: "default" // e.g. Free
                    val buildType = variant.buildType // e.g. debug
                    val variantName = variant.name // e.g. FreeDebug

                    //customize your app name by using variables
                    variantOutput.outputFileName = "${flavorName}_v${versionName}_${buildType}_${getDate()}.apk"
                }
        }
    }
}

fun getDate(): String {
    val date = Date()
    val sdf = SimpleDateFormat("yyyyMMdd")
    return sdf.format(date)
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.glide)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(files("libs/disintegration-1.0.0.aar"))
    implementation(files("libs/carousel_slider-1.0.0.aar"))
//    implementation(project(":disintegration"))

    // Navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // Module flutter
    debugImplementation(libs.flutter.debug)
    releaseImplementation(libs.flutter.release)
    add("profileImplementation", libs.flutter.profile)
}

configurations {
    getByName("profileImplementation") {
    }
}
