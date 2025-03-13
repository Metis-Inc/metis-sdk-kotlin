plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.serialization") version "1.9.25"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.7.20"
}

group = "com.github.Metis-Inc"
version = "main-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.1")

    // HTTP Client
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // JSON
    implementation("com.squareup.moshi:moshi:1.14.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.moshi:moshi-adapters:1.14.0")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.5")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.25")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.25")
    testImplementation("org.mockito:mockito-core:5.0.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
}

java {
    withJavadocJar()
    withSourcesJar()
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "metis-sdk-kotlin"

            pom {
                name.set("Metis SDK")
                description.set("Official Kotlin SDK for Metis API")
                url.set("https://github.com/Metis-Inc/metis-sdk-kotlin")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                developers {
                    developer {
                        id.set("metis")
                        name.set("Metis Team")
                        email.set("info@metisai.ir")
                    }
                    developer {
                        id.set("mhdsdt")
                        name.set("Mahdi Saadatbakht")
                        email.set("m.saadatbakht@tapsell.ir")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/Metis-Inc/metis-sdk-kotlin.git")
                    developerConnection.set("scm:git:ssh://github.com:Metis-Inc/metis-sdk-kotlin.git")
                    url.set("https://github.com/Metis-Inc/metis-sdk-kotlin")
                }
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}