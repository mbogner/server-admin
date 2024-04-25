rootProject.name = "server-admin"

val projectModules = mapOf(
    "sa-client" to "apps/sa-client",
    "sa-server" to "apps/sa-server",

    // modules
    "messaging-shared" to "modules/messaging-shared",
    "messaging-io" to "modules/messaging-io",
    "database-shared" to "modules/database-shared",
    "logging" to "modules/logging",
    "test-utils" to "modules/test-utils",
    "shared" to "modules/shared",
)

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // VERSIONS -------------------------------------------------------
            // https://plugins.gradle.org/plugin/org.springframework.boot
            version("spring.boot", "3.2.5")
            // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
            version("kotlin", "1.9.23")
            // https://mvnrepository.com/artifact/org.mockito.kotlin/mockito-kotlin
            version("mockito.kotlin", "5.3.1")
            // https://mvnrepository.com/artifact/org.springframework.shell/spring-shell-dependencies
            version("spring.shell", "3.2.3")
            // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
            version("sqlite.jdbc", "3.45.3.0")
            // https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-community-dialects
            // compare to the version of hibernate-core used in spring-boot
            // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-dependencies
            version("hibernate.dialects", "6.4.4.Final")
            // https://mvnrepository.com/artifact/org.flywaydb/flyway-core
            // https://mvnrepository.com/artifact/org.flywaydb/flyway-database-postgresql
            version("flyway", "10.11.1")
            // https://mvnrepository.com/artifact/org.apache.commons/commons-text
            version("commons.text", "1.12.0")

            // LIBRARIES ------------------------------------------------------
            library(
                "spring-boot-dependencies",
                "org.springframework.boot",
                "spring-boot-dependencies"
            ).versionRef("spring.boot")
            library(
                "mockito-kotlin",
                "org.mockito.kotlin",
                "mockito-kotlin"
            ).versionRef("mockito.kotlin")
            library(
                "spring-shell-dependencies",
                "org.springframework.shell",
                "spring-shell-dependencies"
            ).versionRef("spring.shell")
            library(
                "sqlite.jdbc",
                "org.xerial",
                "sqlite-jdbc"
            ).versionRef("sqlite.jdbc")
            library(
                "hibernate.dialects",
                "org.hibernate.orm",
                "hibernate-community-dialects"
            ).versionRef("hibernate.dialects")
            library(
                "flyway.core",
                "org.flywaydb",
                "flyway-core"
            ).versionRef("flyway")
            library(
                "flyway.postgresql",
                "org.flywaydb",
                "flyway-database-postgresql"
            ).versionRef("flyway")
            library(
                "commons.text",
                "org.apache.commons", "commons-text"
            ).versionRef("commons.text")

            // PLUGINS --------------------------------------------------------
            plugin("spring.boot", "org.springframework.boot").versionRef("spring.boot")
            plugin("kotlin.jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin.spring", "org.jetbrains.kotlin.plugin.spring").versionRef("kotlin")
        }
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

projectModules.forEach {
    include(it.key)
    project(":${it.key}").projectDir = file(it.value)
}