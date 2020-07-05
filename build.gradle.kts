import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

version = "0.1"

// Check Java version, min 8 is required
if (JavaVersion.current() < JavaVersion.VERSION_1_8)
    throw RuntimeException("Min Java 8 required (running ${System.getProperty("java.version")})")

// Log CotD, Gradle and Java versions
println("Card Of The Dead $version")
println("Gradle ${gradle.gradleVersion} at ${gradle.gradleHomeDir}")
println("Java ${System.getProperty("java.version")}")

plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.3.70"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(kotlin("bom"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")

    testImplementation("io.kotest:kotest-core:4.0.0-BETA1")
    testImplementation("io.kotest:kotest-assertions:4.0.0-BETA1")
    testImplementation("io.kotest:kotest-runner-junit5:4.0.0-BETA1")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.0-BETA1")
    testImplementation("io.mockk:mockk:1.9.3")
}

application {
    mainClassName = "cardofthedead.CardOfTheDeadApplicationKt"
}

tasks.withType<Test> {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = application.mainClassName
        attributes["Class-Path"] =
            configurations.compileClasspath.get().joinToString(" ") { it.name }
        attributes["Implementation-Version"] = archiveVersion
    }

    println("Collecting dependencies to a fat jar:")
    from(configurations.compileClasspath.get().files
        .map { println("  ${it.name}"); it }
        .map { if (it.isDirectory) it else zipTree(it) })
}
