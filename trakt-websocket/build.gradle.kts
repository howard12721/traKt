plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvm()
    js(IR) {
        browser()
        nodejs()
    }
    linuxX64()
    linuxArm64()
    macosArm64()
    mingwX64()

    jvmToolchain(21)

    sourceSets {
        commonMain {
            kotlin.srcDir("src/main/kotlin")
            dependencies {
                implementation(project(":trakt-core"))
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.websockets)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        val nativeMain by creating {
            dependsOn(commonMain.get())
        }

        jvmMain {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        jsMain {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

        val linuxMain by creating {
            dependsOn(nativeMain)
            dependencies {
                implementation(libs.ktor.client.curl)
            }
        }
        linuxX64Main {
            dependsOn(linuxMain)
        }
        linuxArm64Main {
            dependsOn(linuxMain)
        }

        val macosMain by creating {
            dependsOn(nativeMain)
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        macosArm64Main {
            dependsOn(macosMain)
        }

        mingwX64Main {
            dependsOn(nativeMain)
            dependencies {
                implementation(libs.ktor.client.winhttp)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}
