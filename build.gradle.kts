plugins {
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    jacoco
}

allprojects {
    val mavenGroup: String by project

    group = mavenGroup

    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    val javaVersion: String by project
    val encoding: String by project

    tasks {
        withType<JavaCompile> {
            sourceCompatibility = javaVersion
            targetCompatibility = sourceCompatibility
            options.encoding = encoding
        }

        withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = javaVersion
            }
        }

        withType<Copy> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }

        withType<Test> {
            useJUnitPlatform()
        }

    }
}


tasks {
    withType<Wrapper> {
        val usedGradleVersion: String by project
        gradleVersion = usedGradleVersion
        distributionType = Wrapper.DistributionType.BIN
    }

    withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

jacoco {
    val jacocoVersion: String by project
    toolVersion = jacocoVersion
}