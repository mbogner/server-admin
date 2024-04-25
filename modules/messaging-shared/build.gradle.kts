plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    api(project(":logging"))
    api(project(":messaging-io"))

    api("org.springframework.kafka:spring-kafka")

    api("org.springframework.boot:spring-boot-starter-json")
    api("org.springframework.boot:spring-boot-starter-validation")

    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation(project(":test-utils"))
}