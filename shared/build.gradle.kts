plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    defaultConfig {
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }
    }

    buildTypes {
        getByName("release") {

        }
        getByName("debug") {

        }
    }

    lintOptions {
        disable("InvalidPackage", "MissingTranslation")
        // Version changes are beyond our control, so don't warn. The IDE will still mark these.
        disable("GradleDependency")
        // Timber needs to update to new Lint API
        disable("ObsoleteLintCustomCheck")
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

    // Some libs (such as androidx.core:core-ktx 1.2.0 and newer) require Java 8
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
    api(project(":model"))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    //testImplementation(project(":test-shared"))
    //testImplementation(project(":androidTest-shared"))

    // AppCompat
    implementation(Libs.APPCOMPAT)

    // Glide
    implementation(Libs.GLIDE)
    kapt(Libs.GLIDE_COMPILER)

    // Architecture Components
    implementation(Libs.LIFECYCLE_LIVE_DATA_KTX)
    implementation(Libs.LIFECYCLE_VIEW_MODEL_KTX)
    implementation(Libs.ROOM_KTX)
    implementation(Libs.ROOM_RUNTIME)
    kapt(Libs.ROOM_COMPILER)
    testImplementation(Libs.ARCH_TESTING)

    // Utils
    api(Libs.TIMBER)
    implementation(Libs.GSON)
    implementation(Libs.CORE_KTX)

    // OkHttp
    implementation(Libs.OKHTTP)
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)

    // Kotlin
    implementation(Libs.KOTLIN_STDLIB)

    // Coroutines
    api(Libs.COROUTINES)
    testImplementation(Libs.COROUTINES_TEST)

    // Androidx Work
    api(Libs.WORK_RUNTIME)
    api(Libs.WORK_GCM)
    api(Libs.WORK_RXJAVA)

    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    api(Libs.HILT_ANDROIDX_WORK)
    kapt(Libs.HILT_COMPILER)

    // Firebase
    api(Libs.FIREBASE_AUTH)
    api(Libs.FIREBASE_CONFIG)
    api(Libs.FIREBASE_ANALYTICS)
    api(Libs.FIREBASE_CRASHLYTICS)
    api(Libs.FIREBASE_IN_APP_MESSAGING)
    api(Libs.FIREBASE_IN_APP_MESSAGING_DISPLAY)
    api(Libs.FIREBASE_CRASHLYTICS)
    /*api(Libs.FIREBASE_FIRESTORE)
    api(Libs.FIREBASE_FUNCTIONS)*/
    api(Libs.FIREBASE_MESSAGING)
    api(Libs.ADMOB)

    api(Libs.GOOGLE_BASEMENT)

    // Has to be replaced to avoid compile / runtime conflicts between okhttp and firestore
    api(Libs.OKIO)

    api(Libs.RETROFIT)
    api(Libs.RETROFIT_RXJAVA_ADAPTER)
    api(Libs.RETROFIT_GSON_CONVERTER)

    // ThreeTenBP for the shared module only. Date and time API for Java.
    //testImplementation(Libs.THREETENBP)
    //compileOnly("org.threeten:threetenbp:${Versions.THREETENBP}:no-tzdb")

    // Unit tests
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.HAMCREST)
    testImplementation(Libs.MOCKITO_CORE)
    testImplementation(Libs.MOCKITO_KOTLIN)

    // unit tests livedata
    testImplementation(Libs.ARCH_TESTING)
}

apply(plugin = "com.google.gms.google-services")