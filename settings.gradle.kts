rootProject.name = "buyMeCoffeeBack"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        // add mavenLocal() if you are using a locally built version of the plugin
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.google.cloud.tools.appengine") {
                useModule("com.google.cloud.tools:appengine-gradle-plugin:${requested.version}")
            }
        }
    }
}
