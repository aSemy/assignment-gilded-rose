import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  kotlin("jvm")
  id("dev.jacomet.logging-capabilities")
}

group = "com.gildedrose"
version = "1.0-SNAPSHOT"

dependencies {
  implementation(libs.bundles.logging)

  implementation(platform(libs.kotlin.bom))
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  testImplementation(libs.bundles.test.kotest)
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("passed", "skipped", "failed")
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = libs.versions.jvm.get()
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xopt-in=io.kotest.common.ExperimentalKotest",
        "-Xopt-in=kotlin.ExperimentalStdlibApi",
        "-Xopt-in=kotlin.experimental.ExperimentalTypeInference",
        "-Xopt-in=kotlin.time.ExperimentalTime",
    )
  }
}

tasks.wrapper {
  gradleVersion = "7.1.1"
  distributionType = Wrapper.DistributionType.ALL
}

loggingCapabilities {
  enforceLogback()
}

idea {
  module {
    isDownloadJavadoc = true
    isDownloadSources = true
  }
}
