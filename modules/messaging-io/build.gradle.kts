plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    api(project(":shared"))

    testImplementation(project(":test-utils"))
}