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

object Versions {
    val versionName = "1.0.10" // X.Y.Z; X = Major, Y = minor, Z = Patch level
    private val versionCodeBase = 10100 // XYYZZM; M = Module (tv, mobile)
    val versionCodeMobile = versionCodeBase + 3

    const val COMPILE_SDK = 29
    const val TARGET_SDK = 29
    const val MIN_SDK = 21

    const val AMPLIFY = "1.0.2"
    const val ANDROID_GRADLE_PLUGIN = "4.1.2"
    const val BENCHMARK = "1.0.0"
    const val CRASHLYTICS = "2.5.0"
    const val GOOGLE_SERVICES = "4.3.5"
    const val KOTLIN = "1.4.10"
    const val NAVIGATION = "2.3.3"
    const val HILT = "2.32-alpha"

    // TODO: Remove this once the version for
    //  "org.threeten:threetenbp:${Versions.threetenbp}:no-tzdb" using java-platform in the
    //  depconstraints/build.gradle.kts.kts is defined
    const val THREETENBP = "1.3.6"
}
