/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("java-platform")
    id("maven-publish")
}

val appcompat = "1.2.0"
val activity = "1.1.0"
val admob = "19.7.0"
val amplify = "1.16.13"
val cardview = "1.0.0"
val circleImageView = "3.1.0"
val archTesting = "2.0.0"
val arcore = "1.7.0"
val benchmark = "1.0.0"
val browser = "1.0.0"
val constraintLayout = "2.0.4"
val core = "1.3.2"
val coroutines = "1.3.9"
val coroutinesTest = "1.3.9"
val drawerLayout = "1.1.1"
val espresso = "3.1.1"
val facebookAds = "6.2.0.1"
val firebaseAnalytics = "18.0.2"
val firebaseCrashlytics = "17.3.1"
val firebaseAuth = "20.0.2"
val firebaseConfig = "20.0.3"
val firebaseMessaging = "21.0.1"
val firebaseInAppMessaging = "19.1.2"
val firebaseFirestore = "22.1.0"
val firebaseUi = "7.1.0"
val flexbox = "1.1.0"
val fragment = "1.2.4"
val glide = "4.11.0"
val glideSVG = "3.0.0"
val googleMapUtils = "0.5"
val googlePlayServicesMaps = "16.0.0"
val googlePlayServicesVision = "17.0.2"
val googleBasement = "17.5.0"
val gson = "2.8.6"
val hamcrest = "1.3"
val htmlTextView ="3.9"
val hilt = Versions.HILT
val hiltJetPack = "1.0.0-alpha01"
val imagePicker = "1.7.5"
val inlineActivityResult = "1.0.4"
val junit = "4.13"
val junitExt = "1.1.1"
val lifecycle = "2.2.0"
val lottie = "3.4.4"
val material = "1.1.0"
val mockito = "3.3.1"
val mockitoKotlin = "1.5.0"
val mpAndroidChart = "v3.1.0"
val okhttp = "3.10.0"
val okio = "1.14.0"
val pageIndicator = "1.3.0"
val playCore = "1.6.5"
val retrofit = "2.7.1"
val room = "2.2.5"
val rules = "1.1.1"
val runner = "1.2.0"
val tedImagePicker = "1.1.7"
val threetenabp = "1.0.5"
val timber = "4.7.1"
val viewpager2 = "1.0.0"
val work = "2.4.0"
val youtube = "1.4.0"

dependencies {
    constraints {
        api("${Libs.ACTIVITY_KTX}:$activity")
        api("${Libs.ADMOB}:$admob")
        api("${Libs.AMPLIFY_CORE}:$amplify")
        api("${Libs.AMPLIFY_STORAGE}:$amplify")
        api("${Libs.AMPLIFY_AUTH}:$amplify")
        api("${Libs.ANDROIDX_HILT_COMPILER}:$hiltJetPack")
        api("${Libs.APPCOMPAT}:$appcompat")
        api("${Libs.CARDVIEW}:$cardview")
        api("${Libs.CIRCLE_IMAGE_VIEW}:$circleImageView")
        api("${Libs.ARCH_TESTING}:$archTesting")
        api("${Libs.ARCORE}:$arcore")
        api("${Libs.BENCHMARK}:$benchmark")
        api("${Libs.CONSTRAINT_LAYOUT}:$constraintLayout")
        api("${Libs.CORE_KTX}:$core")
        api("${Libs.COROUTINES}:$coroutines")
        api("${Libs.COROUTINES_TEST}:$coroutines")
        api("${Libs.DRAWER_LAYOUT}:$drawerLayout")
        api("${Libs.ESPRESSO_CORE}:$espresso")
        api("${Libs.ESPRESSO_CONTRIB}:$espresso")
        api("${Libs.FRAGMENT_KTX}:$fragment")
        api("${Libs.GLIDE}:$glide")
        api("${Libs.GLIDE_COMPILER}:$glide")
        api("${Libs.GLIDE_SVG}:$glideSVG")
        api("${Libs.GOOGLE_BASEMENT}:$googleBasement")
        api("${Libs.GSON}:$gson")
        api("${Libs.HAMCREST}:$hamcrest")
        api("${Libs.HILT_ANDROID}:$hilt")
        api("${Libs.HILT_ANDROIDX_WORK}:$hiltJetPack")
        api("${Libs.HILT_COMPILER}:$hilt")
        api("${Libs.HILT_TESTING}:$hilt")
        api("${Libs.HILT_VIEWMODEL}:$hiltJetPack")
        api("${Libs.HTML_TEXT_VIEW}:$htmlTextView")
        api("${Libs.IMAGE_PICKER}:$imagePicker")
        api("${Libs.INLINE_ACTIVITY_RESULT}:$inlineActivityResult")
        api("${Libs.JUNIT}:$junit")
        api("${Libs.EXT_JUNIT}:$junitExt")
        api("${Libs.KOTLIN_STDLIB}:${Versions.KOTLIN}")
        api("${Libs.LIFECYCLE_COMPILER}:$lifecycle")
        api("${Libs.LIFECYCLE_LIVE_DATA_KTX}:$lifecycle")
        api("${Libs.LIFECYCLE_VIEW_MODEL_KTX}:$lifecycle")
        api("${Libs.LOTTIE}:$lottie")
        api("${Libs.MATERIAL}:$material")
        api("${Libs.MOCKITO_CORE}:$mockito")
        api("${Libs.MOCKITO_KOTLIN}:$mockitoKotlin")
        api("${Libs.MP_ANDROID_CHART}:$mpAndroidChart")
        api("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
        api("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")
        api("${Libs.ROOM_KTX}:$room")
        api("${Libs.ROOM_RUNTIME}:$room")
        api("${Libs.ROOM_COMPILER}:$room")
        api("${Libs.OKHTTP}:$okhttp")
        api("${Libs.OKHTTP_LOGGING_INTERCEPTOR}:$okhttp")
        api("${Libs.OKIO}:$okio")
        api("${Libs.INK_PAGE_INDICATOR}:$pageIndicator")
        api("${Libs.RETROFIT}:$retrofit")
        api("${Libs.RETROFIT_GSON_CONVERTER}:$retrofit")
        api("${Libs.RETROFIT_RXJAVA_ADAPTER}:$retrofit")
        api("${Libs.RULES}:$rules")
        api("${Libs.RUNNER}:$runner")
        api("${Libs.TED_IMAGE_PICKER}:$tedImagePicker")
        api("${Libs.TIMBER}:$timber")
        api("${Libs.VIEWPAGER2}:$viewpager2")

        api("${Libs.WORK_RUNTIME}:$work")
        api("${Libs.WORK_GCM}:$work")
        api("${Libs.WORK_RXJAVA}:$work")

        api("${Libs.FACEBOOK_ADS}:$facebookAds")
        api("${Libs.FIREBASE_AUTH}:$firebaseAuth")
        api("${Libs.FIREBASE_CONFIG}:$firebaseConfig")
        api("${Libs.FIREBASE_ANALYTICS}:$firebaseAnalytics")
        api("${Libs.FIREBASE_CRASHLYTICS}:$firebaseCrashlytics")
        api("${Libs.FIREBASE_FIRESTORE}:$firebaseFirestore")
        //api("${Libs.FIREBASE_FUNCTIONS}:$firebaseFunctions")
        api("${Libs.FIREBASE_MESSAGING}:$firebaseMessaging")
        api("${Libs.FIREBASE_IN_APP_MESSAGING}:$firebaseInAppMessaging")
        api("${Libs.FIREBASE_IN_APP_MESSAGING_DISPLAY}:$firebaseInAppMessaging")
        api("${Libs.FIREBASE_UI_AUTH}:$firebaseUi")

        api("${Libs.YOUTUBE}:$youtube")
    }
}

publishing {
    publications {
        create<MavenPublication>("myPlatform") {
            from(components["javaPlatform"])
        }
    }
}
