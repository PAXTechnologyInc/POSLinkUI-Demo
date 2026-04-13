import com.android.build.gradle.api.ApkVariantOutput
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import java.io.FileInputStream
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
}

apply(plugin = "org.jetbrains.dokka")

fun releaseTime(): String =
    LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.BASIC_ISO_DATE)

val appVersionName = "V1.04.00_${releaseTime()}"
val apkBaseName = "POSLinkUI-Demo_$appVersionName"

val signingProps = Properties().apply {
    val signingFile = file("signing.properties")
    if (signingFile.exists()) {
        FileInputStream(signingFile).use { load(it) }
    }
}

android {
    compileSdk = 34

    signingConfigs {
        create("release") {
            if (signingProps.isNotEmpty()) {
                keyAlias = signingProps.getProperty("keyAlias")
                keyPassword = signingProps.getProperty("keyPassword")
                storeFile = file(signingProps.getProperty("storeFile"))
                storePassword = signingProps.getProperty("storePassword")
            } else {
                keyAlias = "paxdemo"
                keyPassword = "paxdemo"
                storeFile = file("pax_demo.jks")
                storePassword = "paxdemo"
            }
        }
    }

    defaultConfig {
        applicationId = "com.paxus.pay.poslinkui.demo"
        minSdk = 22
        targetSdk = 31
        versionCode = 23
        versionName = appVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            // R8 obfuscates dex/class names for release APK (PAX security naming-ratio checks on decompiled artifacts).
            isMinifyEnabled = true
            isShrinkResources = false
            isZipAlignEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    namespace = "com.paxus.pay.poslinkui.demo"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    @Suppress("DEPRECATION")
    applicationVariants.configureEach {
        if (buildType.name == "release") {
            outputs.configureEach {
                (this as ApkVariantOutput).outputFileName = "$apkBaseName.apk"
            }
            // Avoid overriding packageApplication outputDirectory: writing APK under repo-root `apks/` leaves
            // `app/build/outputs/apk/release/output-metadata.json` missing and breaks
            // `createReleaseApkListingFileRedirect`. CI artifacts use `**/build/outputs/apk/**/*.apk`.
        }
    }

    lint {
        baseline = file("lint-baseline.xml")
        abortOnError = true
    }

    testOptions {
        // Local unit tests use stub android.jar; this avoids RuntimeException from Bundle/Intent stubs.
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(libs.pax.constant)

    implementation(libs.orhanobut.logger)
    implementation(libs.zxing.core)

    implementation(libs.coil)
    implementation(libs.coil.gif)
    implementation(libs.coil.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

tasks.register("dokkaGeneratePublicationHtml") {
    dependsOn("dokkaHtml")
}

tasks.withType<Test>().configureEach {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        showCauses = true
        showStackTraces = true
    }
}
