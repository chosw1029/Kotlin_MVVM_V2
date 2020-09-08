
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("io.fabric")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    defaultConfig {
        applicationId = "com.nextus.kotlinmvvmexample"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = Versions.versionCodeMobile
        versionName = Versions.versionName
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        manifestPlaceholders = mapOf("crashlyticsEnabled" to true)

        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            manifestPlaceholders = mapOf("crashlyticsEnabled" to true)
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            versionNameSuffix = "-debug"
            manifestPlaceholders = mapOf("crashlyticsEnabled" to false)
        }
    }

    buildFeatures {
        dataBinding = true
    }

    // debug and release variants share the same source dir
    sourceSets {
        getByName("debug") {
            java.srcDir("src/debugRelease/java")
        }
        getByName("release") {
            java.srcDir("src/debugRelease/java")
        }
    }

    lintOptions {
        // Eliminates UnusedResources false positives for resources used in DataBinding layouts
        isCheckGeneratedSources = true
        // Running lint over the debug variant is enough
        isCheckReleaseBuilds = false
        // See lint.xml for rules configuration
    }

    // Required for AR because it includes a library built with Java 8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    // To avoid the compile error: "Cannot inline bytecode built with JVM target 1.8
    // into bytecode that is being built with JVM target 1.6"
    kotlinOptions {
        val options = this as org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
        options.jvmTarget = "1.8"
    }
}

dependencies {
    api(platform(project(":depconstraints")))
    kapt(platform(project(":depconstraints")))

    implementation(project(":shared"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    // UI
    implementation(Libs.ACTIVITY_KTX)
    implementation(Libs.CORE_KTX)
    implementation(Libs.APPCOMPAT)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.CARDVIEW)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.DRAWER_LAYOUT)
    implementation(Libs.MATERIAL)
    implementation(Libs.LOTTIE)
    implementation(Libs.INK_PAGE_INDICATOR)

    // Architecture Components
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    kapt(Libs.LIFECYCLE_COMPILER)
    testImplementation(Libs.ARCH_TESTING)
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)
    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.ROOM_KTX)
    implementation(Libs.ROOM_RUNTIME)
    kapt(Libs.ROOM_COMPILER)
    testImplementation(Libs.ROOM_KTX)
    testImplementation(Libs.ROOM_RUNTIME)

    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    implementation(Libs.HILT_VIEWMODEL)
    androidTestImplementation(Libs.HILT_TESTING)
    kapt(Libs.HILT_COMPILER)
    kapt(Libs.ANDROIDX_HILT_COMPILER)
    kaptAndroidTest(Libs.HILT_COMPILER)
    kaptAndroidTest(Libs.ANDROIDX_HILT_COMPILER)

    // Glide
    implementation(Libs.GLIDE)
    kapt(Libs.GLIDE_COMPILER)

    // Fabric and Firebase
    implementation(Libs.FIREBASE_UI_AUTH)
    implementation(Libs.CRASHLYTICS)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)

    // Instrumentation tests
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.EXT_JUNIT)
    androidTestImplementation(Libs.RUNNER)
    androidTestImplementation(Libs.RULES)

    // Local unit tests
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO_CORE)
    testImplementation(Libs.MOCKITO_KOTLIN)
    testImplementation(Libs.HAMCREST)

    // Solve conflicts with gson. DataBinding is using an old version.
    implementation(Libs.GSON)

    implementation(Libs.ARCORE)

/*

    //implementation(project(":shared"))
    //implementation(project(":ar"))
    //testImplementation(project(":test-shared"))
    //testImplementation(project(":androidTest-shared"))





    implementation(Libs.BROWSER)

    implementation(Libs.FLEXBOX)






    // Fabric and Firebase
    implementation(Libs.FIREBASE_UI_AUTH)
    implementation(Libs.CRASHLYTICS)

    // Date and time API for Java.
    implementation(Libs.THREETENABP)
    testImplementation(Libs.THREETENBP)


   */
}

apply(plugin = "com.google.gms.google-services")
/*
*/
