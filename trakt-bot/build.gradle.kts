plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.github.howard12721"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":trakt-websocket"))
    implementation(project(":trakt-rest"))
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
