import java.util.Properties

// Classpath plugin versions — keep in sync with gradle/libs.versions.toml [versions] (agp, kotlin, hilt)
buildscript {
    repositories {
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.9.20")
        classpath("com.github.nic-bell:license-gradle-plugin:cac7e1c79a")
    }
}

apply(from = "gradle/github.gradle")
apply(from = "gradle/version.gradle")

if (project.hasProperty("sonar")) {
    apply(from = "gradle/sonar.gradle")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

val gradleProps = Properties().apply {
    val f = rootProject.file("gradle.properties")
    if (f.exists()) f.reader().use { load(it) }
}
val mavenLocalFirst = gradleProps.getProperty("maven.local.first")?.toBoolean() ?: false
logger.lifecycle("maven.local.first: $mavenLocalFirst")

val kotlinVersion = libs.versions.kotlin.get()

allprojects {
    configurations.configureEach {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlinVersion")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
            force("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        }
    }
    repositories {
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")
        if (mavenLocalFirst) {
            maven(url = uri(rootProject.file("maven")))
        } else {
            maven {
                url = uri("https://maven.pkg.github.com/PAXTechnologyInc/POSLink-UI")
                credentials {
                    username = rootProject.findProperty("GITHUB_USER_NAME") as String? ?: ""
                    password = rootProject.findProperty("GITHUB_USER_TOKEN") as String? ?: ""
                }
            }
        }
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        mavenLocal()
    }
}

apply(from = "gradle/license-allprojects.gradle")