package test

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object MsgMicroServer {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, environment = applicationEngineEnvironment {
            this.connector {
                host = "0.0.0.0"
                port = 20810
            }
            val lock = Mutex()
            module {
                routing {
                    post("/") {
                        lock.withLock {
                            println(call.receive<String>())
                        }
                    }
                }
            }
        }).start(true)
    }
}

object SvcServer {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, environment = applicationEngineEnvironment {
            this.connector {
                host = "0.0.0.0"
                port = 20811
            }
            val lock = Mutex()
            module {
                routing {
                    post("/") {
                        lock.withLock {
                            println(call.receive<String>())
                        }
                    }
                }
            }
        }).start(true)
    }
}

object UniServer {
    @JvmStatic
    fun main(args: Array<String>) {
        embeddedServer(Netty, environment = applicationEngineEnvironment {
            this.connector {
                host = "0.0.0.0"
                port = 20812
            }
            val lock = Mutex()
            module {
                routing {
                    post("/") {
                        lock.withLock {
                            println(call.receive<String>())
                        }
                    }
                }
            }
        }).start(true)
    }
}