plugins {
    kotlin("multiplatform") version "1.4-M1"
    kotlin("plugin.serialization") version "1.4-M1"
}

group = "me.miso"
version = "1.0-SNAPSHOT"
val ktor_version = "1.3.2-1.4-M1-2"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlinx")
    maven("https://dl.bintray.com/kotlin/ktor")
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
}

dependencies {
    commonMainImplementation("io.ktor:ktor-client-core:$ktor_version")
    commonMainImplementation("io.ktor:ktor-client-json-native:$ktor_version")
    commonMainImplementation("io.ktor:ktor-client-serialization-native:$ktor_version")
    commonMainImplementation("io.ktor:ktor-client-curl:$ktor_version")
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        val nativeMain by getting {
        }

        val nativeTest by getting {
        }
    }
}
