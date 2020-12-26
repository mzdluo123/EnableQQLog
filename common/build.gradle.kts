plugins {
    kotlin("jvm")
}

val miraiVersion = "2.0-M2-dev-5"

dependencies {


    api("net.mamoe:mirai-core-api:$miraiVersion")
    api("net.mamoe:mirai-core-utils:$miraiVersion")
}