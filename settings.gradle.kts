pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "kitchenpos"

include("kitchenpos-domain")
include("kitchenpos-api")
include("module:util")
