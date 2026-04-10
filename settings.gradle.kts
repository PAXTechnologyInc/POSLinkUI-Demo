pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // Mirrors for environments where google()/pluginPortal are slow or blocked
        maven(url = "https://maven.aliyun.com/repository/google")
        maven(url = "https://maven.aliyun.com/repository/central")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin")
        maven(url = "https://maven.aliyun.com/repository/public")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
}

rootProject.name = "POSLinkUI-Demo"
include(":app")
