plugins {
    kotlin("jvm") version "2.3.10"
}

group = "com.zenmo"
version = "1.0-SNAPSHOT"

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
    jvmToolchain(17)

    compilerOptions {
        compilerOptions.freeCompilerArgs.add("-Xjvm-default=all")
    }
}
