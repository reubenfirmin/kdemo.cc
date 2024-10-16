import com.github.gradle.node.npm.task.NpxTask
import java.nio.file.Files
import java.util.concurrent.Executors

plugins {
    kotlin("multiplatform") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("com.github.node-gradle.node") version "7.0.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinWrappers = "1.0.0-pre.800"

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
//                commonWebpackConfig {
//                    configDirectory = projectDir.resolve("webpack.config.d")
//                }
            }
            runTask {
                mainOutputFileName = "main.bundle.js"
                devServer = devServer.copy(
                    open = true,
                    port = 3000,
                )
                sourceMaps = true
            }
            webpackTask {
                mainOutputFileName = "main.bundle.js"
                sourceMaps = true
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.platform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:$kotlinWrappers"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-js")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-browser")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-web")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-styled-next")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.11.0")
                implementation(npm("tailwindcss", "3.4.4"))
                implementation(npm("sortablejs", "1.15.1"))
            }
        }
    }
}

val buildStyle: TaskProvider<NpxTask> = tasks.register<NpxTask>("buildStyle") {
    println("Running tailwind")
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

    doLast {
        val kotlinSourceDir = project.file("src/jsMain/kotlin")
        val templateCssFile = project.file("src/jsMain/resources/template.css")
        val outputCssFile = project.file("src/jsMain/resources/style.css")

        val executor = Executors.newSingleThreadScheduledExecutor()
        var devServerProcess: Process? = null

        fun shutdown() {
            println("Shutting down watch task...")
            executor.shutdownNow()
            devServerProcess?.destroyForcibly()
            // Wait for the dev server to terminate
            devServerProcess?.waitFor(5, TimeUnit.SECONDS)
            // Kill any remaining Gradle daemons
            "pkill -f 'GradleDaemon'".runCommand()
            // Kill any remaining Node.js processes
            "pkill -f 'node'".runCommand()
        }

        val shutdownHook = Thread {
            shutdown()
        }

        Runtime.getRuntime().addShutdownHook(shutdownHook)

        try {
            executor.scheduleWithFixedDelay({
                try {
                    val outputCssModifiedTime = if (outputCssFile.exists()) Files.getLastModifiedTime(outputCssFile.toPath()).toMillis() else 0L
                    val templateCssChanged = Files.getLastModifiedTime(templateCssFile.toPath()).toMillis() > outputCssModifiedTime
                    val kotlinSourceChanged = kotlinSourceDir.walk().filter { it.isFile }.any {
                        Files.getLastModifiedTime(it.toPath()).toMillis() > outputCssModifiedTime
                    }

                    if (templateCssChanged || kotlinSourceChanged) {
                        println("Changes detected! Rebuilding styles...")
                        outputCssFile.delete()
                        project.exec {
                            commandLine("./gradlew", "buildStyle")
                            isIgnoreExitValue = true
                        }
                    }
                } catch (e: Exception) {
                    println("Error in watch task: ${e.message}")
                }
            }, 0, 1, TimeUnit.SECONDS)

            // Start the Kotlin webpack dev server in a separate process
            devServerProcess = ProcessBuilder("./gradlew", "jsBrowserRun", "--continuous")
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()

            // Wait for the dev server process to finish
            devServerProcess.waitFor()
        } finally {
            // Ensure cleanup happens even if an exception is thrown
            shutdown()
            Runtime.getRuntime().removeShutdownHook(shutdownHook)
        }
    }
}

tasks {
    register("deploy") {
        group = "build"
        description = "Performs a production build and copies output to dist directory"

        dependsOn("build")

        doLast {
            // Debug: Print all tasks that were executed
            println("Executed tasks: ${gradle.taskGraph.allTasks.map { it.name }}")

            // Ensure the dist directory exists and is empty
            delete("$rootDir/dist")
            mkdir("$rootDir/dist")

            // Copy the production build output to the dist directory
            copy {
                from("$buildDir/dist/js/productionExecutable")
                into("$rootDir/dist")
                include("**/*")  // This ensures all files and subdirectories are copied
            }

            println("\nProduction build completed and copied to $rootDir/dist")
        }
    }
}

fun String.runCommand() {
    try {
        Runtime.getRuntime().exec(this)
    } catch (e: Exception) {
        println("Error running command '$this': ${e.message}")
    }
}