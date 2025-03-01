plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

dependencies {
    implementation(platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
    implementation(platform(libs.spring.shell.dependencies))
    implementation(project(":messaging-shared"))
    implementation("org.springframework.shell:spring-shell-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    runtimeOnly(libs.hibernate.dialects)
    runtimeOnly(libs.sqlite.jdbc)
    runtimeOnly(libs.flyway.core)
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