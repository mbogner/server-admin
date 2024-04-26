plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(platform(libs.spring.shell.dependencies))
    implementation(project(":messaging-shared"))
    implementation(project(":database-shared"))

    implementation("org.springframework.shell:spring-shell-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly(libs.flyway.core)
    runtimeOnly(libs.flyway.postgresql)
    runtimeOnly("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation(project(":test-utils"))
}

tasks {
    val javaVersion: String by project

    withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
        this.archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
        exclude(
            "application-local.yml",
        )
    }
}