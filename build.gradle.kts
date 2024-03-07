import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    base
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.22"
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

allprojects {
    group = "my.okihouse"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven(url = "https://repo.spring.io/snapshot")
        maven(url = "https://repo.spring.io/milestone")
    }
}

subprojects {
    apply(plugin = "kotlin")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}


dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2020.0.4")
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Start-Class"] = "my.okihouse.FunctionApplication"
    }
}

tasks.assemble {
    dependsOn("shadowJar")
}

tasks.withType<ShadowJar> {
    archiveFileName.set("function.jar")
    dependencies {
        exclude("org.springframework.cloud:spring-cloud-function-web")
    }
    // Required for Spring
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(com.github.jengelman.gradle.plugins.shadow.transformers.PropertiesFileTransformer::class.java) {
        paths.add("META-INF/spring.factories")
        mergeStrategy = "append"
    }

    manifest {
        attributes["Start-Class"] = "my.okihouse.FunctionApplication"
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.0-M1")
    }
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
    implementation("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.cloud:spring-cloud-aws-context:2.2.6.RELEASE")
    implementation("org.springframework.cloud:spring-cloud-aws-messaging:2.2.6.RELEASE")
    implementation("com.amazonaws:aws-lambda-java-events:3.10.0")
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}