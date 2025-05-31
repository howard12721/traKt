plugins {
    kotlin("jvm") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

group = "com.github.howard12721"
version = "1.0.0"

repositories {
    mavenCentral()
}

val ktorVersion = "2.3.10"

dependencies {
    implementation("io.ktor:ktor-client-core:${ktorVersion}")
    implementation("io.ktor:ktor-client-cio:${ktorVersion}")
    implementation("io.ktor:ktor-client-websockets:${ktorVersion}")
    implementation("io.ktor:ktor-client-content-negotiation:${ktorVersion}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${ktorVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
    implementation("ch.qos.logback:logback-classic:1.5.18")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
