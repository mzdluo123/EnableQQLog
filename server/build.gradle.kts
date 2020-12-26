plugins {
    kotlin("jvm")
}


val ktorVersion = "1.4.3"
val miraiVersion = "2.0-M2-dev-5"

dependencies {
    api(project(":common"))
    api("com.google.code.gson:gson:2.8.6")
    api("io.ktor:ktor-server-netty:$ktorVersion")

    api("net.mamoe:mirai-core-api:$miraiVersion")
    api("net.mamoe:mirai-core-utils:$miraiVersion")
}