plugins {
    kotlin("jvm")
    java
}


val ktorVersion = "1.4.3"

dependencies {
    api(project(":common"))
    api("com.google.code.gson:gson:2.8.6")
    api("io.ktor:ktor-server-netty:$ktorVersion")
    api("org.slf4j:slf4j-simple:1.7.30")
}

java {
    targetCompatibility = org.gradle.api.JavaVersion.VERSION_1_6
    sourceCompatibility = org.gradle.api.JavaVersion.VERSION_1_6
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java) {
    kotlinOptions {
        jvmTarget = "1.8"
        targetCompatibility = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xjvm-target=1.8"
    }
}

val miraiVersion = "2.0-M2-dev-7"

dependencies {


    api("net.mamoe:mirai-core-api:$miraiVersion")
    api("net.mamoe:mirai-core-utils:$miraiVersion")
}