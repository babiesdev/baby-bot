import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.spring") version "1.7.20"
    id("org.springframework.boot") version "2.7.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

group = "io.baby-developers.baby-bot"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-test")
    implementation("net.dv8tion:JDA:5.0.0-alpha.20")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
