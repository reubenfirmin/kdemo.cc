import com.github.gradle.node.npm.task.NpxTask
import java.nio.file.Files
import java.util.concurrent.Executors

plugins {
    kotlin("multiplatform") version "2.0.20"
    id("com.github.node-gradle.node") version "7.0.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            runTask {
                devServer = devServer.copy(
                    open = true,
                    port = 3000
                )
                sourceMaps = true
            }
            webpackTask {
                sourceMaps = true
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.11.0")
                implementation(npm("tailwindcss", "3.4.4"))
                implementation(npm("sortablejs", "1.15.1"))
            }
        }
    }
}

val buildStyle: TaskProvider<NpxTask> = tasks.register<NpxTask>("buildStyle") {
    println("Rerunning tailwind")
    command.set("tailwindcss")
    args.set(listOf("-i", "src/jsMain/resources/template.css", "-o", "src/jsMain/resources/style.css"))
    inputs.files(fileTree("src/jsMain/kotlin"))
    inputs.file("src/jsMain/resources/template.css")
    outputs.file("src/jsMain/resources/style.css")
}

tasks.named("jsProcessResources") {
    dependsOn(buildStyle)
}

tasks.register("watch") {
    dependsOn("buildStyle")
    dependsOn("jsBrowserRun")

    doLast {
        val templateCssFile = project.file("src/jsMain/resources/template.css")
        val outputCssFile = project.file("src/jsMain/resources/style.css")

        val executor = Executors.newSingleThreadScheduledExecutor()

        executor.scheduleWithFixedDelay({
            try {
                val outputCssModifiedTime = if (outputCssFile.exists()) Files.getLastModifiedTime(outputCssFile.toPath()).toMillis() else 0L
                val templateCssChanged = Files.getLastModifiedTime(templateCssFile.toPath()).toMillis() > outputCssModifiedTime

                if (templateCssChanged) {
                    println("CSS template changed! Rebuilding styles...")
                    outputCssFile.delete()
                    project.exec {
                        commandLine("./gradlew", "buildStyle")
                        isIgnoreExitValue = true
                    }
                }
            } catch (e: Exception) {
                println("Error in CSS watch task: ${e.message}")
            }
        }, 0, 1, TimeUnit.SECONDS)

        Runtime.getRuntime().addShutdownHook(Thread {
            executor.shutdown()
        })

        while (true) {
            Thread.sleep(1000)
        }
    }
}