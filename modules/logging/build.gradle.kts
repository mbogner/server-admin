plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api(project(":shared"))
    api("org.slf4j:slf4j-api")

    testImplementation(project(":test-utils"))
}