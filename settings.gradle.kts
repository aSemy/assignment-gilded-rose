rootProject.name = "gilded-rose"

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://jitpack.io")
  }

  pluginManagement {
    plugins {
      val kotlinVersion = "1.5.21"
      kotlin("jvm") version kotlinVersion
      kotlin("kapt") version kotlinVersion
      kotlin("stdlib") version kotlinVersion
      kotlin("script-runtime") version kotlinVersion
      kotlin("reflect") version kotlinVersion
      kotlin("plugin.serialization") version kotlinVersion

      id("dev.jacomet.logging-capabilities") version "0.9.0"
    }

    repositories {
      gradlePluginPortal()
    }
  }
}
