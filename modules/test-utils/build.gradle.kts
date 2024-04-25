plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))

    api("org.springframework.boot:spring-boot-starter-test")
    api(libs.mockito.kotlin)
}