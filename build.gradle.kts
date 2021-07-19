import net.ltgt.gradle.errorprone.errorprone
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  idea
  kotlin("jvm")
  id("dev.jacomet.logging-capabilities")
  jacoco
  id("info.solidsoft.pitest")
  id("net.ltgt.errorprone") version "2.0.2"
}

group = "com.gildedrose"
version = "1.0-SNAPSHOT"

dependencies {
  implementation(libs.bundles.logging)

  implementation(platform(libs.kotlin.bom))
  implementation(kotlin("stdlib"))
  implementation(kotlin("reflect"))

  testImplementation(libs.bundles.test.kotest)
  errorprone("com.google.errorprone:error_prone_core:2.7.1")
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

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.get()))
  }
}

tasks.withType<JavaCompile>().configureEach {
  options.errorprone.disableWarningsInGeneratedCode.set(true)
}

//<editor-fold desc="Test config">

tasks.test {
  useJUnitPlatform()
  systemProperty("gradle.build.dir", project.buildDir)
  testLogging {
    events("passed", "skipped", "failed")
  }
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events = setOf(
        TestLogEvent.FAILED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
    )
    exceptionFormat = TestExceptionFormat.FULL
  }
  reports {
    html.required.set(false)
    junitXml.required.set(true)
  }
  finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
  dependsOn(tasks.test)
  reports {
    xml.isEnabled = true
  }
}

pitest {
  pitestVersion.set("1.6.7")
  targetClasses.addAll(
      "com.gildedrose.*",
      "dev.adamko.*",
  )
  testPlugin.set("Kotest")
  timestampedReports.set(false)
  failWhenNoMutations.set(true)
  detectInlinedCode.set(true)
  verbose.set(true)
  mutators.add("STRONGER")
//  mutators.add("ALL")
//	testSourceSets.add(project.sourceSets.test)
}

tasks.pitest {
  finalizedBy(tasks.jacocoTestReport)
}

tasks.check {
  finalizedBy(tasks.pitest)
}

//</editor-fold>

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
