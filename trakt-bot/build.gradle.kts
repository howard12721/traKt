import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.openapi.generator)
}

group = "jp.xhw"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":trakt-websocket"))

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.core)
    testImplementation(kotlin("test"))
}

val generatedTraqClientDir: Provider<Directory> = layout.buildDirectory.dir("generated/traq-client")
val apiSpecUrl = "https://raw.githubusercontent.com/traPtitech/traQ/master/docs/v3-api.yaml"
val apiSpecFile: Provider<RegularFile> = layout.buildDirectory.file("downloaded-specs/v3-api.yaml")
val apiSpecPath: Provider<String> = apiSpecFile.map { it.asFile.absolutePath }

val downloadApiSpecTask: TaskProvider<Task> =
    tasks.register("downloadApiSpec") {
        outputs.file(apiSpecFile)

        doLast {
            val targetFile = apiSpecFile.get().asFile
            targetFile.parentFile.mkdirs()

            URI.create(apiSpecUrl).toURL().openStream().use { input ->
                Files.copy(input, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }

openApiGenerate {
    generatorName.set("kotlin")
    library.set("multiplatform")
    globalProperties.set(
        mapOf(
            "apis" to
                "Activity,Authentication,Bot,Clip,Public,Star,Group,Notification,Ogp,Pin,UserTag,Webrtc,User,Me,Message,Channel,File,Stamp,Webhook,Oauth2",
            "models" to "",
            "supportingFiles" to "",
        ),
    )
    additionalProperties.set(
        mapOf(
            "dateLibrary" to "kotlinx-datetime",
            "enumPropertyNaming" to "UPPERCASE",
        ),
    )

    inputSpec.set(apiSpecPath)
    outputDir.set(generatedTraqClientDir.map { it.asFile.absolutePath })

    typeMappings.set(
        mapOf(
            "UUID" to "kotlin.uuid.Uuid",
            "uuid" to "kotlin.uuid.Uuid",
            "string+date-time" to "Instant",
        ),
    )

    packageName.set("jp.xhw.trakt.rest")
}

tasks.openApiGenerate {
    dependsOn(downloadApiSpecTask)
}

sourceSets.main {
    kotlin.srcDir(generatedTraqClientDir.map { it.dir("src/commonMain/kotlin") })
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
        freeCompilerArgs.add("-opt-in=io.ktor.utils.io.InternalAPI")
    }
    jvmToolchain(25)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "trakt-websocket"
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
}
