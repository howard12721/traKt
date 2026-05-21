plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
}

repositories {
    mavenCentral()
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
        val nativeMain by creating {
            dependsOn(commonMain.get())
        }

        linuxX64Main {
            dependsOn(nativeMain)
        }
        linuxArm64Main {
            dependsOn(nativeMain)
        }
        macosArm64Main {
            dependsOn(nativeMain)
        }
        mingwX64Main {
            dependsOn(nativeMain)
        }
    }
}
