plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

// utility  that can't have any dependencies except test
dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    testImplementation(project(":test-utils"))
}