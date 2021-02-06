plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    java
}


val ktorVersion = "1.5.0"

dependencies {
    api(project(":common"))
    api("com.google.code.gson:gson:2.8.6")
    api("io.ktor:ktor-server-netty:$ktorVersion")
    api("org.slf4j:slf4j-simple:1.7.30")
    api("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.0.1")
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
        freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
    }
}

val miraiVersion = "2.3.2"

dependencies {


    //  api("net.mamoe:mirai-core-api:$miraiVersion") {
    //      exclude("com.squareup.okhttp3:okhttp:4.6.0")
    //  }
    api("net.mamoe:mirai-core-utils:$miraiVersion") {
        exclude("com.squareup.okhttp3", "okhttp")
    }
    api("net.mamoe:mirai-core:$miraiVersion") {
        exclude("com.squareup.okhttp3", "okhttp")
    }
}