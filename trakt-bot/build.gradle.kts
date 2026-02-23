import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
    id("org.openapi.generator")
}

group = "jp.xhw"
version = "1.1.1"

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
val apiSpecFile = layout.projectDirectory.file("spec/v3-api.yaml")
val apiSpecPath = apiSpecFile.asFile.absolutePath

val refreshApiSpecTask: TaskProvider<Task> =
    tasks.register("refreshApiSpec") {
        group = "build setup"
        description = "Refreshes the bundled traQ OpenAPI spec used for code generation."
        outputs.file(apiSpecFile)

        doLast {
            val targetFile = apiSpecFile.asFile
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
                "Activity,Authentication,Bot,Clip,Public,Star,Group,Notification,Ogp,Pin,UserTag,User,Me,Message,Channel,File,Stamp,Webhook",
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
    inputs.file(apiSpecFile)
}

sourceSets.main {
    kotlin.srcDir(generatedTraqClientDir.map { it.dir("src/commonMain/kotlin") })
}

tasks.named("compileKotlin") {
    dependsOn(tasks.openApiGenerate)
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
    jvmToolchain(21)
    sourceSets {
        all {
            languageSettings.enableLanguageFeature("ContextParameters")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "trakt-bot"
            version = project.version.toString()

            from(components["kotlin"])
        }
    }
}
