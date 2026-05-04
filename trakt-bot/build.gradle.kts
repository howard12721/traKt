import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier
import java.net.URI
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
    id("org.openapi.generator")
    id("org.jetbrains.dokka")
}

group = "jp.xhw"
version = "4.1.0"

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
    outputDir.set(generatedTraqClientDir)

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

val internalizeTraqClientTask =
    tasks.register("internalizeTraqClient") {
        dependsOn(tasks.openApiGenerate)
        inputs.dir(generatedTraqClientDir)
        outputs.upToDateWhen { false }

        doLast {
            val generatedSourceRoot =
                generatedTraqClientDir
                    .get()
                    .asFile
                    .toPath()
                    .resolve("src/commonMain/kotlin")
            val declarationRegex =
                Regex(
                    pattern =
                        """(?m)^(?!internal\b)((?:(?:inline|suspend|operator|infix|tailrec|external)\s+)*(?:open\s+class|data\s+class|sealed\s+class|enum\s+class|class|interface|object|typealias|fun|val|var)\b)""",
                )

            Files.walk(generatedSourceRoot).use { paths ->
                paths
                    .filter { Files.isRegularFile(it) && it.fileName.toString().endsWith(".kt") }
                    .forEach { path ->
                        val source = Files.readString(path, StandardCharsets.UTF_8)
                        val internalized = source.replace(declarationRegex, "internal $1")
                        if (internalized != source) {
                            Files.writeString(path, internalized, StandardCharsets.UTF_8)
                        }
                    }
            }
        }
    }

sourceSets.main {
    kotlin.srcDir(generatedTraqClientDir.map { it.dir("src/commonMain/kotlin") })
}

tasks.named("compileKotlin") {
    dependsOn(internalizeTraqClientTask)
}

tasks.test {
    useJUnitPlatform()
}

dokka {
    dokkaPublications.html {
        moduleName.set("trakt-bot")
        moduleVersion.set(project.version.toString())
        outputDirectory.set(layout.buildDirectory.dir("dokka/html"))
        suppressObviousFunctions.set(true)
        failOnWarning.set(false)
    }

    dokkaSourceSets.configureEach {
        documentedVisibilities.set(setOf(VisibilityModifier.Public))
        skipEmptyPackages.set(true)
        suppressGeneratedFiles.set(true)

        perPackageOption {
            matchingRegex.set("jp\\.xhw\\.trakt\\.bot\\.infrastructure\\.(gateway|runtime\\.(bot|user))(\\..*)?")
            suppress.set(true)
        }
        perPackageOption {
            matchingRegex.set("jp\\.xhw\\.trakt\\.bot\\.port(\\..*)?")
            suppress.set(true)
        }
        perPackageOption {
            matchingRegex.set("jp\\.xhw\\.trakt\\.rest(\\..*)?")
            suppress.set(true)
        }
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-opt-in=kotlin.time.ExperimentalTime")
        freeCompilerArgs.add("-opt-in=kotlin.uuid.ExperimentalUuidApi")
        freeCompilerArgs.add("-opt-in=io.ktor.utils.io.InternalAPI")
        freeCompilerArgs.add("-Xcontext-parameters")
    }
    jvmToolchain(21)
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
