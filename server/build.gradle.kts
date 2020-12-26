plugins {
    kotlin("jvm")
}


val ktorVersion = "1.4.3"

dependencies {
    api(project(":common"))
    api("com.google.code.gson:gson:2.8.6")
    api("io.ktor:ktor-server-netty:$ktorVersion")
    api("org.slf4j:slf4j-simple:1.7.30")
}