pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Vito"
include(":app:client")
include(":app:driver")
include(":app:admin")
include(":core:ui")
include(":core:data")
include(":core:domain")
include(":core:common")
