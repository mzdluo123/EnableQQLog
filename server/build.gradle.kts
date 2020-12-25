plugins {
    kotlin("jvm")
}


val ktorVersion = "1.4.3"

dependencies {
    api("io.ktor:ktor-server-netty:$ktorVersion")
}