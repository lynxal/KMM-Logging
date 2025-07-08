import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    id("com.vanniktech.maven.publish") version "0.33.0"
    id("signing")
}

kotlin {
    androidTarget()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "logging"
        }
    }

    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("kmm-logging")
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "kmm-logging.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.datetime)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
    }
}

android {
    namespace = "com.lynxal.logging"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain((findProperty("jvm.version") as String).toInt())
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("com.lynxal.logging", "logging", "0.0.6")
    pom {
        name.set("KMM Logging")
        description.set("A lightweight and flexible logging library for Kotlin Multiplatform Mobile (KMM) projects. It provides platform-specific logging implementations for Android and iOS, with an easy-to-use API and customizable log levels. Designed to integrate seamlessly into KMM applications.")
        url.set("https://github.com/lynxal/KMM-Logging")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/lynxal/KMM-Logging/blob/main/LICENSE")
            }
        }
        issueManagement {
            system.set("GitHub Issues")
            url.set("https://github.com/lynxal/KMM-Logging/issues")
        }

        developers {
            developer {
                id.set("VardanK")
                name.set("Vardan Kurkchiyan")
                email.set("central.repo@Lynxal.com")
            }
        }
        scm {
            connection.set("scm:git:git://github.com:lynxal/KMM-Logging.git")
            developerConnection.set("scm:git:ssh://github.com:lynxal/KMM-Logging.git")
            url.set("https://github.com/lynxal/KMM-Logging")
        }
    }
}