plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    java
}

java {
    targetCompatibility = JavaVersion.VERSION_1_6
    sourceCompatibility = JavaVersion.VERSION_1_6
}

dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
}