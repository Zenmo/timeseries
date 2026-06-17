import java.net.URI

plugins {
    kotlin("jvm") version "2.4.0"
    `maven-publish`
}

group = "com.zenmo"
version = "0.0.1"

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

publishing {
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = URI("https://maven.pkg.github.com/zenmo/timeseries")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

