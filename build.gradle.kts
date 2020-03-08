plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.41"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    mavenCentral()
}

tasks.withType<Test> {
    useJUnitPlatform()
    filter {
        isFailOnNoMatchingTests = false
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(kotlin("bom"))
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("io.kotest:kotest-core:4.0.0-BETA1")
    testImplementation("io.kotest:kotest-assertions:4.0.0-BETA1")
    testImplementation("io.kotest:kotest-runner-junit5:4.0.0-BETA1")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.0-BETA1")
    testImplementation("io.mockk:mockk:1.9.3")
}

application {
    mainClassName = "cardofthedead.CardOfTheDeadApplicationKt"
}
