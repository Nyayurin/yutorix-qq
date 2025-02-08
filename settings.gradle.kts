rootProject.name = "Yutorix-QQ"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/Nyayurin/yutori")
            credentials {
                username = providers.gradleProperty("gpr.actor").orNull ?: System.getenv("GITHUB_ACTOR")
                password = providers.gradleProperty("gpr.token").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
        google()
        mavenCentral()
    }
}

include(":core", ":adapter")
findProject(":core")!!.name = "yutorix-qq-core"
findProject(":adapter")!!.name = "yutorix-qq-adapter"