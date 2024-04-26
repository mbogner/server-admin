plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    api("com.fasterxml.jackson.core:jackson-annotations")

    testImplementation(project(":test-utils"))
}