plugins {
    kotlin("jvm") version "2.0.0"
}

group = "dev.ftb.tooling"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}