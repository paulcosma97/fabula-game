import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val koin_version = "3.1.2"

plugins {
    kotlin("jvm") version "1.5.20"
    application
}

group = "io.paulc03.fabula.server"
version = "1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.6.1")
    implementation("io.ktor:ktor-server-netty:1.6.1")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.ktor:ktor-websockets:1.6.1")
    implementation("com.beust:klaxon:5.5")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    implementation("io.insert-koin:koin-core:$koin_version")

    testImplementation("io.insert-koin:koin-test:$koin_version")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}